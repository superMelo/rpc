package com.qyf.rpc.register.api;

import com.qyf.rpc.annotion.EnableRpcServer;
import com.qyf.rpc.config.ChooseConfig;
import com.qyf.rpc.config.Config;
import com.qyf.rpc.eunm.RemotingType;
import com.qyf.rpc.eunm.Type;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class RpcServerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //通过zookeeper注册服务,建立netty服务端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRpcServer.class.getName()));
        if (annoAttrs != null){
            RemotingType remotingType = (RemotingType)annoAttrs.get("type");
            //加载所有配置
            ChooseConfig chooseConfig = new ChooseConfig();
            Config config = chooseConfig.getConfig(remotingType);
            config.register(registry, Type.Server);
        }
    }

}