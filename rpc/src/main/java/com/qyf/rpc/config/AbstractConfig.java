package com.qyf.rpc.config;

import com.google.common.collect.Maps;
import com.qyf.rpc.discovery.redis.Publish;
import com.qyf.rpc.discovery.redis.RedisServiceDiscovery;
import com.qyf.rpc.discovery.zookeeper.ZkServiceDiscovery;
import com.qyf.rpc.eunm.RegisterType;
import com.qyf.rpc.eunm.Type;
import com.qyf.rpc.proxy.RpcFactory;
import com.qyf.rpc.register.redis.RedisRegister;
import com.qyf.rpc.register.zookeeper.ZkConfig;
import com.qyf.rpc.register.zookeeper.ZookeeperRegister;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Map;

public abstract class AbstractConfig implements Config {
    public Map<String, Class> loadMap = Maps.newHashMap();

    @Override
    public void register(BeanDefinitionRegistry registry, Type type, RegisterType registerType) {
        doRegister(registry, type, registerType);
    }

    private void doRegister(BeanDefinitionRegistry registry, Type type, RegisterType registerType){
        choose(type, registerType);
        loadMap.put("zkConfig", ZkConfig.class);
        loadMap.forEach((k, v) -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(k, definition);
        });
    }

    private void choose(Type type, RegisterType registerType){
        switch (type){
            case Client:
                //加载rpcFactory
                loadMap.put("rpcFactory", RpcFactory.class);
                chooseServiceDiscovery(registerType, type);
                loadClientConfig();
                break;
            case Server:
                //加载注册
                chooseServiceDiscovery(registerType, type);
                loadServerConfig();
                break;
        }
    }

    //加载服务发现
    private void chooseServiceDiscovery(RegisterType registerType, Type type){
        switch (registerType){
            case Zk:
                if (type == Type.Client){
                    loadMap.put("discovery", ZkServiceDiscovery.class);
                }else {
                    loadMap.put("register", ZookeeperRegister.class);
                }
                break;
            case Redis:
                if (type == Type.Client){
                    loadMap.put("discovery", RedisServiceDiscovery.class);
                }else {
                    loadMap.put("publish", Publish.class);
                    loadMap.put("register", RedisRegister.class);
                }
                break;
        }
    }
}
