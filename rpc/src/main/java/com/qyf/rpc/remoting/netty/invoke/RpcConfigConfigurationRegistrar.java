package com.qyf.rpc.remoting.netty.invoke;

import com.qyf.rpc.annotion.EnableRpc;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.server.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.imageio.spi.ServiceRegistry;

public class RpcConfigConfigurationRegistrar implements ImportBeanDefinitionRegistrar, BeanDefinitionRegistryPostProcessor {
    String basePackage;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //开启rpc，扫描接口生成代理类,并且建立netty客户端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRpc.class.getName()));
        if (annoAttrs != null){
            basePackage = (String)annoAttrs.get("value");
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NettyClient.class);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition("nettyClient", definition);
        }
        //通过zookeeper注册服务,建立netty服务端
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ServiceRegistry.class);
        GenericBeanDefinition serviceRegistry = (GenericBeanDefinition) builder.getRawBeanDefinition();
        BeanDefinitionBuilder builder1 = BeanDefinitionBuilder.genericBeanDefinition(NettyServer.class);
        GenericBeanDefinition nettyServer = (GenericBeanDefinition) builder1.getRawBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("serviceRegistry", serviceRegistry);
        beanDefinitionRegistry.registerBeanDefinition("nettyServer", nettyServer);
    }

    //扫描根据路径扫描接口，生成代理
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathRpcScanner scanner = new ClassPathRpcScanner(beanDefinitionRegistry);

        scanner.setAnnotationClass(null);
        scanner.registerFilters();

        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}