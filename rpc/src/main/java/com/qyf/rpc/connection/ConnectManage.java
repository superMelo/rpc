package com.qyf.rpc.connection;

import java.util.List;
//连接管理
public interface ConnectManage {
    void updateConnectServer(List<String> addressList);

    Object select();

    void remove(Object obj);

    void addChannel(Object... obj);
}
