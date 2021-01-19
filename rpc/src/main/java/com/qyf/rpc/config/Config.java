package com.qyf.rpc.config;

import com.qyf.rpc.eunm.Type;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

public interface Config {

     void loadClientConfig();

     void loadServerConfig();

     void register(BeanDefinitionRegistry registry, Type type);
}
