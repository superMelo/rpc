package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.remoting.netty.server.NettyServer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class RpcServerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //通过zookeeper注册服务,建立netty服务端
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ZookeeperServiceRegistry.class);
        GenericBeanDefinition serviceRegistry = (GenericBeanDefinition) builder.getRawBeanDefinition();
        BeanDefinitionBuilder builder1 = BeanDefinitionBuilder.genericBeanDefinition(NettyServer.class);
        GenericBeanDefinition nettyServer = (GenericBeanDefinition) builder1.getRawBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("zookeeperServiceRegistry", serviceRegistry);
        beanDefinitionRegistry.registerBeanDefinition("nettyServer", nettyServer);
    }

}