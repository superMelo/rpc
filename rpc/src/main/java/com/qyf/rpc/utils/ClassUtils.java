package com.qyf.rpc.utils;

import com.alibaba.fastjson.JSON;

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
                new_parameters[i] = JSON.parseObject(parameters[i].toString(),parameterTypes[i]);
            }
            return new_parameters;
        }
    }
}
