package com.qyf.rpc.remoting.http.server;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.entity.Response;
import com.qyf.rpc.register.Register;
import com.qyf.rpc.remoting.AbstractServerProtocol;
import com.qyf.rpc.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * http服务端
 */
public class HttpServer extends AbstractServerProtocol implements HandlerInterceptor,InitializingBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${http.server.host}")
    private String url;

    @Autowired
    private Register register;




    @Override
    public void afterPropertiesSet() throws Exception {
        //注册服务
        register.doRegister(url);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> map = decode(request);
        if (map != null){
            Method method = (Method) map.get("method");
            Object[] objects  = (Object[])map.get("parameter");
            Object serviceBean = map.get("serviceBean");
            if (serviceBean != null){
                Object o = method.invoke(serviceBean, ClassUtils.getParameters( method.getParameterTypes(), objects));
                method.setAccessible(true);
                response.setCharacterEncoding("UTF-8");
                Response resp = new Response();
                resp.setCode(200);
                resp.setRequestId("1");
                resp.setData(o);
                responseResult(response, resp);
            }
            return false;
        }else{
            return false;
        }
    }


    private Map<String, Object> decode(HttpServletRequest request) throws ClassNotFoundException {
        String className = request.getParameter("className");
        String methodName = request.getParameter("methodName");
        String parameters = request.getParameter("parameters");
        Object[] objects = JSON.parseObject(parameters, Object[].class);
        Object serviceBean = serviceMap.get(className);
        if (serviceBean != null){
            Class<?> clz = serviceBean.getClass();
            Method[] methods = clz.getMethods();
            Method method = null;
            for (Method m : methods) {
                if (methodName.equals(m.getName())){
                    method = m;
                    break;
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("className", className);
            map.put("methodName", methodName);
            map.put("method", method);
            map.put("parameter", objects);
            map.put("serviceBean", serviceBean);
            return map;
        }
        return null;
    }

    private void responseResult(HttpServletResponse response, Response resp) {
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
    }

}
