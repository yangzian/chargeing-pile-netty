package com.chargeingpile.netty.chargeingpilenetty.controller;


import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.DateUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.EhcacheUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.HttpPost;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/demoController")
@Api(tags = "测试")
public class DemoController {


    @Autowired
    private ChargingServiceImpl chargingService;

    @Autowired
    private ChargingMapper chargingMapper;


    @ApiOperation(value = "测试")
    @GetMapping(value = "/testDemo")
    public ServerResponse getUserInfo() {


        return chargingService.getUserInfo();


    }


    @ApiOperation(value = "测试费用")
    @GetMapping(value = "/testEle")
    public ServerResponse testEle() throws Exception {


        //  充电桩定价方案标识
        Map<String, Object> opeMap = new HashMap<String, Object>();
        opeMap.put("chpId", "002");//本次充电电量 0.01kwh
        List<Map<String, Object>> ociLst = chargingMapper.getOpeConInf(opeMap);

        if (ociLst.size() > 0) {

            // 状态为 1 充电中
            if ("1".equals(ociLst.get(0).get("cha_pil_sta").toString())) {

                //本次充电电量 0.01kwh
                double ele = 0.01;

                // 获取分时段 定价方案
                Map<String, Object> codMap = new HashMap<String, Object>();
                codMap.put("chaSchCod", ociLst.get(0).get("cha_sch_cod"));
                List<Map<String, Object>> schLst = chargingMapper.getChaSchCod(codMap);

                if (schLst.size() > 0) {

                    // 当前时间
                    Calendar now = Calendar.getInstance();
                    // 当前时间 转为 12:01:01 时分秒格式
                    String nowTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);

                    for (Map<String, Object> schMap : schLst) {

                        // 定价方案 开始时间段
                        String staTime = schMap.get("int_sta_tim").toString();
                        // 定价方案 结束时间段
                        String endTime = schMap.get("int_end_tim").toString();

                        // 判断当前时间 是否 在区间内
                        boolean b = DateUtil.isInTimeRange(nowTime, staTime, endTime);

                        if (b) {
                            // 在 则 获取 该时段价格
                            Double pri = Double.parseDouble(schMap.get("tim_int_pri").toString());

                            // 缓存中 获取 用户openid
                            //String openId = (String) ehcache.get(client.getChargeRecordInfo().getPileCode()+"openId");
                            String openId = "ofbASxNBpP9WzHsKp6A6FikcDalE";

                            // 查询用户余额
                            Map<String, Object> accBalMap = chargingMapper.selUseWxAccBal(openId);

                            double accc = (double) accBalMap.get("acc_bal");

                            // 用户剩余余额 = 已有余额 - 本次充电电费(充电电量 * 价格)
                            double money = accc - ele * pri;

                            //余额 >1 正常充电 并修改用户金额 和 经验值
                            if (money > 1) {

                                //经验值 = 经验值 + 充电费用
                                double expert = (double) accBalMap.get("use_experience");

                                double useExperience = expert + 0.02;

                                DecimalFormat dft = new DecimalFormat("#.00");

                                // 修改余额 经验值
                                chargingMapper.updUseWx(dft.format(money), useExperience, openId);

                                System.out.println(new Date() + "余额够抵扣电费，正常充电。" + "=====桩id" + "用户openId=====" + openId);

                            } else {

                                // 余额 <1 结束充电

                                MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                                        .setCharset(Charset.forName("utf-8"));
                                builder.addTextBody("cha_num", "002");
                                builder.addTextBody("userId", "0");
                                builder.addTextBody("openId", openId);

                                String content = Request.Post("http://218.17.24.102:8090/service/stopCharge")
                                        .body(builder.build())
                                        .execute().returnContent().asString();

                                System.out.println(new Date() + "余额不够抵扣，结束充电" + "=====桩id" + "用户openId=====" + openId);

                            }

                        }

                    }
                }


            }

        }



        return null;
    }



    @Test
    public void demo()throws Exception{

        //double f = 29.954000000000002;
        double f = 0;

        DecimalFormat df = new DecimalFormat("#.00");

        System.out.println(df.format(f));


        float a = 1000f;
        float b = 23.88f;

        float c = a - b;
        System.out.println(c);


        System.out.println(Double.valueOf("12.233"));



        double m = 1.98;

        double i = 1.00;

        if (m > 1){
            System.out.println("aaaaa");
        }else {
            System.out.println("bbbbbb");
        }

/*
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(Charset.forName("utf-8"));
        builder.addTextBody("cha_num", "002");
        builder.addTextBody("userId", "0");
        builder.addTextBody("openId", "ofbASxNBpP9WzHsKp6A6FikcDalE");

        String content = Request.Post("http://218.17.24.102:8090/service/stopCharge")
                .body(builder.build())
                .execute().returnContent().asString();

     */

        System.out.println(new Date()+"余额不够抵扣，结束充电"+"=====桩id"+"用户openId=====");


        //String co = HttpPost.http("http://218.17.24.102:8090/service/stopCharge","cha_num=002&userId=0&openId=ofbASxNBpP9WzHsKp6A6FikcDalE");

/*

        String content = Request.Post("http://218.17.24.102:8090/service/stopCharge")
                .addHeader("X-Custom-header", "stuff")
                //.viaProxy(new HttpHost("myproxy", 8090))
                .bodyForm(Form.form().add("cha_num", "002")
                        .add("userId", "0")
                        .add("openId","ofbASxNBpP9WzHsKp6A6FikcDalE").build())
                .execute().returnContent().asString();

*/

        // 当前时间
        Calendar now = Calendar.getInstance();
        // 当前时间 转为 12:01:01 时分秒格式
        String nowTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);


        System.out.println(nowTime);

        EhcacheUtil ehcache = EhcacheUtil.getInstance();

        ehcache.putList("123"+"stop"+"123","0000");

        Thread.sleep(1000*4);

        Object sta1 =  ehcache.getList("123"+"stop"+"123");


        System.out.println("1====================="+sta1);

        Thread.sleep(1000*2);

        Object sta2 =  ehcache.getList("123"+"stop"+"123");

        System.out.println("2====================="+sta2);

    }







}
