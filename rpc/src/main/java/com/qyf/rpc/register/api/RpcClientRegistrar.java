package com.qyf.rpc.register.api;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.annotion.Reference;
import com.qyf.rpc.config.api.ChooseConfig;
import com.qyf.rpc.config.api.Config;
import com.qyf.rpc.eunm.RegisterType;
import com.qyf.rpc.eunm.RemotingType;
import com.qyf.rpc.eunm.Type;
import com.qyf.rpc.proxy.ClassPathRpcScanner;
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
            RemotingType type = (RemotingType) annoAttrs.get("type");
            RegisterType registerType = (RegisterType) annoAttrs.get("registerType");
            //加载所有配置
            ChooseConfig chooseConfig = new ChooseConfig();
            Config config = chooseConfig.getConfig(type);
            config.register(registry, Type.Client, registerType);

            //生成代理类
            ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);
            scanner.setAnnotationClass(Reference.class);
//            scanner.setAnnotationClass(null);
            scanner.registerFilters();
            scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        }
    }
}
