package com.chargeingpile.netty.chargeingpilenetty.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

import javafx.scene.chart.PieChart.Data;

public class TimeUtils {

	public static int START_YEAR = 2000;
	public static String TIME_START_SH = "20";
	
	
	/**
	 * 获取当前时间的BCD格式
	 * @param
	 * @return
	 */
	public static byte[] getCurBCDTime() {
	    byte[] res = new byte[7];
	    res = BytesUtil.hexStringToBytes(DateUtil.getDatetime2());
	    return res;
	}
	
	/**
	 * 盛宏时间
	 * @param msg
	 * @param begain
	 * @return
	 * @throws ParseException 
	 */
	public static String getSHBCDTime(byte[] msg, int begain)  {
        byte[] res = new byte[7];
        System.arraycopy(msg, begain, res, 0, 7);
        
        String time = BytesUtil.bytesToHexString(res);
        
        Date date = DateUtil.getDateFromString(time, DateUtil.FORMAT_TRADETIME);
        time = DateUtil.getStringFromDate(date, DateUtil.FORMAT_DATETIME);
        
        return time;
    }

	/**
	 * 硕维时间
	 * 将16进制string 转换为时间日期 </br>
	 * 00 10 08 09 03 0D (秒 分 时 日 月 年-2000) --> 2013‐03‐09 08:16:00
	 * 
	 * @param hex
	 *            hex string
	 * @return
	 */
	public static String parseSWTime(String hex) {
		if ( CommonUtil.isEmpty(hex)) {
			return null;
		}
		StringBuilder sBuilder = new StringBuilder();
		String str;
		int a ;
		for (int i = 10; i >= 0; i-=2) {
			//年
			str = hex.substring(i, i+2);
			a = Integer.parseInt(str, 16);
			if (a < 10 && i!=10 ) {
				sBuilder.append(0);
			}
			switch (i) {
			case 10: //年
				sBuilder.append(a + START_YEAR).append("-");
				break;
			case 8:
				sBuilder.append(a).append("-");
				break;
			case 6:
				sBuilder.append(a).append(" ");
				break;
			case 4:
				sBuilder.append(a ).append(":");
				break;
			case 2:
				sBuilder.append(a ).append(":");
				break;
			case 0:
				sBuilder.append(a );
				break;

			}
		}
		
		
		return sBuilder.toString();
	}

	

}
