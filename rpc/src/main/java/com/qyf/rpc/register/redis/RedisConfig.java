package com.qyf.rpc.register.redis;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;


@Configuration
public class RedisConfig {


    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;


    @Bean
    public Jedis getJedis(){
        Jedis jedis = new Jedis(host, port);
        return jedis;
    }

}
