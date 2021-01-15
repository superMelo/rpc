package com.qyf.rpc.config.netty;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.monitor.ConnectManage;
import com.qyf.rpc.monitor.ServiceDiscovery;
import com.qyf.rpc.proxy.RpcFactory;
import com.qyf.rpc.register.zookeeper.ZookeeperRegister;
import com.qyf.rpc.remoting.netty.NettyProtocol;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;
import com.qyf.rpc.remoting.netty.server.NettyServer;

import java.util.Map;

public class NettyConfig extends AbstractConfig{


    @Override
    public Map<String, Class> loadClientConfig() {
        //加载netty客户端
        map.put("protocol", NettyProtocol.class);
        //加载netty客户端
        map.put("nettyClient", NettyClient.class);
        //加载服务发现
        map.put("serviceDiscovery", ServiceDiscovery.class);
        //加载连接管理
        map.put("connectManage", ConnectManage.class);
        //加载handle
        map.put("nettyClientHandle", NettyClientHandle.class);
        //加载rpcFactory
        map.put("rpcFactory", RpcFactory.class);
        return map;
    }

    @Override
    public Map<String, Class> loadServerConfig() {
        //加载注册
        map.put("register", ZookeeperRegister.class);
        //加载netty服务端
        map.put("nettyServer", NettyServer.class);
        return map;
    }
}
