package com.qyf.rpc.config.http;

import com.qyf.rpc.config.api.AbstractConfig;
import com.qyf.rpc.connection.http.HttpConnectManage;
import com.qyf.rpc.remoting.http.HttpProtocol;
import com.qyf.rpc.remoting.http.client.HttpClient;
import com.qyf.rpc.remoting.http.server.HttpServer;
import com.qyf.rpc.remoting.http.server.WebConfig;

public class HttpConfig extends AbstractConfig{
    @Override
    public void loadClientConfig() {
        //加载连接管理
        loadMap.put("manage", HttpConnectManage.class);
        //加载http协议
        loadMap.put("protocol", HttpProtocol.class);
        //加载http客户端
        loadMap.put("httpClient", HttpClient.class);
    }

    @Override
    public void loadServerConfig() {
        //加载拦截配置
        loadMap.put("webConfig", WebConfig.class);
        //加载http服务端
        loadMap.put("httpServer", HttpServer.class);
    }
}
