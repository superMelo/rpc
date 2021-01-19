package com.qyf.rpc.remoting.http;

import com.qyf.rpc.entity.Request;
import com.qyf.rpc.remoting.api.AbstractProtocol;
import com.qyf.rpc.remoting.http.client.HttpClient;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;

public class HttpProtocol extends AbstractProtocol{

    @Autowired
    private HttpClient httpClient;


    @Override
    public Object send(Request request) throws Exception {
        Object object = httpClient.send(request);
        return object;
    }

    @Override
    public Channel doConnect(SocketAddress address) throws Exception {
        return null;
    }
}
