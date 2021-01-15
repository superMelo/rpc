package com.qyf.rpc.remoting.netty.client;

import com.alibaba.fastjson.JSONArray;
import com.qyf.rpc.entity.Request;
import com.qyf.rpc.entity.Response;
import com.qyf.rpc.remoting.netty.codec.JSONDecoder;
import com.qyf.rpc.remoting.netty.codec.JSONEncoder;
import com.qyf.rpc.monitor.ConnectManage;
import com.qyf.rpc.remoting.netty.handle.NettyClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;

public class NettyClient {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final EventLoopGroup boss = new NioEventLoopGroup(1);

    private Bootstrap bootstrap = new Bootstrap();

    @Autowired
    private NettyClientHandle handle;

    @Autowired
    private ConnectManage manage;

    public NettyClient() {
        bootstrap.group(boss)
                .channel(NioSocketChannel.class) //设置channel
                .option(ChannelOption.SO_KEEPALIVE, true) //设置长连接
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) //使用池化分配
                .option(ChannelOption.TCP_NODELAY, true) //设置tcp
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 0));
                        pipeline.addLast(new JSONEncoder());//编码
                        pipeline.addLast(new JSONDecoder());//解码
                        pipeline.addLast("handle", handle);
                    }
                });
    }
    @PreDestroy
    public void destroy(){
        log.info("RPC客户端退出,释放资源!");
        boss.shutdownGracefully();
    }

    public Object send(Request request) throws InterruptedException {
        Channel channel = manage.chooseChannel();
        if (channel != null && channel.isActive()){
            SynchronousQueue<Object> queue = handle.sendRequest(request, channel);
            Object result = queue.take();
            return JSONArray.toJSON(result);
        }else {
            Response res = new Response();
            res.setCode(1);
            res.setError_msg("未正确连接到服务器.请检查相关配置信息!");
            return JSONArray.toJSON(res);
        }
    }

    public Channel doConnect(SocketAddress address) throws InterruptedException {
        ChannelFuture connect = bootstrap.connect(address);
        return connect.sync().channel();
    }
}
