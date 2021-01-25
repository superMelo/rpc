package com.qyf.rpc.web.service;

import com.qyf.rpc.annotion.Reference;
import com.qyf.rpc.web.entity.InfoUser;

import java.util.List;
import java.util.Map;

@Reference
public interface InfoUserService {

    List<InfoUser> insertInfoUser(InfoUser infoUser);

    InfoUser getInfoUserById(String id);

    void deleteInfoUserById(String id);

    String getNameById(String id);

    Map<String,InfoUser> getAllUser();

    String insertUser(Map<String, Object> map) throws Exception;

}