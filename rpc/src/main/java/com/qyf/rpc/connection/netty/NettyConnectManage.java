package com.qyf.rpc.connection.netty;

import com.google.common.collect.Sets;
import com.qyf.rpc.connection.api.AbstractConnectManage;
import com.qyf.rpc.entity.Request;
import com.qyf.rpc.remoting.api.Protocol;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class NettyConnectManage extends AbstractConnectManage {




    @Autowired
    Protocol protocol;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object select(Request request) {
        String className = request.getClassName();
        List<String> list = addressList.get(className);
        if (list.size()>0) {
            int size = list.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            String url = list.get(index);
            String[] arr = url.split(":");
            final SocketAddress remotePeer = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
            Channel channel = channelNodes.get(remotePeer);
            return channel;
        }else{
            return null;
        }

    }

    @Override
    public synchronized void updateConnectServer(Map<String, CopyOnWriteArrayList<String>> addressList) {
        this.addressList = addressList;
        //判断是否存在服务
        if (addressList.size()==0 || addressList==null){
            logger.error("没有可用的服务器节点, 全部服务节点已关闭!");
            for (final Object channel : channels) {
                Channel c = (Channel) channel;
                SocketAddress remotePeer = c.remoteAddress();
                Channel handler_node = channelNodes.get(remotePeer);
                handler_node.close();
            }
            channels.clear();
            channelNodes.clear();
            return;
        }

        //获取所有服务地址
        HashSet<SocketAddress> newAllServerNodeSet = Sets.newHashSet();
        addressList.forEach((k, v) -> {
            v.stream().forEach(node->{
                String[] array = node.split(":");
                if (array.length == 2) {
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    final SocketAddress remotePeer = new InetSocketAddress(host, port);
                    newAllServerNodeSet.add(remotePeer);
                }
            });
        });

        for (final SocketAddress serverNodeAddress : newAllServerNodeSet) {
            Channel channel = channelNodes.get(serverNodeAddress);
            if (channel != null && channel.isOpen()) {
                logger.info("当前服务节点已存在,无需重新连接.{}", serverNodeAddress);
            } else {
                connectServerNode(serverNodeAddress);
            }
        }

        for (int i = 0; i < channels.size(); ++i) {
            Channel channel = (Channel) channels.get(i);
            SocketAddress remotePeer = channel.remoteAddress();
            if (!newAllServerNodeSet.contains(remotePeer)) {
                logger.info("删除失效服务节点 " + remotePeer);
                Channel channel_node = channelNodes.get(remotePeer);
                if (channel_node != null) {
                    channel_node.close();
                }
                channels.remove(channel);
                channelNodes.remove(remotePeer);
            }
        }


//        HashSet<SocketAddress> newAllServerNodeSet = new HashSet<>();
//        for (int i = 0; i < addressList.size(); ++i) {
//            String[] array = addressList.get(i).split(":");
//            if (array.length == 2) {
//                String host = array[0];
//                int port = Integer.parseInt(array[1]);
//                final SocketAddress remotePeer = new InetSocketAddress(host, port);
//                newAllServerNodeSet.add(remotePeer);
//            }
//        }
//
//        for (final SocketAddress serverNodeAddress : newAllServerNodeSet) {
//            Channel channel = channelNodes.get(serverNodeAddress);
//            if (channel!=null && channel.isOpen()){
//                logger.info("当前服务节点已存在,无需重新连接.{}",serverNodeAddress);
//            }else{
//                connectServerNode(serverNodeAddress);
//            }
//        }
//        for (int i = 0; i < channels.size(); ++i) {
//            Channel channel = (Channel) channels.get(i);
//            SocketAddress remotePeer = channel.remoteAddress();
//            if (!newAllServerNodeSet.contains(remotePeer)) {
//                logger.info("删除失效服务节点 " + remotePeer);
//                Channel channel_node = channelNodes.get(remotePeer);
//                if (channel_node != null) {
//                    channel_node.close();
//                }
//                channels.remove(channel);
//                channelNodes.remove(remotePeer);
//            }
//        }

    }


    private void connectServerNode(SocketAddress address){
        try {
            Channel channel = protocol.doConnect(address);
            addChannel(channel,address);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("未能成功连接到服务器:{}",address);
        }
    }

    @Override
    public void addChannel(Object... obj) {
        Channel channel = (Channel) obj[0];
        SocketAddress address = (SocketAddress) obj[1];
        logger.info("加入Channel到连接管理器.{}", address);
        channels.add(channel);
        channelNodes.put(address, channel);
    }

    @Override
    public void remove(Object obj) {
        Channel channel = (Channel) obj;
        logger.info("从连接管理器中移除失效Channel.{}",channel.remoteAddress());
        SocketAddress remotePeer = channel.remoteAddress();
        channelNodes.remove(remotePeer);
        channels.remove(channel);
    }
}
