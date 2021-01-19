package com.qyf.rpc.discovery;

public interface Discovery {

    void watchNode() throws Exception;

    void updateConnectedServer() throws Exception;
}
