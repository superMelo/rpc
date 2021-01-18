package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.register.Register;
import com.qyf.rpc.utils.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;

public class ZookeeperRegister implements Register{


    @Autowired
    private CuratorFramework curator;


    Logger log = LoggerFactory.getLogger(this.getClass());



    //zk服务注册目录
    private static final String ZK_REGISTRY_PATH = "/rpc";



    @Override
    //注册
    public void doRegister(String url) throws Exception{
        if (StringUtil.isNotEmpty(url)){
            if (curator != null){
                //添加服务根节点
                addRootNode(curator);
                //添加服务节点
                createNode(curator, url);
            }
        }
    }


    private void addRootNode(CuratorFramework curator) throws Exception {
        Stat stat = curator.checkExists().forPath(ZK_REGISTRY_PATH);
        if (stat == null){
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ZK_REGISTRY_PATH);
            log.info("创建注册目录");
        }
    }

    private void createNode(CuratorFramework curator, String data) throws Exception {
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(ZK_REGISTRY_PATH + "/provider", data.getBytes(Charset.defaultCharset()));
    }
}