package com.qyf.rpc.entity;

import lombok.Data;

@Data
public class Request {

    private String id;

    private String clzId; //类名相同时的区分标识，唯一

    private String className; //类名

    private String methodName; //方法名

    //在服务方获取对应的方法参数类型
//    private Class<?>[] parameterTypes; //参数类型

    private Object[] parameters; //参数列表


}
