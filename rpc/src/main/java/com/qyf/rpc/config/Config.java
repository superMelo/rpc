package com.qyf.rpc.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

public interface Config {

     void loadClientConfig();

     void loadServerConfig();

     void register(BeanDefinitionRegistry registry, int type);
}
