package com.qyf.rpc.register.api;

public interface Register {

    void doRegister(String url, String serverName) throws Exception;


    void createNode(String url, String serverName) throws Exception;
}
