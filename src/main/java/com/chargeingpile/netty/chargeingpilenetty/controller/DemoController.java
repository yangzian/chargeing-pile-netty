package com.chargeingpile.netty.chargeingpilenetty.controller;


import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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






}
