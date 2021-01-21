package com.qyf.rpc.entity;

import lombok.Data;

@Data
public class Request {

    private String id;

    private String className; //类名

    private String methodName; //方法名

    //在服务方获取对应的方法参数类型
//    private Class<?>[] parameterTypes; //参数类型

    private Object[] parameters; //参数列表

    private String serverName;

}
