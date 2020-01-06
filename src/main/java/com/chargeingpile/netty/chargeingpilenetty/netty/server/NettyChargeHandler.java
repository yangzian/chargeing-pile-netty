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


    private int flag;



    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }




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



        if (cmd == 0x08) {
            int res = BytesUtil.toInt4(msg, 45);

            if (res == 0) {
                if (client != null) {
                    if (flag == 0) { // 预约

                        System.out.println("开启预约  -- 成功");

                        //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)

                        client.setPileState(ClientConnection.STATE_ORDER); // 5
                    } else {

                        System.out.println("开启充电 -- 成功");

                        client.setPileState(ClientConnection.STATE_CHARGE);// 2

//						String id = client.getUserID();
//						com.chargepile.service.collection.suowei.sql.Insert.insertChargeStart(id, pile,
//								new Date(System.currentTimeMillis()));
                    }
                }

            } else {
                if (client != null) {

                    client.setPileState(ClientConnection.STATE_START_FAILED); //4
                }

                System.out.println("开启预约/即时充电--失败");
            }
        } else if (cmd == 0x06) {

            if (msg[50] == 0) {
                if (client != null) {
                    client.setPileState(ClientConnection.STATE_NORMAL); //0

                    System.out.println("取消预约/停止充电--成功");
                }
            } else {
                if (client != null) {

                    client.setPileState(ClientConnection.STATE_START_FAILED);//4
                }
                System.out.println("取消预约/停止充电--失败");
            }
        }






    }
}
