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

    protected AtomicInteger roundRobin = new AtomicInteger(0);
    protected CopyOnWriteArrayList<Object> channels = new CopyOnWriteArrayList<>();
    protected Map<SocketAddress, Channel> channelNodes = new ConcurrentHashMap<>();



  

}
