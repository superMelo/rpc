package com.qyf.rpc.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qyf.rpc.annotion.RpcService;
import com.qyf.rpc.web.entity.InfoUser;
import com.qyf.rpc.web.service.InfoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RpcService
public class InfoUserServiceImpl implements InfoUserService {


    Logger logger = LoggerFactory.getLogger(this.getClass());

    Map<String,InfoUser> infoUserMap = new ConcurrentHashMap<>();

    public List<InfoUser> insertInfoUser(InfoUser infoUser) {
        logger.info("新增用户信息:{}", JSONObject.toJSONString(infoUser));
        infoUserMap.put(infoUser.getId(),infoUser);
        return getInfoUserList();
    }

    public InfoUser getInfoUserById(String id) {
        InfoUser infoUser = infoUserMap.get(id);
        logger.info("查询用户ID:{}",id);
        return infoUser;
    }

    public List<InfoUser> getInfoUserList() {
        List<InfoUser> userList = new ArrayList<>();
        Iterator<Map.Entry<String, InfoUser>> iterator = infoUserMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, InfoUser> next = iterator.next();
            userList.add(next.getValue());
        }
        logger.info("返回用户信息记录:{}", JSON.toJSONString(userList));
        return userList;
    }

    public void deleteInfoUserById(String id) {
        logger.info("删除用户信息:{}", JSONObject.toJSONString(infoUserMap.remove(id)));
    }

    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        return infoUserMap.get(id).getName();
    }
    public Map<String, InfoUser> getAllUser(){
        logger.info("查询所有用户信息{}", JSONObject.toJSONString(infoUserMap));
        return infoUserMap;
    }

    @Override
    public String insertUser(Map<String, Object> map) {
        InfoUser infoUser = new InfoUser();
        infoUser.setId((String) map.get("id"));
        infoUser.setName((String) map.get("name"));
        infoUser.setAddress((String) map.get("address"));
        infoUserMap.put((String)map.get("id"),infoUser);
        return "success";
    }
}