package com.qyf.rpc.connection.http;

import com.qyf.rpc.connection.AbstractManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpManage extends AbstractManage{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void updateConnectServer(List<String> addressList) {

    }
}
