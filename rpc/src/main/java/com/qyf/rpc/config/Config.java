package com.qyf.rpc.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

public interface Config {
     Map<String, Class> loadConfig();

     void register(BeanDefinitionRegistry registry);
}
