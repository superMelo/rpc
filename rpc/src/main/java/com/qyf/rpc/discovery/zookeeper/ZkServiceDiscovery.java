package com.qyf.rpc.discovery.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.qyf.rpc.connection.ConnectManage;
import com.qyf.rpc.discovery.AbstractDiscovery;
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
public class ZkServiceDiscovery extends AbstractDiscovery {


    @Autowired
    ConnectManage connectManage;

    private static final String ZK_REGISTRY_PATH = "/rpc";

    @Autowired
    private CuratorFramework curator;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void afterPropertiesSet() throws Exception {
        if (curator != null) {
            watchNode();
        }
    }

    @Override
    public void watchNode() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curator, ZK_REGISTRY_PATH, true);
        List<String> nodes = curator.getChildren().forPath(ZK_REGISTRY_PATH);
        cache.getListenable().addListener((curatorFramework, event) -> {
            logger.info("监听到子节点数据变化{}", JSONObject.toJSONString(event.getInitialData()));
            addressList.clear();
            getNodeData(curator.getChildren().forPath(ZK_REGISTRY_PATH));
            updateConnectedServer();
        });
        cache.start();
        getNodeData(nodes);
        logger.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }

    @Override
    public void updateConnectedServer() throws Exception {
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> nodes) throws Exception {
        logger.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
        for(String node:nodes){
            String address = new String(curator.getData().forPath(ZK_REGISTRY_PATH + "/" + node));
            addressList.add(address);
        }
    }
}
