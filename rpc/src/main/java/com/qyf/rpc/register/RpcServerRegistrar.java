package com.qyf.rpc.register;

import com.qyf.rpc.annotion.EnableRpcServer;
import com.qyf.rpc.config.ChooseConfig;
import com.qyf.rpc.config.Config;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class RpcServerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final int TYPE = 1;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //通过zookeeper注册服务,建立netty服务端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRpcServer.class.getName()));
        if (annoAttrs != null){
            String type = (String)annoAttrs.get("type");
            //加载所有配置
            ChooseConfig chooseConfig = new ChooseConfig();
            Config config = chooseConfig.getConfig(type, TYPE);
            config.register(registry, TYPE);
        }
    }

}