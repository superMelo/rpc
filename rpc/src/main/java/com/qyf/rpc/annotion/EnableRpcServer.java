package com.qyf.rpc.annotion;

import com.qyf.rpc.eunm.RemotingType;
import com.qyf.rpc.register.api.RpcServerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServerRegistrar.class)
@Documented
@Inherited
/**
 * 开启rpc服务端
 */
public @interface EnableRpcServer {

    RemotingType type() default RemotingType.Netty;

}
