package com.qyf.rpc.register.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qyf.rpc.discovery.redis.Publish;
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
    private Publish publish;



    private static final String REGISTRY_PATH_KEY = "registry_path_key";

    @Override
    public void doRegister(String url) throws Exception {
        if (StringUtil.isNotEmpty(url)){
            createNode(url);
        }
    }



    @Override
    public void createNode(String url) {
        List<String> list = Lists.newLinkedList();
        list.add(url);
        //注册服务地址
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        serviceMap.put(url, new AtomicInteger(0));
        //保存服务地址到redis
        client.set(REGISTRY_PATH_KEY, JSON.toJSONString(list));
        //发送消息，保持心跳
        publish.publish(url);
    }


}
