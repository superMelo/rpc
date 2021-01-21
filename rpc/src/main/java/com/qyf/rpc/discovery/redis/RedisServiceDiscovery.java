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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
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
        updateConnectedServer();
        //监听地址
        addListener();
    }

    @Override
    public void updateConnectedServer() throws Exception {
        connectManage.updateConnectServer(addressList);
    }


    private void deleteNode(String node){
        String[] strs = node.split(":");
        String jsonStr = jedis.get(REGISTRY_PATH_KEY);
        Map<String, CopyOnWriteArrayList<String>> serviceMap = JSON.parseObject(jsonStr, Map.class);
        CopyOnWriteArrayList<String> sets = serviceMap.get(strs[0]);
        synchronized (sets){
            if (sets != null){
                sets.remove(strs[1]);
                serviceMap.put(strs[0], sets);
                jedis.set(REGISTRY_PATH_KEY, JSON.toJSONString(serviceMap));
                addressList.clear();
                addressList = serviceMap;
            }
        }
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
            Jedis pubClient = new Jedis("127.0.0.1", 6379);
            pubClient.subscribe(listener, REDIS_REGISTER);
        }).start();
    }

    //加载服务列表
    private void getNodeData(){
        //获取redis的服务名称和地址
        String str = jedis.get(REGISTRY_PATH_KEY);
        Map<String, CopyOnWriteArrayList<String>> map = JSON.parseObject(str, Map.class);
        //保存到服务本地列表
        addressList = map;
        //给每个服务加上监听
        Map<String, AtomicInteger> serviceMap = RedisPubSub.serviceMap;
        addressList.forEach((k, v)-> v.stream().forEach(s -> serviceMap.put(k + ":" + s, new AtomicInteger(1))));
    }

}
