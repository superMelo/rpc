package com.qyf.rpc.web.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.qyf.rpc.web.provider.entity.InfoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        InfoUser user = new InfoUser(UUID.randomUUID().toString(),"王思萌","BeiJing");
        String json = JSONObject.toJSONString(user);
        logger.info(json);
        return json;
    }
}
