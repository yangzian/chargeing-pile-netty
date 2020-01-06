package com.chargeingpile.netty.chargeingpilenetty.controller;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}
