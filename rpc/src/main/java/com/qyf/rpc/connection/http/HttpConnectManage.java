package com.qyf.rpc.connection.http;

import com.qyf.rpc.connection.AbstractConnectManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpConnectManage extends AbstractConnectManage {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void updateConnectServer(List<String> addressList) {
        channels.clear();
        addressList.stream().forEach(a-> channels.add(a));
    }

    @Override
    public Object select() {
        if (channels.size()>0) {
            int size = channels.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            return channels.get(index);
        }else{
            return null;
        }
    }

    @Override
    public void addChannel(Object... obj) {

    }

    @Override
    public void remove(Object obj) {

    }
}
