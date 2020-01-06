package com.chargeingpile.netty.chargeingpilenetty.util;

import java.net.InetSocketAddress;
import java.util.Calendar;


import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import io.netty.channel.ChannelHandlerContext;

public class CommonUtil {
    
    
	public static int START_YEAR = 2000;


	public static String getIP(ChannelHandlerContext ctx) {
		if (ctx == null) {
			return null;
		}
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = address.getAddress().getHostAddress();
		return ip;
	}

	/**
	 * 将ip转换成16进制string
	 * 
	 * @param ip
	 * @return
	 */
	public static String ipToHexString(String ip) {
		if (CommonUtil.isEmpty(ip)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String[] strings = ip.split(".");
		int src = 0;
		for (int i = 0; i < strings.length; i++) {
			src = Integer.parseInt(strings[i]);
			sb.append(BytesUtil.int2HexString(src));
		}

		return sb.toString();
	}

	/**
	 * 计算校验位
	 * 
	 * @param hex
	 *            生成校验位的数据,空格分隔
	 * @return 16进制的string
	 */
	public static String calculateCheckbit(String hex) {
		int total = 0;
		String[] strings = hex.split(" ");
		for (int i = 0; i < strings.length; i++) {
			total += Integer.parseInt(strings[i], 16);
		}
		/**
		 * 用256求余最大是255，即16进制的FF
		 */
		int mod = total % 256;
		String h = Integer.toHexString(mod);
		int len = h.length();
		// 如果不够校验位的长度，补0,这里用的是两位校验
		if (len < 2) {
			h = "0" + h;
		}
		return h;
	}

	/**
	 * 将16进制string 转换为时间日期 </br>
	 * 00 10 08 09 03 0D (秒 分 时 日 月 年+2000) --> 2013‐3‐9 8:16:00
	 * 
	 * @param hex
	 *            hex string
	 * @return
	 */
	public static Calendar getTime(String hex) {
		Calendar calendar = Calendar.getInstance();
		if (hex == null || hex.isEmpty()) {
			return null;
		}
		String[] res = hex.split(" ");
		int index = res.length - 1;
		for (int i = 0; i < res.length; i++) {
			String s = res[i];
			int a = Integer.parseInt(s, 16);
			if (i == index) {
				a += START_YEAR;
			}
			switch (i) {
			case 5:
				calendar.set(Calendar.YEAR, a);
				break;
			case 4:
				calendar.set(Calendar.MONTH, a);
				break;
			case 3:
				calendar.set(Calendar.DAY_OF_MONTH, a);
				break;
			case 2:
				calendar.set(Calendar.HOUR_OF_DAY, a);
				break;
			case 1:
				calendar.set(Calendar.MINUTE, a);
				break;
			case 0:
				calendar.set(Calendar.SECOND, a);
				break;

			default:
				calendar = null;
				break;
			}
		}

		return calendar;
	}

	/**
	 * 将日期 转换为16进制string </br>
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getHexTimeStr(Calendar calendar) {
		StringBuilder builder = new StringBuilder();
		if (calendar == null) {
			return null;
		}
		builder.append(BytesUtil.int2HexString(calendar.get(Calendar.SECOND)) + " ")
				.append(BytesUtil.int2HexString(calendar.get(Calendar.MINUTE)) + " ")
				.append(BytesUtil.int2HexString(calendar.get(Calendar.HOUR_OF_DAY)) + " ")
				.append(BytesUtil.int2HexString(calendar.get(Calendar.DAY_OF_MONTH)) + " ")
				.append(BytesUtil.int2HexString(calendar.get(Calendar.MONTH)) + " ")
				.append(BytesUtil.int2HexString(calendar.get(Calendar.YEAR) - START_YEAR));

		return builder.toString();
	}

	/**
	 * 将日期字符串 转换为BCD时间string </br>
	 * 如 2015－07－22－13－16－15， 为： 0x20 0x15 0x07 0x22 0x13 0x16 0x15 0xff
	 * 
	 * @param
	 * @return
	 */
	public static String setBCDTimeStr(String str) {
		StringBuilder builder = new StringBuilder();
		builder.append(str).append("ff");
		return builder.toString();
	}

	/**
	 * 将日期 转换为BCD时间string </br>
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getBCDTimeStr(Calendar calendar) {
		StringBuilder builder = new StringBuilder();
		if (calendar == null) {
			return null;
		}
		String year = calendar.get(Calendar.YEAR) + "";
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = calendar.get(Calendar.MINUTE) + "";
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = calendar.get(Calendar.SECOND) + "";
		if (second.length() == 1) {
			second = "0" + second;
		}
		builder.append(year).append(month).append(day).append(hour).append(minute).append(second).append("FF");
		return builder.toString();
	}

	/**
	 * 判断string是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

}
