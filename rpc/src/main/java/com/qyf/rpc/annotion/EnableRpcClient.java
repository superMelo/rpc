package com.qyf.rpc.annotion;

import com.qyf.rpc.eunm.RemotingType;
import com.qyf.rpc.register.RpcClientRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcClientRegistrar.class)
@Documented
@Inherited
/**
 * 开启rpc客户端
 */
public @interface EnableRpcClient {
    String value();

    RemotingType type() default RemotingType.Netty;
}
