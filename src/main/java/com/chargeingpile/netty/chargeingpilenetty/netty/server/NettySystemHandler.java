package com.chargeingpile.netty.chargeingpilenetty.netty.server;

import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.*;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.ApplicationContextUtils;
import com.chargeingpile.netty.chargeingpilenetty.util.EhcacheUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;


//业务逻辑处理
public class NettySystemHandler extends SimpleChannelInboundHandler<byte[]> {



    private ApplicationContext applicationContext= ApplicationContextUtils.getApplicationContext();

    ChargingMapper chargingMapper = applicationContext.getBean(ChargingMapper.class);



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        EhcacheUtil ehcache = EhcacheUtil.getInstance();



        //System.out.println("system-------------------------"+msg);

        if (!SHUtils.isShengHong(msg)){
            ctx.fireChannelRead(msg);
        }
        String cmd = BytesUtil.getMsgCmd(msg);
        String pileCode = SHUtils.getPileNum(msg);

        //System.out.println(pileCode+"-------pileCode--------");



        final ClientConnection client = ClientManager.getClientConnection(ctx,pileCode);


        if (cmd.equalsIgnoreCase("0200")) { //充电桩应答整形参数设置/查询报文 cmd=2
            if (msg[50] == 0) {

            } else {
                System.out.println("参数查询/设置--失败");
            }


        }else if (cmd.equalsIgnoreCase("6800")) { //充电桩上传状态信息包 cmd=104


            PileStateInfo info = PileStateInfo.getStateInfo(msg);

            chargingMapper.insertDatChaPilSta(info);


            int pileState = info.getWorkState();

            System.out.println("盛宏充电桩，状态信息上报-------------"+info.getZhuangId()+"---"+pileState);

            String gun = BytesUtil.byteToHexString(info.getGun());
            String cardStr = BytesUtil.bytesToHexString(info.getCardID());
            StateResponse sr = new StateResponse(gun, cardStr);

            byte[] srMsgByte =sr.getMsgByte(1);

            //  ctx.writeAndFlush(sr.getMsgByte(1));
            ctx.writeAndFlush(srMsgByte);
            //System.out.println("应答 103");

            // 桩 原来是 空闲状态 这次是 充电状态 就认为是开启了充电
            if (client != null){
                int lastState = client.getPileState();

                // 0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                if (lastState != 2 && pileState == 2){

                    String id = client.getUserID();

                    System.out.println("开启中 是在充电的状态。"+id+"---"+client.getPile_code()+client);
                }


                client.setPileState(pileState);

            }


            // 实时充电数据放入缓存
            StateInfo stateInfo = StateInfo.getIns(msg);

            //System.out.println("实时充电数据========"+stateInfo.toString());

            String pipleCode = stateInfo.getZhuangId();

            System.out.println("实时充电数据放入缓存,桩编号--------pipleCode---"+pipleCode);

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
            retMap.put("pipleCode",pipleCode);

            System.out.println("retMap========"+retMap);
            ehcache.put(pipleCode+"chaReaTim",retMap);



            // 告警状态
            Map<String, Object> alarmMap = new HashMap<String, Object>();


            //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)

            if (4 == stateInfo.getAlarm() || 6 == stateInfo.getAlarm()){
                // 插入告警
                //更新告警状态
                // 插入 实时数据信息

               // alarmMap.put("ala_typ_id", "1");
               // alarmMap.put("ala_sta", "1");
               // alarmMap.put("chp_id", stateInfo.getZhuangId());
               // alarmMap.put("chs_id", "9");
               // alarmMap.put("ala_lev", "1");
                //alarmMap.put("ala_dec", "发生了故障");

                //chargingMapper.insertAla(alarmMap); //添加告警信息

            }

            alarmMap.put("alarm", stateInfo.getAlarm());
            System.out.println("alaMap=========="+alarmMap);


            ehcache.put(pipleCode+"alarm",alarmMap);





        }else if (cmd.equalsIgnoreCase("CA00")) { //充电桩上传充电信息 cmd=202

            ChargeRecordInfo info = ChargeRecordInfo.getInfo(msg);
            pileCode = info.getPileCode();


            System.out.println("pipleCode = " + pileCode + " 上报充电信息"+"桩状态--"+client.getPileState());
            //System.out.println(info.toString());

            // 新增 充电记录信息 dat_cha_inf


            //响应
            String gun = BytesUtil.int2HexString(info.getGun());
            String card = BytesUtil.bytesToHexString(info.getCarId());
            CharInfoResponse res = new CharInfoResponse(gun, card);

            ctx.writeAndFlush(res.getMsgByte(2));

            //System.out.println("上传 cmd=201");


            String id = "";
            if (client != null) {
                id = client.getPile_code();
                // 停止充电时用到，充电信息
                client.setChargeRecordInfo(info);
                System.out.println("1.停止充电时用到，充电信息----"+client.getChargeRecordInfo());

                //新增充电记录
                chargingMapper.insertDatChaInf(client.getChargeRecordInfo());



                //新增记录

                ChargeRecordInfo chInfo = client.getChargeRecordInfo();

                Map map = new HashMap();

                map.put("useId",chInfo.getCardID());
                map.put("chsId",9); //电站
                map.put("chpId",chInfo.getPileCode());
                map.put("chaSta",2); // 充电状态 1为正在充电，2为充电已完成
                map.put("chaStaTim",chInfo.getStartTime());
                map.put("chaEndTim",chInfo.getEndTime());
                map.put("chaAmo",chInfo.getChargeMoney());
                map.put("chaEleQua",chInfo.getChargeEle());
                map.put("weOrApp",1); //微信为2，app为1
                map.put("staSoc",chInfo.getStartSOC());
                map.put("endSoc",chInfo.getEndSOC());
                //map.put("purEleAmo",); 购电金额
                //map.put("arrTim",); 进站时间
                //map.put("outTim",); 出站时间
               // map.put("payee",);  收款人
                //map.put("remark",); 备注
                //map.put("dcrInf",); 充电记录信息
                //map.put("openid",); 微信唯一标识
                //map.put("chaOrdNum",);充电订单号
                chargingMapper.insertDatChaRes(map);
            }

        }else if (cmd.equalsIgnoreCase("6C00")) {// 108

            Map<String, Object> alarmMap = new HashMap<String, Object>();

                //预留，先不处理
                AlarmInfo info = AlarmInfo.getIns(msg);
                pileCode = info.getPile_code();

                // TODO
                if (!CommonUtil.isEmpty(pileCode)) {

                    System.out.println("充电桩 上报  告警信息  : code====" + info.getPile_code()+"alarmType===="+info.getAlarmType());
                    //System.out.println("alarms==========="+info.getAlarms());


                    //新增告警

                    //AlarmInfo_dao.updateChpStaAlarm(pipleCode);

                    //alarmMap.put("ala_typ_id", "1");
                    //alarmMap.put("ala_sta", "1");
                    alarmMap.put("chp_id", info.getPile_code());
                    alarmMap.put("chs_id", "9");
                    alarmMap.put("ala_lev", info.getAlarmType());
                    alarmMap.put("ala_dec", info.getAlarms().toString()); ////告警位,32个字节。 每一位代码一个告警，共可表示 256 个告警，具体含义待定义 （ 为服务器能了解桩的告警信息）

                    chargingMapper.insertAla(alarmMap); //添加告警信息

                    ehcache.put(info.getPile_code()+"alarm",alarmMap);

                }

            }else {
            ctx.fireChannelRead(msg);
        }

        ReferenceCountUtil.safeRelease(msg);





    }
}
