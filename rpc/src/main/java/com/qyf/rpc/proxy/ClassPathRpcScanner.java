package com.qyf.rpc.proxy;

import com.qyf.rpc.annotion.Reference;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

//扫描指定路径扫描注解接口,生成代理类
public class ClassPathRpcScanner extends ClassPathBeanDefinitionScanner {

//    private RpcFactoryBean<?> rpcFactoryBean = new RpcFactoryBean<>();

    private Class<? extends Annotation> annotationClass;
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public ClassPathRpcScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No RPC mapper was found in '"
                    + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        //将自定义注解添加到扫描任务中
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            addIncludeFilter(((metadataReader, metadataReaderFactory) -> {
                return true;
            }));
        }
        //将带有自定义注解的类 ，不加载到容器中
        addExcludeFilter((metadataReader, metadataReaderFactory)->{
            String className = metadataReader.getClassMetadata()
                    .getClassName();
            return className.endsWith("package-info");
        });
    }
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {

        GenericBeanDefinition definition;

        for (BeanDefinitionHolder holder : beanDefinitions) {

            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
//            try {
//                Class<?> aClass = Class.forName(beanClassName);
//                Reference annotation = aClass.getAnnotation(Reference.class);
//                //判断是否有加注解，使用代理生成实现类
//                if (annotation != null){
                    definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
                    definition.setBeanClass(RpcFactoryBean.class);

                    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    System.out.println(holder);
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }

        }
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
