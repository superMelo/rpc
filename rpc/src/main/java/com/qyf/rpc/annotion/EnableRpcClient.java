package com.qyf.rpc.annotion;

import com.qyf.rpc.eunm.RegisterType;
import com.qyf.rpc.eunm.RemotingType;
import com.qyf.rpc.register.api.RpcClientRegistrar;
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
    //指向的service路径
    String value() default "";

    //远程调用类型
    RemotingType remotingType() default RemotingType.Netty;

    //服务注册类型
    RegisterType registerType() default RegisterType.Zk;
}
