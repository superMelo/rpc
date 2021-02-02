package com.qyf.rpc.config.deploy;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class LocalApp implements ApplicationContextAware{


    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //注销bean
    public void destroyBean(String beanName){
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        beanDefReg.removeBeanDefinition(beanName);
    }

    //创建bean
    public void addBean(String beanName, Class beanClass, Object object, Method method){
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        if (!beanDefReg.containsBeanDefinition(beanName)){
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, beanClass);
            constructorArgumentValues.addIndexedArgumentValue(1, object);
            constructorArgumentValues.addIndexedArgumentValue(2, method);
            beanDefinition.setBeanClass(ConfigFactoryBean.class);
            beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefReg.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}
