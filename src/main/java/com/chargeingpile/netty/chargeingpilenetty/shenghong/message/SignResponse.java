package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;




/**
 * 响应签到
 */
public class SignResponse extends Message {
	
	private String yuliu1;
	private String yuliu2;
	
	
	public SignResponse() {
		super();
		yuliu1 = "0000";
		yuliu2 = "0000";
		setM_cmd(ShengHong.SIGN_RESP);
	}

	

	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2);
		
	}



}
