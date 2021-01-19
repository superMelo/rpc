package com.qyf.rpc.config;

import com.google.common.collect.Maps;
import com.qyf.rpc.discovery.zookeeper.ZkServiceDiscovery;
import com.qyf.rpc.eunm.Type;
import com.qyf.rpc.proxy.RpcFactory;
import com.qyf.rpc.register.zookeeper.ZkConfig;
import com.qyf.rpc.register.zookeeper.ZookeeperRegister;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Map;

public abstract class AbstractConfig implements Config {
    public Map<String, Class> loadMap = Maps.newHashMap();

    @Override
    public void register(BeanDefinitionRegistry registry, Type type) {
        doRegister(registry, type);
    }

    private void doRegister(BeanDefinitionRegistry registry, Type type){
        choose(type);
        loadMap.put("zkConfig", ZkConfig.class);
        loadMap.forEach((k, v) -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(k, definition);
        });
    }

    private void choose(Type type){
        if (type == Type.Client){
            //加载rpcFactory
            loadMap.put("rpcFactory", RpcFactory.class);
            chooseServiceDiscovery();
            loadClientConfig();
        }else {
            //加载注册
            loadMap.put("register", ZookeeperRegister.class);
            loadServerConfig();
        }
    }

    private void chooseServiceDiscovery(){
        //加载服务发现
        loadMap.put("discovery", ZkServiceDiscovery.class);
    }
}
