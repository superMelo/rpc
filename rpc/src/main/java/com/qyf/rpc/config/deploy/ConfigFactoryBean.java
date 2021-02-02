package com.qyf.rpc.config.deploy;
import com.qyf.rpc.annotion.ConfigBean;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;

public class ConfigFactoryBean<T> implements FactoryBean{
    private Class<T> rpcInterface;

    private Method method;
    private Object targetObj;

    public ConfigFactoryBean(Class<T> rpcInterface, Object targetObj, Method method) {
        this.rpcInterface = rpcInterface;
        this.method = method;
        this.targetObj = targetObj;
    }

    public T getObject() throws Exception {
//        Class<?> clz = targetObject.getClass();
//        Method[] methods = clz.getDeclaredMethods();
//        T t = null;
//        for (Method method : methods) {
//            if (method.getAnnotation(ConfigBean.class) != null){
//                t = (T)method.invoke(targetObject);
//            }
//        }
        T t = (T)method.invoke(targetObj);
        return t;
    }

    public Class<?> getObjectType() {
        return this.rpcInterface;
    }

    public boolean isSingleton() {
        return true;
    }

}
