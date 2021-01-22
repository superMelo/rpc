package com.qyf.rpc.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Component
public class RedisLock {

    @Autowired
    private RedissonClient redissonClient;

    public void lock(String name){
        RLock lock = redissonClient.getLock(name);
        lock.lock();
    }

    public void unLock(String name){
        RLock lock = redissonClient.getLock(name);
        lock.unlock();
    }

    public void doLock(String name, Execute execute) throws Exception {
        try {
            lock(name);
            execute.doExecute();
        }finally {
            unLock(name);
        }
    }
}
