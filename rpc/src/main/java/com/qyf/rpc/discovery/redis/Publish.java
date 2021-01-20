package com.qyf.rpc.discovery.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Publish {

    @Autowired
    Jedis jedis;

    //发送心跳消息，维持可用状态
    public void publish(String url){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(() ->
            jedis.publish(url, "测试")
        , 1, 1, TimeUnit.SECONDS);
    }
}
