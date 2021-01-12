package com.qyf.rpc.annotion;

import com.qyf.rpc.remoting.netty.invoke.RpcConfigConfigurationRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcConfigConfigurationRegistrar.class)
@Documented
@Inherited
/**
 * 开启rpc
 */
public @interface EnableRpc {
    String value();
}
