package com.qyf.rpc.config.common;

import com.qyf.rpc.annotion.ConfigBean;
import com.qyf.rpc.annotion.RpcConfig;
import com.qyf.rpc.annotion.Value;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RpcConfig
public class RedissonConfig {


    @Value("redis.host")
    private String host;

    @Value("redis.port")
    private int port;

//    @Bean
    @ConfigBean
    public RedissonClient redissonClient(){
        Config config = new Config();
        String url = "redis://" + host +":" + port;
        ((SingleServerConfig)config.useSingleServer().setTimeout(1000000)).setAddress(url);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
//        return Redisson.create();
    }
}
