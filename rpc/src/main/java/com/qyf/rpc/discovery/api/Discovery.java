package com.qyf.rpc.discovery.api;

public interface Discovery {

    void watchNode() throws Exception;

    void updateConnectedServer() throws Exception;
}
