package com.chargeingpile.netty.chargeingpilenetty.controller;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServerHandler;
import com.chargeingpile.netty.chargeingpilenetty.pojo.BasChaPilPojo;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHCmd;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.ChargeRecordInfo;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StartCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StopCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
//import com.chargeingpile.netty.chargeingpilenetty.util.ASCIIUtil;
//import com.chargeingpile.netty.chargeingpilenetty.util.CommonUtil;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;



import com.chargeingpile.netty.chargeingpilenetty.util.EhcacheUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sun.security.krb5.Config;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ***************************************************
 *
 * @Auther: zianY
 * @Descipion: TODO
 * @CreateDate: 2019-11-15
 * ****************************************************
 */


@RestController
@RequestMapping("/service")
@Api(tags = "充电桩")
public class ChargingController {



    private static final boolean TEST = false;



    @Autowired
    private ChargingServiceImpl chargingService;


    @Autowired
    private ChargingMapper chargingMapper;






    //@ApiIgnore
    @ApiOperation(value = "开启服务")
    @PostMapping(value = "/startServer")
    public ServerResponse startService() {

            return chargingService.startService();

    }





    @ApiOperation(value = "停止服务")
    @PostMapping(value = "/stopServer")
    public ServerResponse stopService() {

        Map resultMap = new HashMap();
        Map failureMap = new HashMap();
        Map successMap = new HashMap();

        // 获取所有 客户端 ip
        List<BasChaPilPojo> chaList = chargingService.selChaIp(null,null);

        if (chaList.size() > 0 ){

            for (BasChaPilPojo basChaPilPojo : chaList){

                Integer i = chargingService.stopService(basChaPilPojo.getChaIp(),basChaPilPojo.getChaNum());

                if (1 == i){ //0成功 1失败

                    failureMap.put(basChaPilPojo.getChaNum(),"ip："+basChaPilPojo.getChaIp()+"--编号："+basChaPilPojo.getChaNum()+"服务停止失败。");
                }
                successMap.put(basChaPilPojo.getChaNum(),"ip："+basChaPilPojo.getChaIp()+"--编号："+basChaPilPojo.getChaNum()+"服务停止成功。");


            }

            resultMap.putAll(successMap);
            resultMap.putAll(failureMap);

            return ServerResponse.createBySuccess("成功。",resultMap);

        }

        return ServerResponse.createBySuccess("暂且没有可停止的服务。",null);



    }


    /**
     * 预约充电/即时充电-开启
     *
     * 预约充电 时间格式为String 2020-01-07 12:01:01 正常开启，充电桩状态 为 预约 但是 桩 无反应？？？
     * 即时充电 开启 可用
     *
     * @param staTim
     * @param endTime
     * @param flag
     * @return
     */
    @ApiOperation(value = "即时充电-----开启")
    @PostMapping(value = "/startOrder")
    public ServerResponse startOrder(@RequestParam(value = "staTim",required = false) String staTim,
                                     @RequestParam(value = "endTime",required = false) String endTime,
                                     @RequestParam(value = "cha_num") String cha_num,
                                     @RequestParam(value = "userId") String userId,
                                     @RequestParam(value = "openId") String openId,
                                     //value = "即时充电-开启，状态为1。(默认为1)",
                                     @RequestParam(value = "flag",defaultValue = "1") String flag){
        try {
//



            // 查询用户余额
            Map<String,Object> accBalMap = chargingMapper.selUseWxAccBal(openId);

            double accc = (double)accBalMap.get("acc_bal");

            if (accc < 2){

                return ServerResponse.createByErrorMessage(new Date()+"桩编码为"+cha_num+"余额不够抵扣电费，请充值后再进行充电。");

            }


            System.out.println("开始充电-------桩-"+cha_num+"openId-----------"+ openId);

            EhcacheUtil ehcache = EhcacheUtil.getInstance();
            ehcache.put(cha_num+"openId",openId);

            // chp_id  use_id  sta_tim  end_tim tim_len flag dev_add_num

            // 充电桩ID
            final String chp_id = "";

            // 用户卡号/用户识别号
            String use_id = userId;
            // 开始时间
            String sta_tim = staTim;
            // 结束时间
            String end_tim = endTime;
            //时长=
            String tim_len = "";

            String dev_add_num = ""; //硕维 在集中器 下的编号

            String pileNum;

            if (!CommonUtil.isEmpty(dev_add_num)){

                pileNum = BytesUtil.toHexString(dev_add_num);


            }

            // 0-预约，1-开启充电
            //String flag = "0";


            String chp_ip = ""; //充电桩ip
            String chp_por = ""; // 充电桩端口号
            //String cha_num = "014"; // 充电桩 编号
           // String man_nam = ""; // 充电桩名称
            // 通讯设备
            String chp_com_equ = "";

            String desc = "";




            //根据充电桩id 查询 桩信息bas_cha_pil
            List<BasChaPilPojo> chaList = chargingService.selChaIp(null,cha_num);
            if (chaList.size() > 0 ){

                chp_ip = chaList.get(0).getChaIp();
                chp_por = chaList.get(0).getChaPor();
                cha_num = chaList.get(0).getChaNum();

                String pilSta = chaList.get(0).getChaPilSta();

                if (!StringUtil.equals("2",pilSta)){
                    return ServerResponse.createByErrorMessage("编号："+cha_num+"的充电桩非空闲状态，暂时无法开启，请重新连接。");
                }


                //chp_com_equ = chaPojo.getChpComEqu();
                //man_nam = chaPojo.getManNam();

            }



            Map<String, Object> retMap = new HashMap<String, Object>();

            Thread.sleep(2000);

            if (chp_por.equals("9999")) { //盛宏

                if (TEST) {
                    // 充电
                    desc = "开启充电成功";
                    retMap.put("state", 1);
                    return ServerResponse.createBySuccess("开启充电成功。",retMap);
                    //new Thread(new ChargeRun(cha_num)).start();

                } else {

                    if (!CommonUtil.isEmpty(chp_ip)) {

                        ClientConnection client = ClientManager.getClientConnection(chp_ip, cha_num);

                        ChannelHandlerContext ctx = NettyServerHandler.getClientConnection(chp_ip);

                        System.out.println("ctx==start==============="+ctx);




                        if (client == null || client.getCtx() == null) {
                        //if (ctx == null) {
                            desc = "桩未连接";
                            retMap.put("desc","充电桩未连接。");
                            retMap.put("state", 2);

                            return ServerResponse.createBySuccess("桩未连接",retMap);


                        } else {

                             client.setUserID(use_id);

                            StartCharger charger = new StartCharger();//预约/即时充电

                            String charType = "";

                            if (flag.equals("0")) { // 预约

                               charType = StartCharger.ORDER;
/*

                               Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Long.valueOf(end_tim));

                                end_tim = CommonUtil.getBCDTimeStr(calendar);

                                charger.setTime(end_tim); // 预约或定时启动时间（8字节）
*/


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Calendar calendar = Calendar.getInstance();

                                Date t = sdf.parse(end_tim);

                                calendar.setTime(t);

                                String c = CommonUtil.getBCDTimeStr(calendar);

                                charger.setTime(c);


                            } else {// 即时充电
                                charType = StartCharger.CHARGE;

                            }


                            charger.setCharType(charType);

                            String card = ASCIIUtil.ASCII2HexString(use_id);

                            charger.setCard(card);

                            //SHCmd.startCharge(client.getCtx(), charger);
                            SHCmd.startCharge(ctx, charger);

                            // 等待结果
                            long start = System.currentTimeMillis();


                            while (true) {
                                long end = System.currentTimeMillis();

                                client = ClientManager.getClientConnection(chp_ip, cha_num);

                                if ((end - start) > 60 * 1000) { // 大于60s，视为失败
                                    retMap.put("state", 0);
                                    desc = "失败-超时";

                                    return ServerResponse.createBySuccess("连接超时（连接时间大于60s），请重新再试。",retMap);

                                    //break;
                                } else if (client.getPileState() == ClientConnection.STATE_START_FAILED) {
                                    desc = "失败";
                                    retMap.put("state", 0);
                                    return ServerResponse.createBySuccess("连接失败，请重新插入电源。",retMap);

                                  //  break;
                                } else if ((charType.equals(StartCharger.ORDER))
                                            && (client.getPileState() == ClientConnection.STATE_ORDER)) {
                                    // 预约
                                    desc = "预约成功";
                                    retMap.put("state", 1);
                                    return ServerResponse.createBySuccess("预约充电成功。",retMap);

                                   // break;
                                } else if ((charType.equals(StartCharger.CHARGE))
                                            && (client.getPileState() == ClientConnection.STATE_CHARGE)) {
                                    // 充电
                                    desc = "开启充电成功";
                                    retMap.put("state", 1);

                                    //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                                    // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警',
                                    //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
                                    // 修改设备的状态
                                    //chargingMapper.updChaPilSta(null,null, cha_num,null,"1","0");


                                    return ServerResponse.createBySuccess("开启充电成功。",retMap);

                                   // break;

                                }
                            }
                        }
                    }

                }

            }

            return ServerResponse.createBySuccess("成功",retMap);

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println("sta========"+e);

            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

    }


    /**
     * 停止充电 或 取消预约 充电
     *
     * 取消预约可以用，
     * 即时充电 无法取消？？？
     *
     * @param flag
     * @return
     */
    @ApiOperation(value = "即时充电-----关闭")
    @PostMapping(value = "/stopCharge")
    public ServerResponse stopCharge(
            @RequestParam(value = "cha_num") String cha_num,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "openId") String openId,
            //即时充电-停止，状态为1
            @RequestParam(value = "flag",defaultValue = "1") String flag) {
        try {



            if (cha_num.isEmpty()){
                return ServerResponse.createByErrorMessage("充电桩编号不能为空");
            }

             if (userId.isEmpty()){
                return ServerResponse.createByErrorMessage("用户唯一识别号不能为空");
            }



            // chp_id  use_id  flag  dev_add_num

            EhcacheUtil ehcache = EhcacheUtil.getInstance();


            String chp_id;
            String pileNum ; // dev_add_num

            // 充电桩ID
            String chp_ip = ""; //充电桩ip
            //String usr_id = "0000120190317244";

            String chp_por = ""; // 充电桩端口号
            //String cha_num = "014"; // 充电桩 编号

            // 0-取消预约，1-停止充电
           // String flag = "0";

            // buwei
            //while (usr_id.length() < 64) {
                //usr_id += "0";
            //}


            String chp_com_equ = "";
            String man_nam = "";

            //根据充电桩id 查询 桩信息bas_cha_pil
            List<BasChaPilPojo> chaList = chargingService.selChaIp(null,cha_num);
            if (chaList.size() > 0){

                chp_ip = chaList.get(0).getChaIp();
                chp_por = chaList.get(0).getChaPor();
                cha_num = chaList.get(0).getChaNum();
                //chp_com_equ = chaPojo.getChpComEqu();
                //man_nam = chaPojo.getManNam();

                String pilSta = chaList.get(0).getChaPilSta();

                if (StringUtil.equals("1",pilSta) || StringUtil.equals("2",pilSta)){
                    //return ServerResponse.createByErrorMessage("编号："+cha_num+"的充电桩正在充电或取消充电，请无须重复关闭。");
                }


            }



            //
            Map<String, Object> retMap = new HashMap<String, Object>();
            String desc = "";

            Thread.sleep(2000);

            if (chp_por.equals("9999")){

                if (TEST) {

                    desc = "停止充电成功";

                    Object object = ehcache.get(cha_num+"retMap");

                    if (object != null){

                       // System.out.println("停止充电成功-----"+object.toString());
                    }

                    retMap.put("state", 1);

                    //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                    // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警',
                    //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
                    // 修改设备的状态
                    //chargingMapper.updChaPilSta(null,null, cha_num,null,"2","0");


                } else {
                    if (!CommonUtil.isEmpty(chp_ip)) {



                        ClientConnection client = ClientManager.getClientConnection(chp_ip, cha_num);

                        ChannelHandlerContext ctx = NettyServerHandler.getClientConnection(chp_ip);
                        System.out.println("stop======="+ctx);


                        if (client == null || client.getCtx() == null) {
                            desc = "桩未连接";
                            retMap.put("desc","桩未连接");
                            retMap.put("state", 2);

                        } else {

                            String startAdd = "";

                            if (flag.equals("0")) { // 预约

                                startAdd = StopCharger.ADDR_ORDER;

                            } else {// 即时充电

                                startAdd = StopCharger.ADDR_CHARGE;

                            }

                            StopCharger charger = new StopCharger();

                            charger.setAddr(startAdd);

                           // SHCmd.stopCharge(client.getCtx(), charger);
                            SHCmd.stopCharge(ctx, charger);

                            long start = System.currentTimeMillis();

                            while (true) {

                                long end = System.currentTimeMillis();

                                client = ClientManager.getClientConnection(chp_ip, cha_num);

                               // 0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状态 6‐系统故障(不能给汽车充电)
                                if ((end - start) > 60 * 1000) { // 大于60s，视为失败
                                    desc = "失败-超时";
                                    retMap.put("desc","失败-超时");
                                    retMap.put("state", 0);
                                    break;
                                } else if (client.getPileState() == ClientConnection.STATE_START_FAILED) {
                                    desc = "失败";
                                    retMap.put("desc","桩启动失败");
                                    retMap.put("state", 0);
                                    break;
                                } else if ((startAdd.equals(StopCharger.ADDR_ORDER))
                                            && (client.getPileState() == ClientConnection.STATE_NORMAL)) {
                                    // 预约
                                    desc = "取消预约成功";
                                    retMap.put("desc","取消预约成功");
                                    retMap.put("state", 1);
                                    break;
                                } else if ((startAdd.equals(StopCharger.ADDR_CHARGE))
                                            && (client.getPileState() == ClientConnection.STATE_NORMAL
                                            || client.getPileState() == ClientConnection.STATE_CHARGE_OVER)) {
                                    // 充电停止
                                    desc = "停止充电成功";
                                    retMap.put("desc","停止充电成功");
                                    retMap.put("state", 1);


                                    //0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
                                    // cha_pil_sta` 充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警',
                                    //fau_sta '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)
                                    // 修改设备的状态
                                    //chargingMapper.updChaPilSta(null,null, cha_num,null,"2","0");


                                    ChargeRecordInfo charge = client.getChargeRecordInfo();

                                    if (charge != null) {

                                        double elecQua = charge.getChargeEle();
                                        int time = charge.getDurationTime();
                                        double read_before = charge.getChargeStart();
                                        double read_after = charge.getChargeEnd();
                                        retMap.put("elec", elecQua);
                                        retMap.put("time", time);
                                        retMap.put("read_before", read_before);
                                        retMap.put("read_after", read_after);

                                        ehcache.put(cha_num+"retMap",retMap);

                                       // System.out.println("停止充电成功==========="+retMap);

                                        break;

                                    }
                                }
                            }
                        }

                    }
                }
                return ServerResponse.createBySuccess("成功",retMap);
            }

            return ServerResponse.createBySuccess("成功",retMap);

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println("stop========"+e);

            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

    }


    /**
     * 获取充电桩实时数据
     * @param chp_id 充电桩编号
     * @return
     */
    @ApiOperation(value = "充电桩实时数据获取")
    @PostMapping(value = "/chaReaTim")
    public ServerResponse chaReaTim(@RequestParam(value = "chp_id") String chp_id,
                                    @RequestParam(value = "openid") String openid){

        EhcacheUtil ehcache = EhcacheUtil.getInstance();

        Object object = ehcache.get(chp_id+"chaReaTim");



        if (!StringUtil.isBlank(openid)){

            ehcache.put(chp_id+"openId",openid);  // 桩编号+openId，openid

        }




        return ServerResponse.createBySuccess("充电桩实时数据获取成功。",object);





    }

    /**
     * 获取充电桩告警状态
     * @param chp_id
     * @return
     */
    @ApiOperation(value = "获取充电桩告警状态")
    @PostMapping(value = "/getAlarm")
    public ServerResponse getAlarm(@RequestParam(value = "chp_id") String chp_id){

        EhcacheUtil ehcache = EhcacheUtil.getInstance();

        Object object = ehcache.get(chp_id+"alarm");

        return ServerResponse.createBySuccess("充电桩实时数据获取成功。",object);





    }


}
