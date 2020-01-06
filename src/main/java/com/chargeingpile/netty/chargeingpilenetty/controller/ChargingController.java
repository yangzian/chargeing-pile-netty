package com.chargeingpile.netty.chargeingpilenetty.controller;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHCmd;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StartCharger;
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
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.Config;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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





    @ApiOperation(value = "预约充电/即时充电")
    @PostMapping(value = "/startOrder")
    public ServerResponse startOrder() {
        try {

            // 充电桩ID
            final String chp_id = "";
            // 用户卡号/用户识别号
            String use_id = "11010001668019601101000166801960";
            // 开始时间
            String sta_tim = "";
            // 结束时间
            String end_tim = "";
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
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Long.valueOf(end_tim));

                                end_tim = CommonUtil.getBCDTimeStr(calendar);

                                charger.setTime(end_tim); // 预约或定时启动时间（8字节）

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




}
