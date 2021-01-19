package com.qyf.rpc.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;

public class ClassUtils {

    /**
     * 获取参数列表
     * @param parameterTypes
     * @param parameters
     * @return
     */
    public static Object[] getParameters(Class<?>[] parameterTypes,Object[] parameters){
        if (parameters==null || parameters.length==0){
            return parameters;
        }else{
            Object[] new_parameters = new Object[parameters.length];
            for(int i=0;i<parameters.length;i++){
                new_parameters[i] = JSON.parseObject(JSON.toJSONString(parameters[i]),parameterTypes[i]);
            }
            return new_parameters;
        }
    }

    //根据clz和方法名获取method
    public static Method getMethod(Class<?> serviceClass, String methodName){
        Method[] methods = serviceClass.getMethods();
        Method method = null;
        for (Method m : methods) {
            if (methodName.equals(m.getName())){
                method = m;
                break;
            }
        }
        return method;
    }

}
