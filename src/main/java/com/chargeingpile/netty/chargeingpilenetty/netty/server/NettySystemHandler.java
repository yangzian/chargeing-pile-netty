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
import com.chargeingpile.netty.chargeingpilenetty.util.PileStaUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//业务逻辑处理
public class NettySystemHandler extends SimpleChannelInboundHandler<byte[]> {



    private ApplicationContext applicationContext= ApplicationContextUtils.getApplicationContext();

    ChargingMapper chargingMapper = applicationContext.getBean(ChargingMapper.class);



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        EhcacheUtil ehcache = EhcacheUtil.getInstance();

        if (!SHUtils.isShengHong(msg)){
            ctx.fireChannelRead(msg);
        }
        String cmd = BytesUtil.getMsgCmd(msg);
        String pileCode = SHUtils.getPileNum(msg);

        final ClientConnection client = ClientManager.getClientConnection(ctx,pileCode);


        if (cmd.equalsIgnoreCase("0200")) { //充电桩应答整形参数设置/查询报文 cmd=2
            if (msg[50] == 0) {

            } else {
               // System.out.println("参数查询/设置--失败");
            }


        }else if (cmd.equalsIgnoreCase("6800")) { //充电桩上传状态信息包 cmd=104


            PileStateInfo info = PileStateInfo.getStateInfo(msg);

            String carIdStr = BytesUtil.bytesToHexString(info.getCardID());

            info.setCarIdStr(carIdStr);

            //info.setChargeDurationStr(BytesUtil.bytesToHexString(info.getChargeDuration()));
            info.setChargeDurationInt(BytesUtil.toInt4(info.getChargeDuration()));

            System.out.println("实时信息上报，充电时长===="+info.getChargeDurationInt());


            info.setStaTime(BytesUtil.bytesToHexString(info.getStartTime()));

            System.out.println("实时信息上报，开始时间===="+info.getStaTime());


            System.out.println("info========"+info.toString());;

            chargingMapper.insertDatChaPilSta(info);   // 实时数据上传

            System.out.println("状态信息上报1---104----"+info);

            int pileState = info.getWorkState();


            int sta = PileStaUtil.getPileSta(pileState); // 桩和 数据库中的状态不一致 转换

            System.out.println("状态信息上报2---------insert pile:----"+SHUtils.getPileNum(msg)+"----sta:--"+pileState+"===="+sta);

            //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
            // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警',

            //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
            // 修改设备的状态
            //chargingMapper.updChaPilSta(null,null, SHUtils.getPileNum(msg),null,String.valueOf(sta),"0");
            chargingMapper.updChaPilStaNew(SHUtils.getPileNum(msg),String.valueOf(sta));



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

                 //   System.out.println("开启中 是在充电的状态。"+id+"---"+client.getPile_code()+client);

                    //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                    // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警',
                    //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
                    // 修改设备的状态
                    //chargingMapper.updChaPilSta(null,null, SHUtils.getPileNum(msg),null,"1","0");
                }

                client.setPileState(pileState);

            }

            // 实时充电数据放入缓存
            StateInfo stateInfo = StateInfo.getIns(msg);

            String pipleCode = stateInfo.getZhuangId();

            //System.out.println("实时充电数据放入缓存,桩编号--------pipleCode---"+pipleCode);

            Map<String, Object> retMap = new HashMap<String, Object>();

            DecimalFormat df = new DecimalFormat("#.00");

            retMap.put("elec", stateInfo.getElecQua());//本次充电电量 0.01kwh
            retMap.put("time", stateInfo.getCharTim());
            retMap.put("DC_v", stateInfo.getDireV());
            retMap.put("DC_i", stateInfo.getDireI());
            retMap.put("av", df.format(stateInfo.getaV()));
            retMap.put("ai", df.format(stateInfo.getaI()));
            retMap.put("bv", stateInfo.getbV());
            retMap.put("bi", stateInfo.getbI());
            retMap.put("cv", stateInfo.getcV());
            retMap.put("ci", stateInfo.getcI());
            //剩余充电时间  秒
            retMap.put("rem_tim", stateInfo.getRemTim());
            retMap.put("soc", stateInfo.getSoc());
            retMap.put("pipleCode",pipleCode);

            System.out.println("实时数据========"+retMap);
            ehcache.put(pipleCode+"chaReaTim",retMap);

            // 告警状态
            Map<String, Object> fauMap = new HashMap<String, Object>();


            //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充电)

            if (6 == stateInfo.getAlarm()){
                // 插入故障
                //更新告警状态
             //   System.out.println("insert fau -------------------"+stateInfo.getAlarm());

                fauMap.put("ala_typ_id", "1");
                fauMap.put("ala_sta", "1");
                fauMap.put("chp_id", ehcache.get(stateInfo.getZhuangId())); // 桩id 缓存中 根据桩编号获取桩id
                fauMap.put("chs_id", "9");
                fauMap.put("ala_lev", "1");
                fauMap.put("ala_dec", "发生了故障(不能给汽车充电)");

                chargingMapper.insertFau(fauMap); //添加故障信息

                // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警）',
                //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
                // 修改设备的状态
                //chargingMapper.updChaPilSta(null,null,stateInfo.getZhuangId(),null,"6","3");

            }

            fauMap.put("alarm", stateInfo.getAlarm());

            ehcache.put(pipleCode+"alarm",fauMap);




        }else if (cmd.equalsIgnoreCase("CA00")) { //充电桩上传充电信息 cmd=202

            ChargeRecordInfo info = ChargeRecordInfo.getInfo(msg);
            pileCode = info.getPileCode();


            System.out.println("===上传充电信息====CA00==========pipleCode = " + pileCode + "桩状态--"+client.getPileState());
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

                System.out.println("2.48个时间段的充电信息（每个时段电量2个字节）-------"+Arrays.toString(client.getChargeRecordInfo().getElectric()));

               // System.out.println("3.48个时间段的充电信息-------"+BytesUtil.bytesToHexString(client.getChargeRecordInfo().getElectric()));

                System.out.println("3.48个时间段的充电信息-------"+ StringUtils.join(BytesUtil.bytesToHexString2(client.getChargeRecordInfo().getElectric()),","));

                byte [] a = client.getChargeRecordInfo().getElectric();

                System.out.println("electric========="+BytesUtil.toHexString(a));

                //新增充电记录
                chargingMapper.insertDatChaInf(client.getChargeRecordInfo());


                //修改 用户 余额 （原来修改余额 放在运营平台上 如果是车自动充满电 会自动结束 运营平台无法计算费用）
                String openId = (String) ehcache.get(client.getChargeRecordInfo().getPileCode()+"openId");// 缓存中 获取 用户penid



                //新增记录

                // 桩id
                String chaId = (String) ehcache.get(client.getChargeRecordInfo().getPileCode());

                // 根据openid 和 桩id 查询 充电记录 信息
                List<Map<String,String>> list = chargingMapper.selDatChaRec(openId,chaId,
                        client.getChargeRecordInfo().getStartTime(),
                        client.getChargeRecordInfo().getEndTime());

                if (list.size() < 1){

                    ChargeRecordInfo chInfo = client.getChargeRecordInfo();

                    System.out.println("info====money===chaAmo"+chInfo.getChargeMoney());

                    Map map = new HashMap();

                    map.put("useId",chInfo.getCardID());
                    map.put("chsId",9); //电站
                    //map.put("chpId",chInfo.getPileCode());// 设备编号
                    map.put("chpId",ehcache.get(chInfo.getPileCode()));// 设备id 通过编号从缓存中获取设备id
                    map.put("chaSta",2); // 充电状态 1为正在充电，2为充电已完成
                    map.put("chaStaTim",chInfo.getStartTime());
                    map.put("chaEndTim",chInfo.getEndTime());
                    map.put("chaAmo",chInfo.getChargeMoney());
                    map.put("chaEleQua",chInfo.getChargeEle());
                    map.put("weOrApp",2); //微信为2，app为1
                    map.put("staSoc",chInfo.getStartSOC());
                    map.put("endSoc",chInfo.getEndSOC());
                    //map.put("purEleAmo",); 购电金额
                    //map.put("arrTim",); 进站时间
                    //map.put("outTim",); 出站时间
                    // map.put("payee",);  收款人
                    //map.put("remark",); 备注
                    //map.put("dcrInf",); 充电记录信息

                    map.put("openid",ehcache.get(chInfo.getPileCode()+"openId")); //微信唯一标识
                    //map.put("chaOrdNum",);充电订单号
                    chargingMapper.insertDatChaRes(map);

                    // 查询用户余额
                    Map<String,Object> accBalMap = chargingMapper.selUseWxAccBal(openId);

                    double accc = (double)accBalMap.get("acc_bal");

                    // 余额 = 余额-充电电费
                    double money = accc - client.getChargeRecordInfo().getChargeMoney();

                    //经验值 = 经验值 + 充电费用
                    double expert = (double)accBalMap.get("use_experience");

                    double useExperience = expert + client.getChargeRecordInfo().getChargeMoney();

                    DecimalFormat df = new DecimalFormat("#.00");

                    // 修改余额 经验值
                    chargingMapper.updUseWx(df.format(money),useExperience,openId);






                }

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
//
                    //alarmMap.put("ala_typ_id", "1");
                    //alarmMap.put("ala_sta", "1");
                    alarmMap.put("chp_id", info.getPile_code());
                    alarmMap.put("chs_id", "9");
                    alarmMap.put("ala_lev", info.getAlarmType());
                    alarmMap.put("ala_dec", info.getAlarms().toString()); ////告警位,32个字节。 每一位代码一个告警，共可表示 256 个告警，具体含义待定义 （ 为服务器能了解桩的告警信息）

                    //chargingMapper.insertAla(alarmMap); //添加告警信息

                    ehcache.put(info.getPile_code()+"alarm",alarmMap);


                }

            }else {
            ctx.fireChannelRead(msg);
        }

        ReferenceCountUtil.safeRelease(msg);





    }
}
