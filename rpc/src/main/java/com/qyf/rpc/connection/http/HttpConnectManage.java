package com.qyf.rpc.connection.http;

import com.qyf.rpc.connection.AbstractConnectManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpConnectManage extends AbstractConnectManage {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void updateConnectServer(List<String> addressList) {
        addressList.stream().forEach(a->{
            urls.add(a);
        });
    }

    @Override
    public Object select() {
        if (urls.size()>0) {
            int size = urls.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            return urls.get(index);
        }else{
            return null;
        }
    }

    @Override
    public void remove(Object obj) {
        
    }
}
