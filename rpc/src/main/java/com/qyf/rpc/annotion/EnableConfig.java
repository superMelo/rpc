package com.qyf.rpc.annotion;

import com.qyf.rpc.config.deploy.ImportConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportConfigRegistrar.class)
@Documented
@Inherited
public @interface EnableConfig {
}
