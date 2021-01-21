package com.qyf.rpc.register.api;

public interface Register {

    void doRegister(String url, String className) throws Exception;


    void createNode(String url, String className) throws Exception;
}
