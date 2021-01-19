package com.qyf.rpc.remoting.http.client;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.connection.http.HttpConnectManage;
import com.qyf.rpc.entity.Request;
import com.qyf.rpc.remoting.AbstractProtocol;
import io.netty.channel.Channel;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

/**
 * http客户端
 */
public class HttpClient extends AbstractProtocol{

    @Autowired
    private HttpConnectManage connectManage;


    @Override
    public Channel doConnect(SocketAddress address) throws Exception {
        return null;
    }

    private CloseableHttpClient client = HttpClientBuilder.create().build();



    public Object send(Request request) throws Exception{
        List<String> urls = (List<String>) connectManage.select();
        String url = urls.get(0);
        String[] strs = url.split(":");
        String className = request.getClassName();
        String[] classNames = className.split("\\.");
        URI uri = new URIBuilder().setScheme("http")
                .setHost(strs[0]).setPort(Integer.parseInt(strs[1])).setPath(request.getMethodName())
                .setParameter("className", classNames[classNames.length - 1])
                .setParameter("methodName", request.getMethodName())
                .setParameter("parameters", JSON.toJSONString(request.getParameters()))
                .setParameter("parameterTypes", JSON.toJSONString(request.getParameterTypes())).build();
        HttpGet httpGet = new HttpGet(uri);
        System.out.println("===============" + httpGet.getURI());
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

        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity responseEntity = response.getEntity();
        String resp = EntityUtils.toString(responseEntity);
        return resp;
    }

//    public static void main(String[] args) throws Exception {
//        HttpClient httpClient = new HttpClient();
//        Class<?> clz = Class.forName("com.qyf.rpc.web.consumer.service.InfoUserService");
//        Method[] methods = clz.getMethods();
//        Request request = new Request();
//        for (Method method : methods) {
//            String name = method.getName();
//            if ("insertUser".equals(name)){
//                request.setId("1111");
//                request.setMethodName(name);
//                request.setClassName("InfoUserService");
//                request.setParameterTypes(method.getParameterTypes());
//                Map<String, Object> map = new HashMap<>();
//                map.put("id", "t1");
//                map.put("name", "test");
//                map.put("address", "123");
//                Object[] objects = new Object[1];
//                objects[0] = map;
//                request.setParameters(objects);
//                break;
//            }
//        }
//        Object send = httpClient.send(request);
//        System.out.println(send);
//    }
}
