package com.chargeingpile.netty.chargeingpilenetty.shenghong;

public class ShConfig {

	public static String HOST = "127.0.0.1";
	// public static String HOST = "192.168.253.1";
	// public static String HOST = "0.0.0.0.0";
	// public static String HOST = "192.168.253.1";
	public static int PORT = 9999;// 7878;

	public static String SEPARATOR = "\n";
	/**
	 * 空格
	 */
	public static final String SEPARATOR_SPACE = " ";

	public static String HEART_BEAT_REQ = "ping" + SEPARATOR;
	public static String HEART_BEAT_RESP = "pong" + SEPARATOR;

	// 超时时间
	public static final int READ_TIME_OUT   = 60;
	public static final int WRITER_TIME_OUT = 60;
	public static final int ALL_TIME_OUT    = 60;

}
