package com.qyf.rpc.connection;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractConnectManage implements ConnectManage {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //netty
    protected AtomicInteger roundRobin = new AtomicInteger(0);
    protected CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
    protected Map<SocketAddress, Channel> channelNodes = new ConcurrentHashMap<>();

    //http
    protected  CopyOnWriteArrayList<String> urls = new CopyOnWriteArrayList<>();



  

}
