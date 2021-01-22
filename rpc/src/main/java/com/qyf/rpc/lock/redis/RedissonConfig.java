package com.qyf.rpc.lock.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {


    @Bean
    public RedissonClient getRedissonClient(){
        RedissonClient redissonClient = Redisson.create();
        return redissonClient;
    }
}
