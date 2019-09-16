package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();





    /**
     * @param ctx
     * @author
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();

        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        System.out.println();
        //如果map中不包含此连接，就保存连接
        if (CHANNEL_MAP.containsKey(channelId)) {

            System.out.println("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());


        } else {
            //保存连接
            CHANNEL_MAP.put(channelId, ctx);

            System.out.println("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            System.out.println("连接通道数量: " + CHANNEL_MAP.size());

        }
    }





    /**
     * @param ctx
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();

        ChannelId channelId = ctx.channel().id();

        //包含此客户端才去删除
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);

            System.out.println("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            System.out.println("连接通道数量: " + CHANNEL_MAP.size());


        }
    }




    /**
     * @param ctx
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println("加载客户端报文......如下--------");
        System.out.println("【" + ctx.channel().id() + "】" + " :" + msg+"-----加载客户端报文结束");

        /**
         *  下面可以解析数据，保存数据，生成返回报文，将需要返回报文写入write函数
         *
         */

        //响应客户端
        this.channelWrite(ctx.channel().id(), msg);
    }




    /**
     * @param msg        需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @author xiongchuan on 2019/4/28 16:10
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, Object msg) throws Exception {

        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);

        if (ctx == null) {

            System.out.println("通道【" + channelId + "】不存在");
            return;
        }

        if (msg == null && msg == "") {
            System.out.println("服务端响应空的消息");
            return;
        }

        //将客户端的信息直接返回写入ctx
        ctx.write(msg);
        //刷新缓存区
        ctx.flush();
    }






    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {

                System.out.println("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();

            } else if (event.state() == IdleState.WRITER_IDLE) {

                System.out.println("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();

            } else if (event.state() == IdleState.ALL_IDLE) {

                System.out.println("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }




    /**
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();

        System.out.println(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + CHANNEL_MAP.size());



        }

}
