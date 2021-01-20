package com.qyf.rpc.register.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qyf.rpc.discovery.redis.RedisPubSub;
import com.qyf.rpc.discovery.redis.Subscribe;
import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisRegister extends AbstractRegister{


    @Autowired
    private Jedis client;

    @Autowired
    private Subscribe subscribe;


    private static final String REGISTRY_PATH_KEY = "registry_path_key";

    @Override
    public void doRegister(String url) throws Exception {
        if (StringUtil.isNotEmpty(url)){
            createNode(url);
        }
    }


    @Override
    public void createRootNode(){

    }

    @Override
    public void createNode(String url) {
        List<String> list = Lists.newLinkedList();
        list.add(url);
        client.set(REGISTRY_PATH_KEY, JSON.toJSONString(list));
        subscribe.register(url);
    }


}
