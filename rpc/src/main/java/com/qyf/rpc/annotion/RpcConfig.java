package com.qyf.rpc.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
/**
 * 动态配置使用注解
 */
public @interface RpcConfig {
}
