package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;

/**
 * 预约/即时充电
 * @author cj
 *
 */
public class StartCharger extends Message{
	
	public static final String ORDER  = "02000000";
	public static final String CHARGE = "00000000";
	
	private String yuliu1;
	private String yuliu2;
	private String gun;//充电枪口（1字节）
	private String charType;//充电类型（4字节）
	private String yuliu3;
	private String charStra;//充电策略（4字节）
	private String charStraPara;//充电策略参数（4字节）
	private String time;//预约或定时启动时间（8字节）
	private String timeOut;//预约超时时间（1字节）
	private String card;//卡号（32字节）
	private String mark;//断网充电标志（1字节）：0不允许，1允许
	private String offElec;//离线可充电电量（4字节）
		
	public StartCharger() {
		super();
		setM_cmd(ShengHong.STAR_SLAVE);
		setYuliu1("0000");
		setYuliu2("0000");
		setYuliu3("00000000");
		setGun("00");
		setCharType("00000000");//默认即时充电		
		setCharStra("00000000");//默认充满为止
		setCharStraPara("00000000");
		setTime("0000000000000000");
		setTimeOut("01");
//		setCard(ASCIIUtil.ASCII2HexString("11010001668019601101000166801960"));
		setMark("00");//默认断网不能充电
		setOffElec("00000000");
	}




	public StartCharger(String gun, String charType, String charStra,
			String charStraPara, String time, String timeOut, String card,
			String mark, String offElec) {
		this();
		this.gun = gun;
		this.charType = charType;
		this.charStra = charStra;
		this.charStraPara = charStraPara;
		this.time = time;
		this.timeOut = timeOut;
		this.card = card;
		this.mark = mark;
		this.offElec = offElec;
	}

	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(gun)
		.append(charType).append(yuliu3).append(charStra)
		.append(charStraPara).append(time).append(timeOut)
		.append(card).append(mark).append(offElec);	
	}
	

	public String getGun() {
		return gun;
	}


	public void setGun(String gun) {
		this.gun = gun;
	}


	public String getCharType() {
		return charType;
	}


	public void setCharType(String charType) {
		this.charType = charType;
	}


	public String getCharStra() {
		return charStra;
	}


	public void setCharStra(String charStra) {
		this.charStra = charStra;
	}


	public String getCharStraPara() {
		return charStraPara;
	}


	public void setCharStraPara(String charStraPara) {
		this.charStraPara = charStraPara;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}


	public String getCard() {
		return card;
	}


	public void setCard(String card) {
		this.card = card;
	}


	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	public String getOffElec() {
		return offElec;
	}


	public void setOffElec(String offElec) {
		this.offElec = offElec;
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


	public String getYuliu3() {
		return yuliu3;
	}


	public void setYuliu3(String yuliu3) {
		this.yuliu3 = yuliu3;
	}




	@Override
	public String toString() {
		return "StartCharger [yuliu1=" + yuliu1 + ", yuliu2=" + yuliu2 + ", gun=" + gun + ", charType=" + charType
				+ ", yuliu3=" + yuliu3 + ", charStra=" + charStra + ", charStraPara=" + charStraPara + ", time=" + time
				+ ", timeOut=" + timeOut + ", card=" + card + ", mark=" + mark + ", offElec=" + offElec + "]";
	}
	
	

}
