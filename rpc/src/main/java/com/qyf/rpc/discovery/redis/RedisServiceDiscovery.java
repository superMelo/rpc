package com.qyf.rpc.discovery.redis;

import com.qyf.rpc.discovery.AbstractDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

public class RedisServiceDiscovery extends AbstractDiscovery{

    @Autowired
    private Jedis jedis;

    @Override
    public void watchNode() throws Exception {

    }

    @Override
    public void updateConnectedServer() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
