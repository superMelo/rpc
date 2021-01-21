package com.qyf.rpc.connection.http;

import com.qyf.rpc.connection.AbstractConnectManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class HttpConnectManage extends AbstractConnectManage {
    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public void updateConnectServer(Map<String, CopyOnWriteArrayList<String>> addressList) {
//        channels.clear();
//        addressList.stream().forEach(a-> channels.add(a));
        this.addressList = addressList;
    }

    @Override
    public void addChannel(Object... obj) {

    }

    @Override
    public void remove(Object obj) {

    }
}
