package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.lock.redis.RedisLock;
import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.utils.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;


public class ZookeeperRegister extends AbstractRegister{


    @Autowired
    private CuratorFramework curator;

    @Autowired
    private RedisLock redisLock;

    //zk服务注册目录
    protected static final String REGISTRY_PATH = "/rpc";


    Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public void doRegister(String url, String serverName) throws Exception {
        if (StringUtil.isNotEmpty(url)){
            if (curator != null){
                //添加服务节点
                createNode(url, serverName);
            }
        }
    }

    @Override
    public void createNode(String url, String className) throws Exception {
        redisLock.doLock(LOCK_NAME, ()->{
            Stat stat = curator.checkExists().forPath(REGISTRY_PATH);
            if (stat == null){
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(REGISTRY_PATH);
                log.info("创建注册目录");
                //创建服务接口目录
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(REGISTRY_PATH + "/" + className);
                //创建服务接口地址节点
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                        .forPath(REGISTRY_PATH + "/" + className + "/" + url, url.getBytes(Charset.defaultCharset()));
            }else {
                //获取服务对应的可用地址
                Stat clzPath = curator.checkExists().forPath(REGISTRY_PATH + "/" + className);
                if (clzPath != null){
                    Stat serviceUrl = curator.checkExists().forPath(REGISTRY_PATH + "/" + className + "/" + url);
                    if (serviceUrl == null){
                        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                                .forPath(REGISTRY_PATH + "/" + className + "/" + url, url.getBytes(Charset.defaultCharset()));
                    }
                }
            }
        });
    }


}
