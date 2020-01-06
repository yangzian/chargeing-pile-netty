package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 停止充电，控制命令
 * @author cj
 *
 */
public class StopCharger extends Message{
	
	
	private static final long serialVersionUID = 1L;
	
	public static final String ADDR_ORDER  = "0A000000";
	public static final String ADDR_CHARGE = "02000000";
	
	private String yuliu1 = "0000";//(2字节)
	private String yuliu2 = "0000";//(2字节)
	/**
	 * 只有一机一桩可为0
	 */
	private String gun = "00";//充电枪口（1字节）
	/**
	 * 起始地址    (4字节)
	 */
	private String addr;
	private String num;//命令个数（1字节）
	private String paraLen = "0400";
	private String para;//命令参数（n字节）
	
	public StopCharger() {
		super();
		setM_cmd(ShengHong.STOP_SLAVE);
		setNum("01");
		setParaLen("0400");
		setPara("55000000");
	}
	
	/**
	 * @param  gun：充电枪口     addr：命令起始标志  num：命令个数  para：命令参数
	 * @author pwt
	 */
	public StopCharger(int gun, int addr, int num, String para) {
		this();
		this.gun = BytesUtil.int2HexString(gun);
		this.addr = BytesUtil.bytesToHexString(BytesUtil.intToBytes(addr));
		this.num = BytesUtil.int2HexString(num);
		this.para = para;
	}
	
	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(gun).append(addr)
		  .append(num).append(paraLen).append(para);
		
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

	public String getGun() {
		return gun;
	}

	public void setGun(String gun) {
		this.gun = gun;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getParaLen() {
		return paraLen;
	}

	public void setParaLen(String paraLen) {
		this.paraLen = paraLen;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
	
	

}
