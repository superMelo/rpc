package com.qyf.rpc.config.netty;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.proxy.RpcFactory;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.monitor.ConnectManage;
import com.qyf.rpc.monitor.ServiceDiscovery;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;

import java.util.Map;


public class ClientConfig extends AbstractConfig{
    @Override
    public Map<String, Class> loadConfig() {
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
}
