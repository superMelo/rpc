package com.qyf.rpc.config;

import com.google.common.collect.Maps;
import com.qyf.rpc.remoting.AbstractProtocol;
import com.qyf.rpc.remoting.Protocol;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public abstract class AbstractConfig implements Config {
    public Map<String, Class> map = Maps.newHashMap();

    @Override
    public void register(BeanDefinitionRegistry registry, int type) {
        doRegister(registry, type);
    }

    private void doRegister(BeanDefinitionRegistry registry, int type){
        if (type == 0){
            loadClientConfig();
        }else {
            loadServerConfig();
        }
        map.forEach((k, v) -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(k, definition);
        });
    }
}
