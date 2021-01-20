package com.qyf.rpc.discovery.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscribe {

    private Logger log  = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Jedis jedis;

    private static final String REDIS_REGISTER = "rpc";

    //监听地址
    public void listener(){
        RedisPubSub listener = new RedisPubSub();
        jedis.subscribe(listener, REDIS_REGISTER);
    }

    //注册所有服务地址
    public void register(String url){
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        serviceMap.put(url, new AtomicInteger(0));
    }

    //心跳检查每个服务是否可用
    public void heartbeat(){
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(()->{
            serviceMap.forEach((k, v)->{
                if (v.intValue() == 0){
                    log.info(k + ":连接断开");
                }else {
                    v.set(0);
                }
            });
        }, 1, 2, TimeUnit.SECONDS);
    }
}
