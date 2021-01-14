package com.qyf.rpc.config.netty;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.proxy.RpcFactory;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.connection.ConnectManage;
import com.qyf.rpc.remoting.netty.connection.ServiceDiscovery;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;

import java.util.Map;


public class ClientConfig extends AbstractConfig{
    @Override
    public Map<String, Class> loadConfig() {
        map.put("nettyClient", NettyClient.class);
        map.put("serviceDiscovery", ServiceDiscovery.class);
        map.put("connectManage", ConnectManage.class);
        map.put("nettyClientHandle", NettyClientHandle.class);
        map.put("rpcFactory", RpcFactory.class);
        return map;
    }
}
