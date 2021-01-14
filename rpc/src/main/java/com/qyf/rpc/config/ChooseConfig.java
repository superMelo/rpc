package com.qyf.rpc.config;


import com.qyf.rpc.config.netty.ClientConfig;
import com.qyf.rpc.config.netty.ServerConfig;

public class ChooseConfig {



    public Config getConfig(String type, int t){
        Config config = null;
        switch (type){
            case "netty":
                if (t>0){
                    config = new ServerConfig();
                }else {
                    config = new ClientConfig();
                }
                break;
        }
        return config;
    }
}
