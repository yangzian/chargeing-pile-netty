package com.chargeingpile.netty.chargeingpilenetty.netty.client;

import com.chargeingpile.netty.chargeingpilenetty.constans.DefaultConstans;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;


public class NettyClient implements Runnable{



    private String content;

    public NettyClient(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            int num = 0;
            boolean boo =true;

            Bootstrap b = new Bootstrap();
            b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new NettyClientChannelInitializer() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast("decoder", new StringDecoder());
                                p.addLast("encoder", new StringEncoder());
                                p.addLast(new NettyClientHandler());
                            }
                        });

            //ChannelFuture future = b.connect(HOST, PORT).sync();
            ChannelFuture future = b.connect("192.168.32.25", 9999).sync();

            //future.channel().writeAndFlush(content + "--" + new Date().getTime()+"-----------这是我发送的数据");


            while (boo) {

                num++;

                //future.channel().writeAndFlush(content + "--" + DateUtils.getgetDateTime());
                //future.channel().writeAndFlush(content + "--" + new Date().getTime());
                future.channel().writeAndFlush(content + "--" + new Date().getTime()+"-----------这是我发送的数据");

                try { //休眠一段时间
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //每一条线程向服务端发送的次数
                if (num == 10) {
                    boo = false;
                }
            }

            System.out.println(content + "-----------------------------" + num);
            //future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }



}
