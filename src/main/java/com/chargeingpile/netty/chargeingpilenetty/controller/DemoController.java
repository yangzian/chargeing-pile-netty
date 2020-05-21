package com.chargeingpile.netty.chargeingpilenetty.controller;


import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@RestController
@RequestMapping("/demoController")
@Api(tags = "测试")
public class DemoController {


    @Autowired
    private ChargingServiceImpl chargingService;


    @ApiOperation(value = "测试")
    @GetMapping(value = "/testDemo")
    public ServerResponse getUserInfo() {


        return chargingService.getUserInfo();


    }



    @Test
    public void demo(){

        //double f = 29.954000000000002;
        double f = 0;

        DecimalFormat df = new DecimalFormat("#.00");

        System.out.println(df.format(f));


        float a = 1000f;
        float b = 23.88f;

        float c = a - b;
        System.out.println(c);


        System.out.println(Double.valueOf("12.233"));





    }




}
