package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.*;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


// 预约/充电
public class NettyChargeHandler extends SimpleChannelInboundHandler<byte[]> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        System.out.println("system-------------------------"+msg);

        if (!SHUtils.isShengHong(msg)){
            ctx.fireChannelRead(msg);
        }
        byte cmd = msg[6];

        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

        String ip = address.getAddress().getHostAddress();

        String pile = SHUtils.getPileNum(msg);


        final ClientConnection client = ClientManager.getClientConnection(ip,pile);






    }
}
