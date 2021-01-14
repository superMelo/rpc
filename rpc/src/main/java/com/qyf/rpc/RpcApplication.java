package com.qyf.rpc;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.annotion.EnableRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableRpcServer
@EnableRpcClient(value = "com.qyf.rpc.web.consumer.service")
@SpringBootApplication
public class RpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcApplication.class, args);
	}

}
