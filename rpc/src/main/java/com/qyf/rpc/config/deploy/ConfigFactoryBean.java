package com.qyf.rpc.config.deploy;
import com.qyf.rpc.annotion.ConfigBean;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;

public class ConfigFactoryBean<T> implements FactoryBean{
    private Class<T> rpcInterface;

    private Object targetObject;


    public ConfigFactoryBean(Class<T> rpcInterface, Object targetObject) {
        this.rpcInterface = rpcInterface;
        this.targetObject = targetObject;
    }

    public T getObject() throws Exception {
        Class<?> clz = targetObject.getClass();
        Method[] methods = clz.getDeclaredMethods();
        T t = null;
        for (Method method : methods) {
            if (method.getAnnotation(ConfigBean.class) != null){
                t = (T)method.invoke(targetObject);
            }
        }
        return t;
    }

    public Class<?> getObjectType() {
        return this.rpcInterface;
    }

    public boolean isSingleton() {
        return true;
    }

}
