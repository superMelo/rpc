package com.qyf.rpc.remoting.http.codec;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.entity.Response;
import com.qyf.rpc.remoting.api.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpEncoder implements Encoder{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Object encode(Object... objects) {
        HttpServletResponse response = (HttpServletResponse)objects[0];
        Response resp = (Response) objects[1];
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
            writer.write(JSON.toJSONString(resp));
            writer.flush();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }finally {
            if(writer!=null) {
                writer.close();
            }
        }
        return null;
    }


}
