package com.qyf.rpc.discovery.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscribe {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Jedis jedis;

    private static final String REDIS_REGISTER = "rpc";
    private static final String REGISTRY_PATH_KEY = "registry_path_key";



}
