package com.chargeingpile.netty.chargeingpilenetty.shenghong.utils;

import com.chargeingpile.netty.chargeingpilenetty.util.CommonUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class zz {



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
}
