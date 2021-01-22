package com.qyf.rpc.register.redis;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.discovery.redis.Publish;
import com.qyf.rpc.discovery.redis.RedisPubSub;
import com.qyf.rpc.lock.redis.RedisLock;
import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;


import java.util.concurrent.atomic.AtomicInteger;

public class RedisRegister extends AbstractRegister{


    @Autowired
    private Jedis client;

    @Autowired
    private Publish publish;

    @Autowired
    private RedisLock redisLock;

    @Override
    public void doRegister(String url, String className) throws Exception {
        if (StringUtil.isNotEmpty(url, className)){
            createNode(url, className);
        }
    }

    @Override
    public void createNode(String url, String className) throws Exception {
        redisLock.doLock(LOCK_NAME, ()->{
            //从redis获取服务列表
            String service = client.get(REGISTRY_PATH_KEY);
            createService(service, url, className);
            //注册服务地址
            RedisPubSub.serviceMap.put(url, new AtomicInteger(1));
            //保存服务地址到redis
            client.set(REGISTRY_PATH_KEY, JSON.toJSONString(serviceMap));
            //发送消息，保持心跳
            publish.publish(className + "-" + url);
        });
    }



}
