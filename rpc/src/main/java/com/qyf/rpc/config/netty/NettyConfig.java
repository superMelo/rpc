package com.qyf.rpc.config.netty;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.connection.http.HttpManage;
import com.qyf.rpc.connection.netty.NettyManage;
import com.qyf.rpc.register.zookeeper.ZookeeperRegister;
import com.qyf.rpc.remoting.http.server.HttpServer;
import com.qyf.rpc.remoting.netty.NettyProtocol;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;
import com.qyf.rpc.remoting.netty.server.NettyServer;

public class NettyConfig extends AbstractConfig{


    @Override
    public void loadClientConfig() {
        //加载netty客户端
        loadMap.put("protocol", NettyProtocol.class);
        //加载netty客户端
        loadMap.put("nettyClient", NettyClient.class);
        //加载handle
        loadMap.put("nettyClientHandle", NettyClientHandle.class);
        //加载连接管理
        loadMap.put("manage", NettyManage.class);
    }

    @Override
    public void loadServerConfig() {
        //加载注册
        loadMap.put("register", ZookeeperRegister.class);
        //加载netty服务端
        loadMap.put("nettyServer", NettyServer.class);
        loadMap.put("httpServer", HttpServer.class);
    }
}
