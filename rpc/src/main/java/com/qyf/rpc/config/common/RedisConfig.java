package com.qyf.rpc.config.common;


import com.qyf.rpc.annotion.ConfigBean;
import com.qyf.rpc.annotion.RpcConfig;
import com.qyf.rpc.annotion.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@RpcConfig
public class RedisConfig {



    @Value("redis.host")
    private String host;
    @Value("redis.port")
    private int port;

    @ConfigBean
    public Jedis jedis(){
        Jedis jedis = new Jedis(host, port);
        return jedis;
    }

}
