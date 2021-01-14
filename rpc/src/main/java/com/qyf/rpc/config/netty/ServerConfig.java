package com.qyf.rpc.config.netty;

import com.qyf.rpc.config.AbstractConfig;
import com.qyf.rpc.register.zookeeper.ZookeeperServiceRegistry;
import com.qyf.rpc.remoting.netty.server.NettyServer;

import java.util.Map;

public class ServerConfig extends AbstractConfig {
    @Override
    public Map<String, Class> loadConfig() {
        map.put("zookeeperServiceRegistry", ZookeeperServiceRegistry.class);
        map.put("nettyServer", NettyServer.class);
        return map;
    }

}
