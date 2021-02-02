package com.qyf.rpc.config.deploy;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ImportConfigRegistrar implements ImportBeanDefinitionRegistrar{

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder1 = BeanDefinitionBuilder.genericBeanDefinition(ConfigBeanPostProcessor.class);
        AbstractBeanDefinition definition1 = builder1.getBeanDefinition();
        BeanDefinitionBuilder builder2 = BeanDefinitionBuilder.genericBeanDefinition(ConfigListener.class);
        AbstractBeanDefinition definition2 = builder2.getBeanDefinition();
        BeanDefinitionBuilder builder3 = BeanDefinitionBuilder.genericBeanDefinition(LocalApp.class);
        AbstractBeanDefinition definition3 = builder3.getBeanDefinition();
        registry.registerBeanDefinition("configBeanPostProcessor", definition1);
        registry.registerBeanDefinition("configListener", definition2);
        registry.registerBeanDefinition("localApp", definition3);
    }
}
