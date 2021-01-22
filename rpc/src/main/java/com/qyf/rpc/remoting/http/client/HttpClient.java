package com.qyf.rpc.remoting.http.client;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.connection.api.ConnectManage;
import com.qyf.rpc.entity.Request;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.net.URI;

/**
 * http客户端
 */
public class HttpClient{


    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConnectManage connectManage;


    private static CloseableHttpClient client = HttpClientBuilder.create().build();


    private HttpGet get(Request request) throws Exception{
        String url = (String) connectManage.select(request);
        String[] strings = url.split(":");
        String className = request.getClassName();
//        String[] classNames = className.split("\\.");
        URI uri = new URIBuilder().setScheme("http")
                .setHost(strings[0]).setPort(Integer.parseInt(strings[1])).setPath(request.getMethodName())
                .setParameter("className", className)
                .setParameter("methodName", request.getMethodName())
                .setParameter("parameters", JSON.toJSONString(request.getParameters()))
//                .setParameter("parameterTypes", JSON.toJSONString(request.getParameterTypes()))
                .build();
        HttpGet httpGet = new HttpGet(uri);
        log.info("http请求地址:{}", httpGet.getURI());
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(50000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(50000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(50000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    public Object send(Request request) throws Exception{
        try {
            HttpGet httpGet = get(request);
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            String resp = EntityUtils.toString(responseEntity);
            log.info("http响应:{}", resp);
            return resp;
        }catch (Exception e){
            throw new ConnectException("连接失败");
        }
    }

}
