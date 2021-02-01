package com.qyf.rpc.config.deploy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qyf.rpc.annotion.ConfigBean;
import com.qyf.rpc.annotion.RpcConfig;
import com.qyf.rpc.annotion.Value;
import com.qyf.rpc.utils.ReflectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

@Component
public class ConfigBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private LocalApp localApp;

    @Autowired
    private Environment env;

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        //根据注解获取动态配置
        if (targetClass.isAnnotationPresent(RpcConfig.class)) {
            Field[] fields = targetClass.getDeclaredFields();
            try {
                Map<String, Object> config = getConfig(targetClass.getName());
                Map<String, Object> map = Maps.newHashMap();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Value annotation = field.getAnnotation(Value.class);
                    String value = annotation.value();
                    //判断zk是否存在配置
                    if (config == null) {
                        //获取配置文件的配置
                        Object o = getProperty(value);
                        ReflectUtils.getObjByType(field, bean, o);
                        map.put(annotation.value(), o);
                    } else {
                        Object o = config.get(value);
                        ReflectUtils.getObjByType(field, bean, o);
                    }
                }
                if (map.size() > 0) {
                    //保存到zk
                    setConfig(targetClass.getName(), map);
                }
                //添加bean到ioc
                Method[] declaredMethods = targetClass.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    ConfigBean configBean = declaredMethod.getAnnotation(ConfigBean.class);
                    if (configBean != null) {
                        Class<?> returnType = declaredMethod.getReturnType();
                        localApp.addBean(declaredMethod.getName(), returnType, bean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    private static final String CONFIG_NAME = "/config";

    //检查是否存在目录
    private boolean check(String name) throws Exception {
        String path = CONFIG_NAME + "/" + name;
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat != null) {
            return true;
        }
        return false;
    }

    //从zk获取指定配置
    private Map<String, Object> getConfig(String name) throws Exception {
        if (check(name)) {
            String s = new String(curatorFramework.getData().forPath(CONFIG_NAME + "/" + name));
            Map<String, Object> map = JSON.parseObject(s, Map.class);
            return map;
        } else {
            return null;
        }
    }

    //将配置上传到zk
    private void setConfig(String name, Map<String, Object> map) throws Exception {
        String path = CONFIG_NAME + "/" + name;
        String jsonString = JSON.toJSONString(map);
        if (check(name)) {
            curatorFramework.setData().forPath(path, jsonString.getBytes(Charset.defaultCharset()));
        } else {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath(path, jsonString.getBytes(Charset.defaultCharset()));
        }
    }

    //从配置文件获取配置信息
    private Object getProperty(String name) {
        return env.getProperty(name);
    }


}
