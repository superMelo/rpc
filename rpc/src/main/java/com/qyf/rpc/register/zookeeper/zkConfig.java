package com.qyf.rpc.register.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class zkConfig {

    @Value("${registry.address}")
    private String registry;

    private static final int CONNECTION_TIMEOUT_MS = 20000;

    private static final int SESSION_TIMEOUT = 20000;

    @Bean
    //连接zookeeper，获取zk客户端
    public CuratorFramework getCuratorFramework(){
        //重试策略，初试时间1秒，重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(registry)
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(policy).build();
        curatorFramework.start();
        return curatorFramework;
    }
}
