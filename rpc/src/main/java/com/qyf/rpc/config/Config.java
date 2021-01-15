package com.qyf.rpc.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

public interface Config {

     Map<String, Class> loadClientConfig();

     Map<String, Class> loadServerConfig();

     void register(BeanDefinitionRegistry registry, int type);
}
