package com.qyf.rpc.config.deploy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qyf.rpc.annotion.ConfigBean;
import com.qyf.rpc.annotion.Value;
import com.qyf.rpc.exception.ConfigException;
import com.qyf.rpc.utils.ReflectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ConfigListener implements InitializingBean, ApplicationContextAware {


    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private LocalApp localApp;


    private static final String CONFIG_NAME = "/config";

    public static Map<String, String> configs = Maps.newConcurrentMap();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //创建目录
        createRoot();
        //监听配置
        listener();
    }

    public void listener() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curatorFramework, CONFIG_NAME, true);
        cache.getListenable().addListener((curatorFramework, event) -> {
            String path = event.getData().getPath();
            String[] names = path.split("/");
            String key = names[names.length - 1];
            String config = new String(event.getData().getData());
            Class<?> clz = Class.forName(key);
            //根据监听事件重新加载配置
            Object bean = applicationContext.getBean(clz);
            String localConfig = getField(clz, bean);
            //检查本地配置和远程配置是否相等
            if (check(config, localConfig)){
                return;
            }
            //重新设置属性
            setField(clz, bean);
            Method[] declaredMethods = clz.getDeclaredMethods();
            //循环获取配置的对象
            for (Method declaredMethod : declaredMethods) {
                ConfigBean configBean = declaredMethod.getAnnotation(ConfigBean.class);
                if (configBean != null) {
                    Class<?> returnType = declaredMethod.getReturnType();
                    String name = declaredMethod.getName();
                    //删除配置，重新加载配置
                    try {
                        Object obj = applicationContext.getBean(name);
                        if (obj != null) {
                            localApp.destroyBean(name);
                        }
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                    //创建bean
                    localApp.addBean(name, returnType, bean, declaredMethod);
                    //重新注入bean
                    String[] definitionNames = applicationContext.getBeanDefinitionNames();
                    Object obj = applicationContext.getBean(name);
                    for (String definitionName : definitionNames) {
                        Object o = applicationContext.getBean(definitionName);
                        Class<?> clzz = o.getClass();
                        Field[] declaredFields = clzz.getDeclaredFields();
                        for (Field declaredField : declaredFields) {
                            if (declaredField.getType() == returnType) {
                                declaredField.setAccessible(true);
                                declaredField.set(o, obj);
                            }
                        }
                    }
                }
            }
        });
        cache.start();
    }

    private void createRoot() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(CONFIG_NAME);
        if (stat == null) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(CONFIG_NAME);
        }
    }

    private Map<String, Object> getConfig(String name) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(CONFIG_NAME + "/" + name);
        if (stat != null) {
            byte[] bytes = curatorFramework.getData().forPath(CONFIG_NAME + "/" + name);
            String s = new String(bytes);
            Map<String, Object> map = JSON.parseObject(s, Map.class);
            return map;
        }
        return null;
    }

    private String getField(Class<?> targetClass, Object bean) {
        Field[] fields = targetClass.getDeclaredFields();
        Map<String, Object> map = Maps.newHashMap();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Value annotation = field.getAnnotation(Value.class);
                map.put(annotation.value(), field.get(bean));
            }
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean check(String config, String localConfig) {
        Map<String, Object> map = JSON.parseObject(config, Map.class);
        Map<String, Object> localConfigMap = JSON.parseObject(localConfig, Map.class);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String i = localConfigMap.get(key).toString();
            String j = map.get(key).toString();
            if (!i.equals(j)){
                return false;
            }
        }
        return true;
    }

    private void setField(Class<?> targetClass, Object bean) throws Exception {
        Field[] fields = targetClass.getDeclaredFields();
        Map<String, Object> config = getConfig(targetClass.getName());
        for (Field field : fields) {
            field.setAccessible(true);
            Value annotation = field.getAnnotation(Value.class);
            if (annotation != null) {
                String value = annotation.value();
                Object o = config.get(value);
                ReflectUtils.getObjByType(field, bean, o);
            } else {
                throw new ConfigException("配置属性不能为空");
            }
        }
    }


}
