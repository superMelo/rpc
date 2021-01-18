package com.qyf.rpc.connection;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractManage implements Manage{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //netty
    protected AtomicInteger roundRobin = new AtomicInteger(0);
    protected CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
    protected Map<SocketAddress, Channel> channelNodes = new ConcurrentHashMap<>();

    //http
    protected Map<String, Integer> addressList = new ConcurrentHashMap<>();

    public Channel chooseChannel() {
        if (channels.size()>0) {
            int size = channels.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            return channels.get(index);
        }else{
            return null;
        }
    }


    public void removeChannel(Channel channel){
        logger.info("从连接管理器中移除失效Channel.{}",channel.remoteAddress());
        SocketAddress remotePeer = channel.remoteAddress();
        channelNodes.remove(remotePeer);
        channels.remove(channel);
    }
}
