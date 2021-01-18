package com.qyf.rpc.config;

import com.google.common.collect.Maps;
import com.qyf.rpc.connection.netty.NettyManage;
import com.qyf.rpc.connection.ServiceDiscovery;
import com.qyf.rpc.proxy.RpcFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Map;

public abstract class AbstractConfig implements Config {
    public Map<String, Class> loadMap = Maps.newHashMap();

    @Override
    public void register(BeanDefinitionRegistry registry, int type) {
        doRegister(registry, type);
    }

    private void doRegister(BeanDefinitionRegistry registry, int type){
        if (type == 0){
            //加载rpcFactory
            loadMap.put("rpcFactory", RpcFactory.class);
            //加载服务发现
            loadMap.put("serviceDiscovery", ServiceDiscovery.class);
            loadClientConfig();
        }else {
            loadServerConfig();
        }
        loadMap.forEach((k, v) -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(k, definition);
        });
    }
}