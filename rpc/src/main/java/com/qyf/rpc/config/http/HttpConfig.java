package com.qyf.rpc.config.http;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.connection.http.HttpConnectManage;
import com.qyf.rpc.register.zookeeper.ZookeeperRegister;
import com.qyf.rpc.remoting.http.client.HttpClient;
import com.qyf.rpc.remoting.http.server.HttpServer;
import com.qyf.rpc.remoting.http.server.WebConfig;

public class HttpConfig extends AbstractConfig{
    @Override
    public void loadClientConfig() {
        loadMap.put("protocol", HttpClient.class);
        //加载连接管理
        loadMap.put("manage", HttpConnectManage.class);
    }

    @Override
    public void loadServerConfig() {
        loadMap.put("webConfig", WebConfig.class);
        loadMap.put("httpServer", HttpServer.class);
        loadMap.put("register", ZookeeperRegister.class);
    }
}
