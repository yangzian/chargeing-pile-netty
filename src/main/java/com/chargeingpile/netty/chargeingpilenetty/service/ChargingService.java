package com.chargeingpile.netty.chargeingpilenetty.service;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.pojo.BasChaPilPojo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

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

   Integer stopService(String chaIp,String chaNum);




   // 根据桩id 查询桩ip及端口号等信息
   List<BasChaPilPojo> selChaIp(String chaId,String chaNum);



}
