package com.qyf.rpc.register.zookeeper;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.connection.ServiceDiscovery;
import com.qyf.rpc.remoting.netty.invoke.ClassPathRpcScanner;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar{


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //开启rpc，扫描接口生成代理类,并且建立netty客户端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRpcClient.class.getName()));
        if (annoAttrs != null){
            String basePackage = (String)annoAttrs.get("value");
            //加载netty客户端
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NettyClient.class);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition("nettyClient", definition);
            //加载服务发现
            BeanDefinitionBuilder builder1 = BeanDefinitionBuilder.genericBeanDefinition(ServiceDiscovery.class);
            GenericBeanDefinition serviceDiscovery = (GenericBeanDefinition) builder1.getRawBeanDefinition();
            registry.registerBeanDefinition("serviceDiscovery", serviceDiscovery);
            ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);

            scanner.setAnnotationClass(null);
            scanner.registerFilters();

            scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        }
    }
}
