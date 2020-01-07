package com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.constans.DefaultConstans;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServer;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * ***************************************************
 *
 * @Auther: zianY
 * @Descipion: TODO
 * @CreateDate: 2019-11-15
 * ****************************************************
 */

@Service
public class ChargingServiceImpl implements ChargingService {





    @Autowired
    private NettyServer nettyServer;


    @Autowired
    private ChargingMapper chargingMapper;


    public ServerResponse startService(){

        try {

            InetSocketAddress address = new InetSocketAddress(DefaultConstans.SOKET_IP, DefaultConstans.SOKET_PORT);
           int i =  nettyServer.start(address);

           if (i != 0){
               return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
           }




        } catch (Exception e) {

            e.printStackTrace();
            return ServerResponse.createByErrorMessage("服务器异常，请联系管理员。");
        }

        return ServerResponse.createBySuccess("启动成功",0);

    }






}
