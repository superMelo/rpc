package com.qyf.rpc.annotion;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 配置对象使用注解
 */
public @interface ConfigBean {
}
