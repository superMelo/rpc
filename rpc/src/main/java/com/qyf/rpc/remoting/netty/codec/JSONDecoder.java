package com.qyf.rpc.remoting.netty.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

//解码
public class JSONDecoder extends LengthFieldBasedFrameDecoder{
    public JSONDecoder() {
        super(65535, 0, 4,0,4);
    }
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode==null){
            return null;
        }
        //获取消息长度
        int data_len = decode.readableBytes();
        byte[] bytes = new byte[data_len];
        //读取内容
        decode.readBytes(bytes);
        //通过json返回
        Object parse = JSON.parse(bytes);
        return parse;
    }
}
