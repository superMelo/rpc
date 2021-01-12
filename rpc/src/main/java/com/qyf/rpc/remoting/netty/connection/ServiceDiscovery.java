package com.qyf.rpc.remoting.netty.connection;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
//服务发现
public class ServiceDiscovery {

    @Value("${registry.address}")
    private String registryAddress;

    @Autowired
    ConnectManage connectManage;

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
        List<ChildData> nodes = new LinkedList<>();
        cache.getListenable().addListener((curatorFramework, event)->{
            logger.info("监听到子节点数据变化{}", JSONObject.toJSONString(event.getInitialData()));
            addressList.clear();
            nodes.addAll(event.getInitialData());
            getNodeData(event.getInitialData());
            updateConnectedServer();
        });
        getNodeData(nodes);
        logger.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }
    private void updateConnectedServer(){
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<ChildData> nodes) throws Exception {
        logger.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
        for(ChildData node:nodes){
            String address = curator.getData().forPath(ZK_REGISTRY_PATH + "/" + node.getPath()).toString();
            addressList.add(address);
        }
    }
}
