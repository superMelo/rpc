package com.qyf.rpc.discovery.redis;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.connection.ConnectManage;
import com.qyf.rpc.discovery.AbstractDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//服务发现
public class RedisServiceDiscovery extends AbstractDiscovery{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Jedis jedis;

    @Autowired
    private ConnectManage connectManage;

    private static final String REGISTRY_PATH_KEY = "registry_path_key";

    @Override
    public void watchNode() throws Exception {
        //从redis加载所有服务的地址
        getNodeData();
        //监听地址
        addListener();
        //心跳监测
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(()->{
            log.info("心跳监测");
            serviceMap.forEach((k, v)->{
                if (v.intValue() == 0){
                    log.info(k + ":连接断开");
                    try {
                        //去除redis里的服务地址
                        deleteNode(k);
                        //更新服务本地列表
                        updateConnectedServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    v.set(0);
                }
            });
        }, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void updateConnectedServer() throws Exception {
        connectManage.updateConnectServer(addressList);
    }


    private void deleteNode(String node){
        String str = jedis.get(REGISTRY_PATH_KEY);
        List<String> list = JSON.parseObject(str, List.class);
        list.remove(node);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        watchNode();
    }

    private static final String REDIS_REGISTER = "rpc";

    //监听地址
    private void addListener(){
        new Thread(()->{
            RedisPubSub listener = new RedisPubSub();
            jedis.subscribe(listener, REDIS_REGISTER);
        }).start();
    }

    //加载服务列表
    private void getNodeData(){
        String str = jedis.get(REGISTRY_PATH_KEY);
        List<String> list = JSON.parseObject(str, List.class);
        //保存到服务本地列表
        addressList.addAll(list);
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        list.stream().forEach(s -> serviceMap.put(s, new AtomicInteger(0)));
    }

}
