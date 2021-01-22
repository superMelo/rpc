package com.qyf.rpc.remoting.api;

import com.google.common.collect.Lists;
import com.qyf.rpc.annotion.RpcService;
import com.qyf.rpc.register.api.Register;
import com.qyf.rpc.remoting.netty.NettyServerProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractServerProtocol implements ServerProtocol, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(NettyServerProtocol.class);

    protected static Map<String, Object> serviceMap = new HashMap<>();


    protected static Map<String, Method> methodMap = new HashMap<>();
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for(Object serviceBean:beans.values()){

            Class<?> clazz = serviceBean.getClass();

            Class<?>[] interfaces = clazz.getInterfaces();

            for (Class<?> inter : interfaces){
                String interfaceName = inter.getName();
//                String[] split = interfaceName.split("\\.");
//                interfaceName = split[split.length - 1];
                log.info("加载服务类: {}", interfaceName);
                Method[] methods = inter.getMethods();
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
                serviceMap.put(interfaceName, serviceBean);
            }
        }
        log.info("已加载全部服务接口:{}", serviceMap);
    }

    protected void loadService(Register register, String url){
        Map<String, Object> serviceMap = AbstractServerProtocol.serviceMap;
        List<String> list = Lists.newLinkedList();
        serviceMap.forEach((k,v)-> list.add(k));
        list.stream().forEach(obj -> {
            try {
                log.info("服务地址和服务名称:{}", url + ":" + obj);
                //注册服务
                register.doRegister(url, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
