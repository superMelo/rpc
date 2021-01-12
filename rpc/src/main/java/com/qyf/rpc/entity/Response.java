package com.qyf.rpc.entity;

import lombok.Data;

@Data
public class Response {

    private String requestId; //请求id

    private int code; //状态码

    private String error_msg; //错误信息

    private Object data; //消息主体

}
