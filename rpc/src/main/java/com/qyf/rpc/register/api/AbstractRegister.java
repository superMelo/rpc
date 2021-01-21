package com.qyf.rpc.register.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qyf.rpc.utils.StringUtil;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractRegister implements Register{

    protected static final String REGISTRY_PATH_KEY = "registry_path_key";

    protected Map<String, CopyOnWriteArrayList<String>> serviceMap = Maps.newConcurrentMap();

    protected  CopyOnWriteArrayList<String> urls = Lists.newCopyOnWriteArrayList();

    protected void createService(String service, String url, String className){
        if (StringUtil.isNotEmpty(service)){
            serviceMap = JSON.parseObject(service, Map.class);
            urls = serviceMap.get(url) != null ?  serviceMap.get(url): urls;
            urls.add(url);
            serviceMap.put(className, urls);
        }else {
            serviceMap = Maps.newHashMap();
            urls = new CopyOnWriteArrayList();
            urls.add(url);
            serviceMap.put(className, urls);
        }
    }
}
