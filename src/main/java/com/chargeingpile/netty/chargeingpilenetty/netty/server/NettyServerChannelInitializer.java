package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.ShConfig;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.decoder.MyDecoder;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.handle.HandleName;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.handle.ShHeartBeatHandler;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.handle.ShServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;


import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {


        //channel.pipeline().addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
        //channel.pipeline().addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
        channel.pipeline().addLast("decoder",new ByteArrayDecoder());
        channel.pipeline().addLast("encoder",new ByteArrayEncoder());



        //超时时间 60秒
        channel.pipeline().addLast(new IdleStateHandler(60,0,0));
//
        channel.pipeline().addLast(new MyDecoder());
        channel.pipeline().addLast(new NettyServerHandler()); // 心跳 签到
        channel.pipeline().addLast("handler",new NettySystemHandler()); //逻辑
        //channel.pipeline().addLast(HandleName.HANDLE_CHARGE,new NettyChargeHandler());
        channel.pipeline().addLast("charge",new NettyChargeHandler());





    }
}
