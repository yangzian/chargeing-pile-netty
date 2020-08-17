package com.chargeingpile.netty.chargeingpilenetty.job;


import com.alibaba.fastjson.JSONObject;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.pojo.BasChaPilPojo;
import com.chargeingpile.netty.chargeingpilenetty.util.ApplicationContextUtils;
import com.chargeingpile.netty.chargeingpilenetty.util.DateUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.EhcacheUtil;
import com.chargeingpile.netty.chargeingpilenetty.util.SpringContextHolder;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class Task {




    private ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();



    //ChargingMapper chargingMapper = (ChargingMapper) applicationContext.getBean("chargingMapper");

    //clientMapper = ApplicationContextUtils.().getBean(ClientMapper.class);


    //3.添加定时任务
    @Scheduled(cron = "0/20 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    private void configureTasks() {

        try {

            ChargingMapper chargingMapper = ApplicationContextUtils.getApplicationContext().getBean(ChargingMapper.class);

            System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

        EhcacheUtil ehcache = EhcacheUtil.getInstance();

        // 所有 桩 信息
        List<BasChaPilPojo> basChaPilPojoList = chargingMapper.selChaIp(null,null);

        System.out.println(basChaPilPojoList);

        if (basChaPilPojoList.size() > 0){

            for (BasChaPilPojo basChaPilPojo : basChaPilPojoList) {

                // 充电中
                if (basChaPilPojo.getChaPilSta().equals("1")){

                    //  根据桩 编号 查询 充电桩定价方案标识
                    Map<String, Object> opeMap = new HashMap<>();

                    opeMap.put("chpId",basChaPilPojo.getChaNum());

                    List<Map<String,Object>> ociLst = chargingMapper.getOpeConInf(opeMap);

                    System.out.println(ociLst+"==========ociLst=======size======="+ociLst.size()+"opeMap===="+opeMap);

                    if (ociLst.size() > 0){

                        System.out.println("状态"+ociLst.get(0).get("cha_pil_sta").toString());

                            // 根据桩编码 获取其充电 实时数据信息
                        Object obj = ehcache.get(basChaPilPojo.getChaNum()+"chaReaTim");


                        if (obj != null){

                           Map map = JSONObject.parseObject(JSONObject.toJSONString(obj), Map.class);

                            //本次充电电量 0.01kwh
                            int ele =(int) map.get("elec");

                            // 获取分时段 定价方案
                            Map<String, Object> codMap = new HashMap<String, Object>();
                            codMap.put("chaSchCod",ociLst.get(0).get("cha_sch_cod"));
                            List<Map<String,Object>> schLst = chargingMapper.getChaSchCod(codMap);

                            System.out.println("schLst=====1======="+schLst+"======size="+schLst.size());

                            if (schLst.size() > 0){

                                System.out.println("schLst=2==========="+schLst+"======size="+schLst.size());

                                // 当前时间
                                Calendar now = Calendar.getInstance();
                                // 当前时间 转为 12:01:01 时分秒格式
                                String nowTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

                                System.out.println("nowTime============"+nowTime+"======");

                                for (Map<String,Object> schMap : schLst){

                                    // 定价方案 开始时间段
                                    String staTime = schMap.get("int_sta_tim").toString();
                                    // 定价方案 结束时间段
                                    String endTime = schMap.get("int_end_tim").toString();

                                    // 判断当前时间 是否 在区间内
                                    boolean b = DateUtil.isInTimeRange(nowTime,staTime,endTime);

                                    if (b){
                                        // 在 则 获取 该时段价格
                                        Double pri = Double.parseDouble(schMap.get("tim_int_pri").toString());

                                        // 缓存中 获取 用户openid
                                        String openId = (String) ehcache.get(basChaPilPojo.getChaNum()+"openId");

                                        System.out.println("桩编码======="+basChaPilPojo.getChaNum()+"openid"+openId);

                                        // 查询用户余额
                                        Map<String,Object> accBalMap = chargingMapper.selUseWxAccBal(openId);

                                        double accc = (double)accBalMap.get("acc_bal");

                                        // 用户剩余余额 = 已有余额 - 本次充电电费(充电电量 * 价格)
                                        double money = accc - 3.2 * pri;

                                        DecimalFormat dft = new DecimalFormat("#.00");

                                        System.out.println("money======="+money);

                                        //经验值 = 经验值 + 充电费用
                                        double expert = (double)accBalMap.get("use_experience");

                                        System.out.println("expert======="+expert);

                                        double useExperience = expert + ele * pri;


                                        //余额 >2 正常充电
                                        if (money > 2){

                                            System.out.println(new Date()+"余额够抵扣电费，正常充电。"+"=====桩id"+basChaPilPojo.getChaNum()+"用户openId====="+openId);

                                        }else {
                                            // 修改余额 经验值
                                            chargingMapper.updUseWx(dft.format(money),useExperience,openId);

                                            // 余额 <2 结束充电

                                            String content = Request.Post("http://localhost:8090/service/stopCharge")
                                                    .addHeader("X-Custom-header", "stuff")
                                                    .bodyForm(Form.form().add("cha_num", basChaPilPojo.getChaNum())
                                                            .add("userId", "0")
                                                            .add("openId",openId).build())
                                                    .execute().returnContent().asString();

                                            //String content = HttpPost.http("http://218.17.24.102:8090/service/stopCharge","cha_num="+pipleCode+"&userId=0&"+"openId"+openId);

                                            System.out.println("调用结束接口返回结果content======"+content);

                                            System.out.println(new Date()+"余额不够抵扣，结束充电"+"=====桩id"+basChaPilPojo.getChaNum()+"用户openId====="+openId);

                                        }

                                    }

                                }
                            }



                        }




                    }







                }






            }




        }



        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }


    }

}
