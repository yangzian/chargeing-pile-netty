package com.chargeingpile.netty.chargeingpilenetty.service;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.github.pagehelper.PageInfo;

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

public interface ChargingService {


   /**
    * 测试
    * @return
    */
   ServerResponse getUserInfo();



   ServerResponse startService() throws Exception;

   ServerResponse stopService();




}
