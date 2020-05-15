package com.chargeingpile.netty.chargeingpilenetty.util;

import org.junit.Test;

public class PileStaUtil {


    public static int getPileSta(int pileSta){


        //  `cha_pil_sta` '充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线,6为告警）',

        //充电桩：0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)

        switch (pileSta){

            case 0:
                return 2;

            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 1;
            case 4:
                return 5;
            case 5:
                return 4;
            case 6:
                return 3;


        }

        return 2;


    }


    @Test
    public void testDemo(){
        int a = getPileSta(0);
        System.out.println(a);

    }
}
