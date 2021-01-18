package com.qyf.rpc.connection.netty;

import com.qyf.rpc.connection.AbstractManage;
import com.qyf.rpc.remoting.Protocol;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;

public class NettyManage extends AbstractManage {

    @Autowired
    Protocol protocol;

    Logger logger = LoggerFactory.getLogger(this.getClass());




    @Override
    public synchronized void updateConnectServer(List<String> addressList){
        if (addressList.size()==0 || addressList==null){
            logger.error("没有可用的服务器节点, 全部服务节点已关闭!");
            for (final Channel channel : channels) {
                SocketAddress remotePeer = channel.remoteAddress();
                Channel handler_node = channelNodes.get(remotePeer);
                handler_node.close();
            }
            channels.clear();
            channelNodes.clear();
            return;
        }
        HashSet<SocketAddress> newAllServerNodeSet = new HashSet<>();
        for (int i = 0; i < addressList.size(); ++i) {
            String[] array = addressList.get(i).split(":");
            if (array.length == 2) {
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                final SocketAddress remotePeer = new InetSocketAddress(host, port);
                newAllServerNodeSet.add(remotePeer);
            }
        }

        for (final SocketAddress serverNodeAddress : newAllServerNodeSet) {
            Channel channel = channelNodes.get(serverNodeAddress);
            if (channel!=null && channel.isOpen()){
                logger.info("当前服务节点已存在,无需重新连接.{}",serverNodeAddress);
            }else{
                connectServerNode(serverNodeAddress);
            }
        }
        for (int i = 0; i < channels.size(); ++i) {
            Channel channel = channels.get(i);
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
    private void addChannel(Channel channel, SocketAddress address) {
        logger.info("加入Channel到连接管理器.{}",address);
        channels.add(channel);
        channelNodes.put(address, channel);
    }


}