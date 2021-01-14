package com.qyf.rpc.config;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Map;

public abstract class AbstractConfig implements Config {
    public Map<String, Class> map = Maps.newHashMap();

    @Override
    public Map<String, Class> loadConfig() {
        return null;
    }

    @Override
    public void register(BeanDefinitionRegistry registry) {
        doRegister(registry);
    }

    private void doRegister(BeanDefinitionRegistry registry){
        loadConfig();
        map.forEach((k, v) -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(v);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(k, definition);
        });
    }
}
