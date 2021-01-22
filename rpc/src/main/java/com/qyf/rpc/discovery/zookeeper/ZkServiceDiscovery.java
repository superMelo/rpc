package com.qyf.rpc.discovery.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.qyf.rpc.connection.api.ConnectManage;
import com.qyf.rpc.discovery.api.AbstractDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        TreeCache cache = new TreeCache(curator, ZK_REGISTRY_PATH);
        //获取所有服务接口
        List<String> services = curator.getChildren().forPath(ZK_REGISTRY_PATH);
        cache.getListenable().addListener((curatorFramework, event) -> {
            List<String> list = curator.getChildren().forPath(ZK_REGISTRY_PATH);
            logger.info("监听到子节点数据变化{}", JSONObject.toJSONString(list));
            addressList.clear();
            getNodeData(list);
//            getNodeData(curator.getChildren().forPath(ZK_REGISTRY_PATH));
            updateConnectedServer();
        });
        cache.start();
        getNodeData(services);
        logger.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }

    @Override
    public void updateConnectedServer() throws Exception {
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> services) throws Exception {

//        logger.info("rpc-"+serviceName+"子节点数据为:{}", JSONObject.toJSONString(nodes));
//        for(String node:nodes){
//            String address = new String(curator.getData().forPath(ZK_REGISTRY_PATH + "/" + node));
//            addressList.add(address);
//        }
        services.stream().forEach(service->{
            try {
                List<String> nodes = curator.getChildren().forPath(ZK_REGISTRY_PATH + "/" + service);
                CopyOnWriteArrayList<String> list = Lists.newCopyOnWriteArrayList();
                nodes.stream().forEach(s -> list.add(s));
                addressList.put(service, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
