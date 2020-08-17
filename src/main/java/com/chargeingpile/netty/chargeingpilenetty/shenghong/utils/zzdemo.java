package com.chargeingpile.netty.chargeingpilenetty.shenghong.utils;

import com.chargeingpile.netty.chargeingpilenetty.util.ApplicationContextUtils;
import com.chargeingpile.netty.chargeingpilenetty.util.CommonUtils;
import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class zzdemo {



    @Test
    public void demo() throws Exception{



       // String end_tim = "1578367686";
//19700119142607FF
        String end_tim = "1578367690";
/*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        Date t = sdf.parse(end_tim);

        calendar.setTime(t);

        String c = CommonUtil.getBCDTimeStr(calendar);

        System.out.println(c);*/

/*

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(end_tim));
        end_tim = CommonUtil.getBCDTimeStr(calendar);
        System.out.println(end_tim);

*/


String a = CommonUtil.setBCDTimeStr("20150722131615");
        System.out.println(a);



        }




        @Test
        public void cach(){
            EhCacheCacheManager cacheCacheManager= ApplicationContextUtils.applicationContext.getBean(EhCacheCacheManager.class);

            //获取Cache
            Cache cache=cacheCacheManager.getCache("SystemCache");
            cache.put("Hello-key", "Hello-value");
            System.out.println("缓存名："+cache.getName());

            System.out.println("缓存Hello-key："+cache.get("Hello-key", String.class));


        }


}
