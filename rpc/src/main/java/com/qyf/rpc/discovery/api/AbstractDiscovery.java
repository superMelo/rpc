package com.qyf.rpc.discovery.api;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;


import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractDiscovery implements Discovery, InitializingBean{

    // 服务地址列表
    protected volatile Map<String, CopyOnWriteArrayList<String>> addressList = Maps.newConcurrentMap();


    protected static final String REGISTRY_PATH_KEY = "registry_path_key";

}
