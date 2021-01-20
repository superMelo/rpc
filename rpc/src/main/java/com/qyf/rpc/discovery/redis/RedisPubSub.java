package com.qyf.rpc.discovery.redis;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

import java.util.concurrent.atomic.AtomicInteger;

public class RedisPubSub extends JedisPubSub{

    public static final Map<String, AtomicInteger> serviceMap = Maps.newConcurrentMap();
    private Logger log = LoggerFactory.getLogger(this.getClass());

    //订阅
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
       log.info("订阅的channel:{}", channel + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info("退出订阅的channel:{}", channel + subscribedChannels);
    }

    @Override
    public void unsubscribe() {
        log.info("取消订阅");
    }

    @Override
    public void onMessage(String channel, String message) {
        AtomicInteger i = serviceMap.get(message);
        synchronized (serviceMap){
            if (i != null){
                i.set(1);
            }else {
                serviceMap.put(message, new AtomicInteger(1));
            }
        }
        log.info(channel + "-收到心跳消息:{}", message);
    }


}
