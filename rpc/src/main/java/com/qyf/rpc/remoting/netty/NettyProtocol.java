package com.qyf.rpc.remoting.netty;

import com.qyf.rpc.entity.Request;
import com.qyf.rpc.remoting.AbstractProtocol;
import com.qyf.rpc.remoting.netty.client.NettyClient;
import com.qyf.rpc.remoting.netty.server.NettyServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.net.SocketAddress;


public class NettyProtocol extends AbstractProtocol{

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NettyClient client;


    @Override
    public Object send(Request request) throws Exception {
        Object obj = client.send(request);
        return obj;
    }


    @Override
    public Channel doConnect(SocketAddress address) throws Exception {
        return client.doConnect(address);
    }
}
