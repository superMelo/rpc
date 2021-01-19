package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.register.api.Register;
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
    //zk服务注册目录
    protected static final String REGISTRY_PATH = "/rpc";

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    //注册
    public void doRegister(String url) throws Exception{
        if (StringUtil.isNotEmpty(url)){
            if (curator != null){
                //添加服务根节点
                createRootNode();
                //添加服务节点
                createNode(url);
            }
        }
    }


    @Override
    public void createRootNode() throws Exception{
        Stat stat = curator.checkExists().forPath(REGISTRY_PATH);
        if (stat == null){
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(REGISTRY_PATH);
            log.info("创建注册目录");
        }
    }

    @Override
    public void createNode(String url) throws Exception{
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(REGISTRY_PATH + "/provider", url.getBytes(Charset.defaultCharset()));
    }



}
