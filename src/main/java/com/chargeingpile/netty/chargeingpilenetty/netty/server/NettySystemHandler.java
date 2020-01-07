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

import java.util.HashMap;
import java.util.Map;


//业务逻辑处理
public class NettySystemHandler extends SimpleChannelInboundHandler<byte[]> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        System.out.println("system-------------------------"+msg);

        if (!SHUtils.isShengHong(msg)){
            ctx.fireChannelRead(msg);
        }
        String cmd = BytesUtil.getMsgCmd(msg);
        String pileCode = SHUtils.getPileNum(msg);


        final ClientConnection client = ClientManager.getClientConnection(ctx,pileCode);


        if (cmd.equalsIgnoreCase("0200")) { //充电桩应答整形参数设置/查询报文 cmd=2
            if (msg[50] == 0) {

            } else {
                System.out.println("参数查询/设置--失败");
            }


        }else if (cmd.equalsIgnoreCase("6800")) { //充电桩上传状态信息包 cmd=104

            PileStateInfo info = PileStateInfo.getStateInfo(msg);

            //System.out.println("handleChargeState --> " + info.toString());

            int pileState = info.getWorkState();
            System.out.println("桩状态----"+pileState);

            String gun = BytesUtil.byteToHexString(info.getGun());
            String cardStr = BytesUtil.bytesToHexString(info.getCardID());
            StateResponse sr = new StateResponse(gun, cardStr);

            byte[] srMsgByte =sr.getMsgByte(1);

            //  ctx.writeAndFlush(sr.getMsgByte(1));
            ctx.writeAndFlush(srMsgByte);
            System.out.println("应答 103");

            // 桩 原来是 空闲状态 这次是 充电状态 就认为是开启了充电
            if (client != null){
                int lastState = client.getPileState();

                // 0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                if (lastState != 2 && pileState == 2){



                }


                client.setPileState(pileState);

            }


            // 实时充电数据放入缓存
            StateInfo stateInfo = StateInfo.getIns(msg);

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

        }else if (cmd.equalsIgnoreCase("CA00")) { //充电桩上传充电信息 cmd=202

            ChargeRecordInfo info = ChargeRecordInfo.getInfo(msg);
            pileCode = info.getPileCode();


            System.out.println("pipleCode = " + pileCode + " 上报充电记录");
            System.out.println(info.toString());


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

        }else if (cmd.equalsIgnoreCase("6C00")) {

                //预留，先不处理
                AlarmInfo info = AlarmInfo.getIns(msg);
                pileCode = info.getPile_code();

                // TODO
                if (!CommonUtil.isEmpty(pileCode)) {

                    System.out.println("充电桩 上报  告警信息  : code====" + info.getPile_code()+"alarmType===="+info.getAlarmType());
                    System.out.println("alarms==========="+info.getAlarms());
                    // Insert.insertAlarm(msg);

                    //AlarmInfo_dao.updateChpStaAlarm(pipleCode);
                }

            }else {
            ctx.fireChannelRead(msg);
        }

        ReferenceCountUtil.safeRelease(msg);





    }
}
