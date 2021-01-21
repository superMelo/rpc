package com.qyf.rpc.register.zookeeper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qyf.rpc.register.api.AbstractRegister;
import com.qyf.rpc.utils.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ZookeeperRegister extends AbstractRegister{


    @Autowired
    private CuratorFramework curator;
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
        Stat stat = curator.checkExists().forPath(REGISTRY_PATH);
        if (stat == null){
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(REGISTRY_PATH);
            log.info("创建注册目录");
            CopyOnWriteArrayList<String> urls = Lists.newCopyOnWriteArrayList();
            urls.add(url);
            String jsonStr = JSON.toJSONString(urls);
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(REGISTRY_PATH + "/" + className, jsonStr.getBytes(Charset.defaultCharset()));
        }else {
            //获取服务对应的可用地址
            String node = new String(curator.getData().forPath(REGISTRY_PATH + "/" + className));
            CopyOnWriteArrayList list = JSON.parseObject(node, CopyOnWriteArrayList.class);
            list.add(url);
            String jsonStr = JSON.toJSONString(list);
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(REGISTRY_PATH + "/" + className, jsonStr.getBytes(Charset.defaultCharset()));
        }
    }


}
