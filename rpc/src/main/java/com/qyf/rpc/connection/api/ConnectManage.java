package com.qyf.rpc.connection.api;

import com.qyf.rpc.entity.Request;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

//连接管理
public interface ConnectManage {
    void updateConnectServer(Map<String, CopyOnWriteArrayList<String>> addressList);

    Object select(Request request);

    void remove(Object obj);

    void addChannel(Object... obj);
}
