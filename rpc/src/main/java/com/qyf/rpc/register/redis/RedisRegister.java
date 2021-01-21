package com.qyf.rpc.register.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qyf.rpc.discovery.redis.Publish;
import com.qyf.rpc.discovery.redis.RedisPubSub;
import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisRegister extends AbstractRegister{


    @Autowired
    private Jedis client;

    @Autowired
    private Publish publish;



    @Override
    public void doRegister(String url, String className) throws Exception {
        if (StringUtil.isNotEmpty(url, className)){
            createNode(url, className);
        }
    }

    @Override
    public void createNode(String url, String className) throws Exception {
        String val = client.get(REGISTRY_PATH_KEY);
        Map<String, Set<String>> map;
        Set<String> urls;
        if (StringUtil.isNotEmpty(val)){
            map = JSON.parseObject(val, Map.class);
            urls = map.get(url);
            if (urls != null){
                urls.add(url);
            }
            map.put(className, urls);
        }else {
            map = Maps.newHashMap();
            urls = Sets.newHashSet();
            urls.add(url);
            map.put(className, urls);
        }
        //注册服务地址
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        serviceMap.put(url, new AtomicInteger(1));
        //保存服务地址到redis
        client.set(REGISTRY_PATH_KEY, JSON.toJSONString(map));
        //发送消息，保持心跳
        url = className + "-" + url;
        publish.publish(url);
    }



}
