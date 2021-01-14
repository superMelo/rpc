package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.config.netty.ClientConfig;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.connection.ConnectManage;
import com.qyf.rpc.remoting.netty.connection.ServiceDiscovery;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;
import com.qyf.rpc.proxy.ClassPathRpcScanner;
import com.qyf.rpc.proxy.RpcFactory;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar{


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //开启rpc，扫描接口生成代理类,并且建立netty客户端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRpcClient.class.getName()));
        if (annoAttrs != null){
            String basePackage = (String)annoAttrs.get("value");
         /*   //加载netty客户端
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NettyClient.class);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition("nettyClient", definition);
            //加载服务发现
            BeanDefinitionBuilder serviceDiscoveryBuilder = BeanDefinitionBuilder.genericBeanDefinition(ServiceDiscovery.class);
            GenericBeanDefinition serviceDiscovery = (GenericBeanDefinition) serviceDiscoveryBuilder.getRawBeanDefinition();
            registry.registerBeanDefinition("serviceDiscovery", serviceDiscovery);

            //加载连接管理
            BeanDefinitionBuilder connectManageBuilder = BeanDefinitionBuilder.genericBeanDefinition(ConnectManage.class);
            GenericBeanDefinition connectManage = (GenericBeanDefinition) connectManageBuilder.getRawBeanDefinition();
            registry.registerBeanDefinition("connectManage", connectManage);

            //加载handle
            BeanDefinitionBuilder nettyClientHandleBuilder = BeanDefinitionBuilder.genericBeanDefinition(NettyClientHandle.class);
            GenericBeanDefinition nettyClientHandle = (GenericBeanDefinition) nettyClientHandleBuilder.getRawBeanDefinition();
            registry.registerBeanDefinition("nettyClientHandle", nettyClientHandle);

            //加载rpcFactory
            BeanDefinitionBuilder rpcFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcFactory.class);
            GenericBeanDefinition  rpcFactory = (GenericBeanDefinition) rpcFactoryBuilder.getRawBeanDefinition();
            registry.registerBeanDefinition("rpcFactory",  rpcFactory);*/
            //加载所有配置
            ClientConfig config = new ClientConfig();
            config.register(registry);
            //生成代理类
            ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);
            scanner.setAnnotationClass(null);
            scanner.registerFilters();
            scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        }
    }
}
