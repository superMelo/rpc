package com.qyf.rpc.connection;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

//服务发现
public class ServiceDiscovery {

    @Value("${registry.address}")
    private String registryAddress;

    @Autowired
    Manage manage;

    // 服务地址列表
    private volatile List<String> addressList = new ArrayList<>();
    private static final String ZK_REGISTRY_PATH = "/rpc";

    @Autowired
    private CuratorFramework curator;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() throws Exception{
        if (curator != null) {
            watchNode(curator);
        }
    }


    private void watchNode(final CuratorFramework client) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, ZK_REGISTRY_PATH, true);
        List<String> nodes = client.getChildren().forPath(ZK_REGISTRY_PATH);
        cache.getListenable().addListener((curatorFramework, event)->{
            logger.info("监听到子节点数据变化{}", JSONObject.toJSONString(event.getInitialData()));
            addressList.clear();
            getNodeData(client.getChildren().forPath(ZK_REGISTRY_PATH), client);
            updateConnectedServer();
        });
        cache.start();
        getNodeData(nodes, client);
        logger.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }
    private void updateConnectedServer(){
        manage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> nodes, CuratorFramework client) throws Exception {
        logger.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
        for(String node:nodes){
            String address = new String(client.getData().forPath(ZK_REGISTRY_PATH + "/" + node));
            addressList.add(address);
        }
    }
}
