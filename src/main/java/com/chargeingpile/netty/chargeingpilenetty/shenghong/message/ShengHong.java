package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

public class ShengHong {
	/**
	 * 起始域
	 */
	public static final String START = "AAF5";
	/**
	 * 版本域
	 */
	public static final String VERSION = "10";
	
	/**
	 * 充电桩签到 cmd=106
	 */
	public static final String SIGN = "6A00";
	/**
	 * 服务器应答签到 cmd=105
	 */
	public static final String SIGN_RESP = "6900";
	
	/**
	 * 服务器应答心跳包 cmd=101
	 */
	public static final String HB_SLAVE = "6500";
	/**
	 * 充电桩上传心跳包 cmd=102
	 */
	public static final String HB_MAST = "6600";
	
	/**
	 * 服务器应答状态信息包 cmd=103
	 */
	public static final String STATE_SLAVE = "6700";
	/**
	 * 充电桩上传状态信息包 cmd=104
	 */
	public static final String STATE_MAST = "6800";
	/**
	 * 充电桩告警信息上报 cmd=108
	 */
	public static final String ALREM_REQ  = "6C00";
	
	/**
	 * 服务器对充电桩进行参数设置/查询 cmd=1
	 */
	public static final String PARA_SLAVE = "0100";
	/**
	 * 充电桩应答整形参数设置/查询报文 cmd=2
	 */
	public static final String PARA_MAST = "0200";
	
	/**
	 * 服务器向充电桩下发停止充电命令 cmd=5
	 */
	public static final String STOP_SLAVE = "0500";
	/**
	 * 充电桩应答停止充电命令 cmd=6
	 */
	public static final String STOP_MAST = "0600";
	
	/**
	 * 服务器向充电桩下发充电命令 cmd=7
	 */
	public static final String STAR_SLAVE = "0700";
	/**
	 * 充电桩应答充电命令 cmd=8
	 */
	public static final String STAR_MAST = "0800";
	
	/**
	 * 服务器应答充电信息 cmd=201
	 */
	public static final String CHARGEINFO_RES = "C900";
	/**
	 * 充电桩上传充电信息 cmd=202
	 */
	public static final String CHARGEINFO_REQ = "CA00";

}
