package com.qyf.rpc.remoting.http.codec;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.remoting.api.Decoder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpDecoder implements Decoder{
    @Override
    public Object decode(Object... objs) {
        HttpServletRequest request = (HttpServletRequest) objs[0];
        Map<String, Object> serviceMap = (Map<String, Object>) objs[1];
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

}
