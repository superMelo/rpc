package com.qyf.rpc.connection;

import com.google.common.collect.Maps;
import com.qyf.rpc.entity.Request;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractConnectManage implements ConnectManage {

    protected AtomicInteger roundRobin = new AtomicInteger(0);
    protected CopyOnWriteArrayList<Object> channels = new CopyOnWriteArrayList<>();
    protected Map<SocketAddress, Channel> channelNodes = new ConcurrentHashMap<>();

    protected Map<String, CopyOnWriteArrayList<String>> addressList = Maps.newConcurrentMap();



}
