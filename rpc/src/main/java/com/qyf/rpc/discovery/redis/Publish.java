package com.qyf.rpc.discovery.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Publish {

    private static final String REDIS_REGISTER = "rpc";
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Jedis jedis;

    //发送心跳消息，维持可用状态
    public void publish(String url){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(() ->{
            log.info("发送心跳维持连接:{}", url);
            jedis.publish(REDIS_REGISTER, url);
        }, 1, 3, TimeUnit.SECONDS);
    }
}
