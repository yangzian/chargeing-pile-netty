package com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ChargingMapper chargingMapper;



    public ServerResponse startService(){


        return null;
    }






}
