package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 服务器应答充电桩上报充电信息报文 
 * @author cj
 *
 */
public class CharInfoResponse extends Message{
	
	private String yuliu1 = "0000";//(2字节)
	private String yuliu2 = "0000";//(2字节)
	private String gun = "00";//充电枪口（1字节）
	private String card;//卡号（32字节）
	
	public CharInfoResponse() {
		super();
		setM_cmd(ShengHong.CHARGEINFO_RES);
	}

	/**
	 * @param gun：充电枪口     card：卡号
	 * @author pwt
	 */
	public CharInfoResponse(String gun, String card) {
		this();
		this.gun = gun;
		this.card = card;
	}
	
	
	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(gun).append(card);
	}
	

}
