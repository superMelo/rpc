package com.qyf.rpc.discovery;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractDiscovery implements Discovery, InitializingBean{

    // 服务地址列表
    protected volatile Map<String, CopyOnWriteArrayList<String>> addressList = Maps.newConcurrentMap();


    protected static final String REGISTRY_PATH_KEY = "registry_path_key";

}
