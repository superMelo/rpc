package com.qyf.rpc;

import com.qyf.rpc.annotion.EnableRpcClient;
import com.qyf.rpc.annotion.EnableRpcServer;
import com.qyf.rpc.eunm.RemotingType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRpcServer(remotingType = RemotingType.Http)
//@EnableRpcClient(value = "com.qyf.rpc.web.consumer.service", type = RemotingType.Http)
@SpringBootApplication
public class RpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcApplication.class, args);
	}

}
