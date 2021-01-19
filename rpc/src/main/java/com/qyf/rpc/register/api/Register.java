package com.qyf.rpc.register.api;

public interface Register {

    void doRegister(String url) throws Exception;

    void createRootNode() throws Exception;

    void createNode(String url) throws Exception;
}
