package com.qyf.rpc.config;


import com.qyf.rpc.config.netty.NettyConfig;

public class ChooseConfig {



    public Config getConfig(String type, int t){
        Config config = null;
        switch (type){
            case "netty":
                config = new NettyConfig();
                break;
        }
        return config;
    }
}
