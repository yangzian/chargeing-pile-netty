package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.*;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
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

        byte[] msg1 = (byte[]) msg;
        if (!SHUtils.isShengHong(msg1)){
            ctx.fireChannelRead(msg1);
        }
        String cmd = BytesUtil.getMsgCmd(msg1);
        String pileCode = SHUtils.getPileNum(msg1);


       // final ClientConnection client = ClientManager.getClientConnection(ctx, pileCode);


        System.out.println("cmd======="+cmd+"-------------pilecode===="+pileCode);

        if (cmd.equalsIgnoreCase("6a00")){ //充电桩签到 cmd=106
           // System.out.println("充电桩签到 cmd=106");

            SignResponse sr = new SignResponse();
            byte[] signResp = sr.getMsgByte(1);


            ctx.writeAndFlush(signResp);


            System.out.println("签到-----105");
            //响应客户端
            //this.channelWrite(ctx.channel().id(),signResp);

        }

        if (cmd.equalsIgnoreCase("6600")){ //充电桩上传心跳包 cmd=102

            //System.out.println("充电桩上传心跳包 cmd=102");
            HbResponse hs = new HbResponse(1, 2);
            byte[] hbSlave = hs.getMsgByte(1);
            ctx.writeAndFlush(hbSlave);
            System.out.println("心跳 cmd=101");
            //响应客户端
            //this.channelWrite(ctx.channel().id(),hbSlave);

        }

        if (cmd.equalsIgnoreCase("0200")) { //充电桩应答整形参数设置/查询报文 cmd=2
            if (msg1[50] == 0) {

            } else {
                System.out.println("参数查询/设置--失败");
            }

        if (cmd.equalsIgnoreCase("6800")) { //充电桩上传状态信息包 cmd=104

            PileStateInfo info = PileStateInfo.getStateInfo(msg1);

            //System.out.println("handleChargeState --> " + info.toString());

            int pileState = info.getWorkState();

            String gun = BytesUtil.byteToHexString(info.getGun());
            String cardStr = BytesUtil.bytesToHexString(info.getCardID());
            StateResponse sr = new StateResponse(gun, cardStr);

            byte[] srMsgByte =sr.getMsgByte(1);

            ctx.writeAndFlush(sr.getMsgByte(1));

            System.out.println("应答 103");

            //响应客户端
            this.channelWrite(ctx.channel().id(), srMsgByte);




            // 实时充电数据放入缓存
            StateInfo stateInfo = StateInfo.getIns(msg1);

            System.out.println("实时充电数据========"+stateInfo.toString());

            String pipleCode = stateInfo.getZhuangId();

            System.out.println("桩id"+pileCode);

            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("elec", stateInfo.getElecQua());
            retMap.put("time", stateInfo.getCharTim());
            retMap.put("DC_v", stateInfo.getDireV());
            retMap.put("DC_i", stateInfo.getDireI());
            retMap.put("av", stateInfo.getaV());
            retMap.put("ai", stateInfo.getaI());
            retMap.put("bv", stateInfo.getbV());
            retMap.put("bi", stateInfo.getbI());
            retMap.put("cv", stateInfo.getcV());
            retMap.put("ci", stateInfo.getcI());
            //剩余充电时间  秒
            retMap.put("rem_tim", stateInfo.getRemTim());
            retMap.put("soc", stateInfo.getSoc());

            System.out.println("retMap========"+retMap);

            // 告警状态
            Map<String, Object> alarmMap = new HashMap<String, Object>();
            alarmMap.put("alarm", stateInfo.getAlarm());
            System.out.println("alaMap=========="+alarmMap);
        }



        if (cmd.equalsIgnoreCase("CA00")) { //充电桩上传充电信息 cmd=202



            ChargeRecordInfo info = ChargeRecordInfo.getInfo(msg1);
            pileCode = info.getPileCode();

            //log.info( "pipleCode = "+ pipleCode +" 上报充电记录");
            //log.info( info.toString());
            System.out.println("pipleCode = " + pileCode + " 上报充电记录");
            System.out.println(info.toString());

            //chargeDao.insertChargeRecord(info, pipleCode);

            //响应
            String gun = BytesUtil.int2HexString(info.getGun());
            String card = BytesUtil.bytesToHexString(info.getCarId());
            CharInfoResponse res = new CharInfoResponse(gun, card);

            ctx.writeAndFlush(res.getMsgByte(2));

            System.out.println("上传 cmd=201");

            //TODO 是否需要
//                        Insert.insertCharInfo(msg);

            String id = "";
//            if (client != null) {
//                id = client.getPile_code();
//                // 停止充电时用到，充电信息
//                client.setChargeRecordInfo(info);
//            }

        }

            /*if (cmd.equalsIgnoreCase("6C00")) {


                //预留，先不处理
                AlarmInfo info = AlarmInfo.getIns(msg1);
                pileCode = info.getPile_code();
                System.out.println("充电桩 上报  告警信息  : pipleCode=" + pileCode);

                // TODO
                if (!CommonUtil.isEmpty(pileCode)) {

                    // Insert.insertAlarm(msg);

                    //AlarmInfo_dao.updateChpStaAlarm(pipleCode);
                }

            }
*/

        }


            //响应客户端
        //this.channelWrite(ctx.channel().id(), msg);
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
