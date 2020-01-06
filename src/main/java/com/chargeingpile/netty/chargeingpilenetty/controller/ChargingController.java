package com.chargeingpile.netty.chargeingpilenetty.controller;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHCmd;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.ChargeRecordInfo;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StartCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StopCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.Config;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/chargingController")
@Api(tags = "测试")
public class ChargingController {



    private static final boolean TEST = false;



    @Autowired
    private ChargingServiceImpl chargingService;




    @ApiOperation(value = "开启服务")
    @PostMapping(value = "/startService")
    public ServerResponse startService() {
        try {

            return chargingService.startService();


        } catch (Exception e) {

            e.printStackTrace();
            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

    }





    @ApiOperation(value = "预约充电/即时充电-开启")
    @PostMapping(value = "/startOrder")
    public ServerResponse startOrder(@RequestParam() String statim, @RequestParam() String endTime){
        try {

            // 充电桩ID
            final String chp_id = "";
            // 用户卡号/用户识别号
            String use_id = "0000120190317244";
            // 开始时间
            String sta_tim = statim;
            // 结束时间
            String end_tim = endTime;
            // 时长=
            String tim_len = "";
            // 0-预约，1-开启充电
            String flag = "0";


            String chp_ip = "169.254.151.100"; //充电桩ip
            String chp_por = "9999"; // 充电桩端口号
            String cha_num = "075586511588001"; // 充电桩 编号
            String man_nam = ""; // 充电桩名称
            // 集中器编号
            String chp_com_equ = "";

            String desc = "";


            Map<String, Object> retMap = new HashMap<String, Object>();


            if (chp_por.equals("9999")) { //盛宏



                if (TEST) {
                    // 充电
                    desc = "开启充电成功";
                    retMap.put("state", 1);

                   // new Thread(new ChargeRun(cha_num)).start();


                } else {

                    if (!CommonUtil.isEmpty(chp_ip)) {

                        ClientConnection client = ClientManager.getClientConnection(chp_ip, cha_num);

                        if (client == null || client.getCtx() == null) {
                            desc = "桩未连接";
                            retMap.put("state", 2);
                        } else {
                            client.setUserID(use_id);

                            StartCharger charger = new StartCharger();//预约/即时充电

                            String charType = "";

                            if (flag.equals("0")) { // 预约

                                charType = StartCharger.ORDER;

                               /* Calendar calendar = Calendar.getInstance();
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

                            SHCmd.startCharge(client.getCtx(), charger);

                            // 等待结果
                            long start = System.currentTimeMillis();


                            while (true) {
                                long end = System.currentTimeMillis();

                                client = ClientManager.getClientConnection(chp_ip, cha_num);

                                if ((end - start) > 60 * 1000) { // 大于60s，视为失败
                                    retMap.put("state", 0);
                                    desc = "失败-超时";
                                    break;
                                } else if (client.getPileState() == ClientConnection.STATE_START_FAILED) {
                                    desc = "失败";
                                    retMap.put("state", 0);
                                    break;
                                } else if ((charType.equals(StartCharger.ORDER))
                                            && (client.getPileState() == ClientConnection.STATE_ORDER)) {
                                    // 预约
                                    desc = "预约成功";
                                    retMap.put("state", 1);
                                    break;
                                } else if ((charType.equals(StartCharger.CHARGE))
                                            && (client.getPileState() == ClientConnection.STATE_CHARGE)) {
                                    // 充电
                                    desc = "开启充电成功";
                                    retMap.put("state", 1);
                                    break;

                                }
                            }
                        }
                    }

                }

            }

            return ServerResponse.createBySuccess("成功",retMap);

        } catch (Exception e) {

            e.printStackTrace();
            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

    }






    @ApiOperation(value = "停止充电 或 取消预约 充电")
    @PostMapping(value = "/stopCharge")
    public ServerResponse stopCharge() {
        try {



            // 充电桩ID
            String chp_ip = "169.254.151.100"; //充电桩ip
            String usr_id = "0000120190317244";

            String chp_por = "9999"; // 充电桩端口号
            String cha_num = "075586511588001"; // 充电桩 编号

            // 0-取消预约，1-停止充电
            String flag = "0";


            // buwei
            while (usr_id.length() < 64) {
                usr_id += "0";
            }

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("chp_id", chp_ip);

            // 查询桩ip

            //
            Map<String, Object> retMap = new HashMap<String, Object>();
            String desc = "";

            if (chp_por.equals("9999")){

                CacheManager cacheManager = CacheManager.getInstance();

                Cache sample = cacheManager.getCache("loginCache");
               // sample.remove(cha_num + "staChp");

                Element element = new Element(cha_num + "staChp", 2);

               // sample.put(element);

                Thread.sleep(2000);
                if (TEST) {

                    desc = "停止充电成功";
                    retMap.put("state", 1);
                    element = sample.get(cha_num + "retMap");
                    Map<String, Object> map1 = (Map<String, Object>) element.getObjectValue();
                    retMap.put("elec", map1.get("elec"));
                    retMap.put("time", map1.get("time"));
                    retMap.put("read_before", map1.get("read_before"));
                    retMap.put("read_after", map1.get("read_after"));
                } else {
                    if (!CommonUtil.isEmpty(chp_ip)) {
                        ClientConnection client = ClientManager.getClientConnection(chp_ip, cha_num);

                        if (client == null || client.getCtx() == null) {
                            desc = "桩未连接";
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

                            SHCmd.stopCharge(client.getCtx(), charger);

                            long start = System.currentTimeMillis();
                            while (true) {
                                long end = System.currentTimeMillis();
                                client = ClientManager.getClientConnection(chp_ip, cha_num);
                                if ((end - start) > 60 * 1000) { // 大于60s，视为失败
                                    desc = "失败-超时";
                                    retMap.put("state", 0);
                                    break;
                                } else if (client.getPileState() == ClientConnection.STATE_START_FAILED) {
                                    desc = "失败";
                                    retMap.put("state", 0);
                                    break;
                                } else if ((startAdd.equals(StopCharger.ADDR_ORDER))
                                            && (client.getPileState() == ClientConnection.STATE_NORMAL)) {
                                    // 预约
                                    desc = "取消预约成功";
                                    retMap.put("state", 1);
                                    break;
                                } else if ((startAdd.equals(StopCharger.ADDR_CHARGE))
                                            && (client.getPileState() == ClientConnection.STATE_NORMAL
                                            || client.getPileState() == ClientConnection.STATE_CHARGE_OVER)) {
                                    // 充电
                                    desc = "停止充电成功";
                                    retMap.put("state", 1);
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
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

            }

            return ServerResponse.createBySuccess("成功",retMap);

        } catch (Exception e) {

            e.printStackTrace();
            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

    }




}
