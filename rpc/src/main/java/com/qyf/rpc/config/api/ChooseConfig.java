package com.qyf.rpc.config.api;


import com.qyf.rpc.config.http.HttpConfig;
import com.qyf.rpc.config.netty.NettyConfig;
import com.qyf.rpc.eunm.RemotingType;

public class ChooseConfig {



    public Config getConfig(RemotingType remotingType){
        Config config = null;
        switch (remotingType){
            case Netty:
                config = new NettyConfig();
                break;
            case Http:
                config = new HttpConfig();
                break;
        }
        return config;
    }
}
