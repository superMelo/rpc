package com.qyf.rpc.remoting.netty.handle;

import com.alibaba.fastjson.JSON;
import com.qyf.rpc.entity.Request;
import com.qyf.rpc.entity.Response;
import com.qyf.rpc.utils.ClassUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

@ChannelHandler.Sharable
public class NettyServerHandle extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NettyServerHandle.class);
    private final Map<String, Object> serviceMap;

    public NettyServerHandle(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        logger.info("客户端连接成功!"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        logger.info("客户端断开连接!{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)   {
        Request request = JSON.parseObject(msg.toString(),Request.class);

        if ("heartBeat".equals(request.getMethodName())) {
            logger.info("客户端心跳信息..."+ctx.channel().remoteAddress());
        }else{
            logger.info("RPC客户端请求接口:"+request.getClassName()+"   方法名:"+request.getMethodName());
            Response response = new Response();
            response.setRequestId(request.getId());
            try {
                Object result = this.handler(request);
                response.setData(result);
            } catch (Throwable e) {
                e.printStackTrace();
                response.setCode(1);
                response.setError_msg(e.toString());
                logger.error("RPC Server handle request error",e);
            }
            ctx.writeAndFlush(response);
        }
    }

    /**
     * 通过反射，执行本地方法
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handler(Request request) throws Throwable{
        String className = request.getClassName();
//        String[] split = className.split("\\.");
//        className = split[split.length - 1];
        Object serviceBean = serviceMap.get(className);

        if (serviceBean!=null){
            logger.info("request对象: " + JSON.toJSONString(request));
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
//            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();
//            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Method method = ClassUtils.getMethod(serviceClass, methodName);
            method.setAccessible(true);
            return method.invoke(serviceBean, ClassUtils.getParameters(method.getParameterTypes(), parameters));
        }else{
            throw new Exception("未找到服务接口,请检查配置!:"+className+"#"+request.getMethodName());
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.ALL_IDLE){
                logger.info("客户端已超过60秒未读写数据,关闭连接.{}",ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)   {
        logger.info("RPC通信服务器发生异常.{}",cause);
        ctx.close();
    }
}
