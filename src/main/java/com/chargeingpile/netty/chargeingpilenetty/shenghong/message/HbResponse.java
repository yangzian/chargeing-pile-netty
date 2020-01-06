package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 心跳响应报文
 * @author cj
 *
 */
public class HbResponse extends Message {
	
	private String yuliu1;
	private String yuliu2;
	private String heart;
	
	
	public HbResponse() {
		super();
		yuliu1 = "0000";
		yuliu2 = "0000";
	}


	public HbResponse(int sequence, int heart) {
		this();
		setM_cmd(ShengHong.HB_SLAVE);
		this.heart = BytesUtil.intToHexString(heart);
	}


	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(heart);		
	}
	

}
