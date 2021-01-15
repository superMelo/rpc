package com.qyf.rpc.remoting;

import com.qyf.rpc.entity.Request;
import io.netty.channel.Channel;

import java.net.SocketAddress;

public interface Protocol {

    //发送请求
    Object send(Request request) throws Exception;

    //服务注册
//    void export();

    Channel doConnect(SocketAddress address) throws Exception;


}
