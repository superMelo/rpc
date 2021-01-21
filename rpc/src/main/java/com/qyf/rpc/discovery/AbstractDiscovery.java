package com.qyf.rpc.discovery;

import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDiscovery implements Discovery, InitializingBean{

    // 服务地址列表
    protected volatile List<String> addressList = new ArrayList<>();


    protected static final String REGISTRY_PATH_KEY = "registry_path_key";

}
