package com.chargeingpile.netty.chargeingpilenetty.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    /**
     * 计算有多少客户端接入，第一个string为客户端ip
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CLIENT_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        CLIENT_MAP.put(ctx.channel().id(), ctx);



        System.out.println("ClientHandler Active --------------------------");


    }

    /**
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @DESCRIPTION: 有服务端端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        ctx.close();
        System.out.println("服务端终止了服务 --------------");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println("回写数据:----------------" + msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        //cause.printStackTrace();
        System.out.println("服务端发生异常【" + cause.getMessage() + "】");
        ctx.close();
    }

    /**
     * @param msg       需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @DESCRIPTION: 客户端给服务端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, String msg) {

        ChannelHandlerContext ctx = CLIENT_MAP.get(channelId);

        if (ctx == null) {
            System.out.println("通道【" + channelId + "】不存在");
            return;
        }

        //将客户端的信息直接返回写入ctx
        //ctx.write(msg + " 时间：" + DateUtils.getDateTime());
        ctx.write(msg + " 写入的时间是：" + new Date().getTime());

        //刷新缓存区
        ctx.flush();
    }

}
