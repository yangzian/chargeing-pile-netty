package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import java.util.ArrayList;
import java.util.List;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 告警信息
 * 
 * @author cj
 *
 */
public class AlarmInfo {

	private String yuliu1;
	private String yuliu2;
	private String pile_code;
	
	private int alarmType;

	/**
	 * 32字节的告警信息
	 */
	private List<String> alarms;

	/**
	 * 
	 * 
	 * @param msg
	 * @return
	 */
	public static AlarmInfo getIns(byte[] msg) {
		if (msg == null || msg.length == 0) {
			return null;
		}
		AlarmInfo info = new AlarmInfo();
		//桩编码
		String code = ASCIIUtil.ASCII2Int(msg, 12 , 44);
		
		info.setPile_code(code);

		//告警位,32个字节。 每一位代码一个告警，共可表示 256 个告警，具体含义待定义 （ 为服务器能了解桩的告警信息）
		byte[] bs = new byte[32];
		System.arraycopy(msg, 44, bs, 0, 32);
		
		List<String> alrams = new ArrayList<String>();
		for (int i = 0; i < bs.length; i++) {
			String val = BytesUtil.toBinary(bs[i]);
			alrams.add(val);
		}
		
		info.setAlarms(alrams);

		return info;
	}

	public String getYuliu1() {
		return yuliu1;
	}

	public void setYuliu1(String yuliu1) {
		this.yuliu1 = yuliu1;
	}

	public String getYuliu2() {
		return yuliu2;
	}

	public void setYuliu2(String yuliu2) {
		this.yuliu2 = yuliu2;
	}

	public String getPile_code() {
		return pile_code;
	}

	public void setPile_code(String pile_code) {
		this.pile_code = pile_code;
	}

	public List<String> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<String> alarms) {
		this.alarms = alarms;
	}

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

}
