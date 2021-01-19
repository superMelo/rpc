package com.qyf.rpc.register;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.config.ChooseConfig;
import com.qyf.rpc.config.Config;
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


    private static final int TYPE = 0;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //开启rpc，扫描接口生成代理类,并且建立netty客户端
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRpcClient.class.getName()));
        if (annoAttrs != null){
            String basePackage = (String)annoAttrs.get("value");
            RemotingType type = (RemotingType) annoAttrs.get("type");
            //加载所有配置
            ChooseConfig chooseConfig = new ChooseConfig();
            Config config = chooseConfig.getConfig(type);
            config.register(registry, Type.Client);

            //生成代理类
            ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);
            scanner.setAnnotationClass(null);
            scanner.registerFilters();
            scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        }
    }
}
