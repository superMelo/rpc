package com.qyf.rpc.remoting.http.server;

import com.qyf.rpc.entity.Response;
import com.qyf.rpc.register.api.Register;
import com.qyf.rpc.remoting.api.AbstractServerProtocol;
import com.qyf.rpc.remoting.http.codec.HttpDecoder;
import com.qyf.rpc.remoting.http.codec.HttpEncoder;
import com.qyf.rpc.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * http服务端
 */
public class HttpServer extends AbstractServerProtocol implements HandlerInterceptor,InitializingBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${rpc.server.url}")
    private String url;

    @Autowired
    private Register register;




    @Override
    public void afterPropertiesSet() throws Exception {
        loadService(register, url);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpDecoder httpDecoder = new HttpDecoder();
        HttpEncoder httpEncoder = new HttpEncoder();
        Map<String, Object> map = (Map<String, Object>) httpDecoder.decode(request, serviceMap);
        if (map != null){
            Method method = (Method) map.get("method");
            Object[] objects  = (Object[])map.get("parameter");
            Object serviceBean = map.get("serviceBean");
            String id = (String)map.get("id");
            if (serviceBean != null){
                Object o = method.invoke(serviceBean, ClassUtils.getParameters(method.getParameterTypes(), objects));
                method.setAccessible(true);
                response.setCharacterEncoding("UTF-8");
                Response resp = new Response();
                resp.setCode(200);
                resp.setRequestId(id);
                resp.setData(o);
                httpEncoder.encode(response, resp);
            }
            return false;
        }else{
            return false;
        }
    }




}
