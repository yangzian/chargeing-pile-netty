package com.chargeingpile.netty.chargeingpilenetty.shenghong;

import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

    /**
     * 盛宏通信服务端
     * @author cj
     *
     */
public class SHServer{

        ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

        private int port;
        private ServerBootstrap bootstrap;
        private EventLoopGroup bossGroup;
        private EventLoopGroup workGroup;

        public SHServer(int port) {
            this.port = port;
        }


        /**
         * 盛弘开始服务
         */
        public void start() {
            bossGroup = new NioEventLoopGroup(1);
            workGroup = new NioEventLoopGroup(2000);

            if (bootstrap == null) {
                bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 100).childOption(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {


                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();
                        // 字符串解码 和 编码
                        //cp.addLast("decoder", new ByteArrayDecoder());
                        //cp.addLast("encoder", new ByteArrayEncoder());

                        cp.addLast(new IdleStateHandler(ShConfig.READ_TIME_OUT, 0, 0));

                        cp.addLast(new NettyServerChannelInitializer());

                        // 自己的逻辑Handler
                        //cp.addLast("handler", new ShServerHandler());
                        //cp.addLast(HandleName.HANDLE_CHARGE, new ShChargeHandler());

                    }
                });
            }

            // 服务器绑定端口监听
            ChannelFuture f = bootstrap.bind(port).addListener(new ChannelFutureListener() {


                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println(" server start on --> " + future.channel().localAddress());
                    } else {
                        System.out.println(" server start failed --> ");
                        bossGroup.shutdownGracefully();
                        workGroup.shutdownGracefully();
                    }
                }
            });

            f.channel().closeFuture().addListener(new ChannelFutureListener() {


                public void operationComplete(ChannelFuture future) throws Exception {
                    //log.info(" server close  --> ");
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            });

        }


}
