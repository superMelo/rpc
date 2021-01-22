package com.qyf.rpc.connection.http;

import com.qyf.rpc.connection.AbstractConnectManage;
import com.qyf.rpc.entity.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class HttpConnectManage extends AbstractConnectManage {
    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public Object select(Request request) {
        log.info("发送请求:{}", request);
        String className = request.getClassName();
        List<String> list = addressList.get(className);
        if (list.size()>0) {
            int size = list.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            return list.get(index);
        }else{
            return null;
        }
    }

    @Override
    public void updateConnectServer(Map<String, CopyOnWriteArrayList<String>> addressList) {
        this.addressList = addressList;
    }

    @Override
    public void addChannel(Object... obj) {

    }

    @Override
    public void remove(Object obj) {

    }
}
