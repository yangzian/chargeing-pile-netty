package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;







@Component
public class NettyServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;

    public int start(InetSocketAddress address) {

        //配置服务端的NIO线程组
         bossGroup = new NioEventLoopGroup(1);
         workerGroup = new NioEventLoopGroup(2000);

        try {
                 bootstrap = new ServerBootstrap()
                        .group(bossGroup, workerGroup)  // 绑定线程池
                        .channel(NioServerSocketChannel.class)
                        .localAddress(address)
                        .childHandler(new NettyServerChannelInitializer())//编码解码
                        .option(ChannelOption.SO_BACKLOG, 124)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                        .childOption(ChannelOption.SO_KEEPALIVE, true);  //保持长连接，2小时无数据激活心跳机制

                // 绑定端口，开始接收进来的连接
                ChannelFuture future = bootstrap.bind(address).sync();


                System.out.println("netty服务器开始监听端口：" + address.getPort());



                //关闭channel和块，直到它被关闭
                future.channel().closeFuture().sync();

                return 0;



        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            return 1;

        }
    }



    public int stop(){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        return 0;

    }

}
