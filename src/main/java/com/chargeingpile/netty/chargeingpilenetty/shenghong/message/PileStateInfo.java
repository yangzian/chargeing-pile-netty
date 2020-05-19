package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import java.util.Arrays;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

/**
 * 充电桩状态信息上报数据 ， 字段为 byte[] 类型
 * 
 * @author cj
 *
 */
public class PileStateInfo {

	/**
	 * 桩 ID， ASSIC 编码
	 */
	private byte[] zhuangId = new byte[32];

	/**
	 * 充电枪数量
	 */
	private byte gunCount;
	/**
	 * 充电口号 , 编码从 1 开始
	 */
	private byte gun;

	/**
	 * 充电枪类型 1=直流； 2=交流；
	 */
	private byte gunType;

	/**
	 * 工作状态 </br>
	 * 0‐空闲中 1‐正准备开始充电 2‐充电进行中 3‐充电结束 4‐启动失败 5‐预约状 态 6‐系统故障(不能给汽车充 电)
	 */
	private byte workState;

	/**
	 * 当前SCO %</br>
	 * (直流有效，交流无效)
	 */
	private byte SOC;

	/**
	 * 告警状态
	 */
	private byte[] alarm = new byte[4];

	/**
	 * 车连接状态 0‐ 断开 1‐半连接 2‐连接 直流 目前只有 0 和 2 状态 交流目前有 0、1、2 三种状 态 只有状态不为 0 时，手机才
	 * 能下发开机指令
	 */
	private byte carConnState;

	/**
	 * 本次充电累计充电费用 从本次充电开始到目 前的累计充电费用 （包括电费与服务 费） ，这里是整型， 要乘以 0.01 才能得 到真实的金额
	 */
	private byte[] curMoney = new byte[4];

	/**
	 * 直流充电电压  0.1V/bit  
	 * 充电有效（直流有效，交流置 0）
	 */
	private byte[] DC_voltage = new byte[2];
	/**
	 * 直流充电电流  0.1A/bit  
	 * 充电有效（直流有效，交流置 0）
	 */
	private byte[] DC_current = new byte[2];
	/**
	 * BMS 需求电压 
	 * 充电有效（直流有效，交流置 0）
	 */
	private byte[] BMS_voltage = new byte[2];
	/**
	 * BMS 需求电流 
	 * 充电有效（直流有效，交流置 0）
	 */
	private byte[] BMS_current = new byte[2];
	/**
	 * BMS 充电模式 
	 */
	private byte BMS_type;
	
	
	/**
	 * 交流 A 相充电电压
	 * 直流桩表示三相输入电压 
	 */
	private byte[] A_voltage = new byte[2];
	/**
	 * 交流 B 相充电电压
	 */
	private byte[] B_voltage = new byte[2];
	/**
	 * 交流 C 相充电电压
	 * 交流桩状态为充电时才有效 
	 */
	private byte[] C_voltage = new byte[2];
	/**
	 * 交流 A 相充电电流
	 * 直流桩表示三相输入电压 
	 */
	private byte[] A_current = new byte[2];
	/**
	 * 交流 B 相充电电流
	 */
	private byte[] B_current = new byte[2];
	/**
	 * 交流 C 相充电电流
	 * 交流桩状态为充电时才有效 
	 */
	private byte[] C_current = new byte[2];
	
	/**
	 * 剩余充电时间(min) 
	 * 充电有效（直流有效，交流无效）
	 */
	private byte[] surplus_charge_time = new byte[2];
	

	/**
	 * 充电时长(秒)
	 */
	private byte[] chargeDuration = new byte[4];
	private String chargeDurationStr;
	private int chargeDurationInt;

	/**
	 * 本次充电累计充电电量（0.01kwh）
	 */
	private byte[] accumulativeEle = new byte[4];
	
	/**
	 * 充电前电表读数 
	 */
	private byte[] charge_read_before = new byte[4];

	/**
	 * 当前电表读数 
	 */
	private byte[] charge_read= new byte[4];

	
	/**
	 * 充电启动方式 
	 * 0：本地刷卡启动 1：后台启动 2：本地管理员启动
	 */
	private byte chargeStartType;
	
	/**
	 * 充电策略 
	 * 0 自动充满 1 按时间充满 2 定金额 3 按电量充满
	 */
	private byte chargeType;

	/**
	 * 充电策略参数 
	 * 时间单位为 1 秒 金额单位为 0.01 元 电量时单位为 0.01kw
	 * 
	 */
	private byte[] chargeTypeParam = new byte[4];

	/**
	 * 预约标志 0‐无预约（无效） 1‐预约有效
	 */
	private byte orderFlag;

	/**
	 * 充电/预约卡号
	 */
	private byte[] cardID = new byte[32];

	private String carIdStr;

	/**
	 * 预约/开始充电 开始时间
	 */
	private byte[] startTime = new byte[8];
	private String staTime;

	/**
	 * 充电前卡余额
	 */
	private byte[] cardBalance = new byte[4];
	
	/**
	 * 充电功率
	 * 0.1kw
	 */
	private byte[] charge_power = new byte[4];

	public PileStateInfo() {
	}

	/**
	 * 将桩上传的信息转化为实体
	 * 
	 * @param msg
	 * @return
	 */
	public static PileStateInfo getStateInfo(byte[] msg) {
		if (msg == null || msg.length == 0) {
			return null;
		}
		PileStateInfo info = new PileStateInfo();
		byte[] data = info.getZhuangId();
		System.arraycopy(msg, 12, data, 0, data.length);
		info.setGunCount(msg[44]);
		info.setGun(msg[45]);
		info.setGunType(msg[46]);
		info.setWorkState(msg[47]);
		info.setSOC(msg[48]);
		data = info.getAlarm();
		System.arraycopy(msg, 49, data, 0, data.length);

		info.setCarConnState(msg[53]);
		data = info.getCurMoney();
		System.arraycopy(msg, 54, data, 0, data.length);
		//电压 电流
		data = info.getDC_voltage();
		System.arraycopy(msg, 66, data, 0, data.length);
		data = info.getDC_current();
		System.arraycopy(msg, 68, data, 0, data.length);
		data = info.getBMS_voltage();
		System.arraycopy(msg, 70, data, 0, data.length);
		data = info.getBMS_current();
		System.arraycopy(msg, 72, data, 0, data.length);
		info.setBMS_type(msg[74]);
		// 交流
		data = info.getA_voltage();
		System.arraycopy(msg, 75, data, 0, data.length);
		data = info.getB_voltage();
		System.arraycopy(msg, 77, data, 0, data.length);
		data = info.getC_voltage();
		System.arraycopy(msg, 79, data, 0, data.length);
		data = info.getA_current();
		System.arraycopy(msg, 81, data, 0, data.length);
		data = info.getB_current();
		System.arraycopy(msg, 83, data, 0, data.length);
		data = info.getC_current();
		System.arraycopy(msg, 85, data, 0, data.length);
		//
		data = info.getSurplus_charge_time();
		System.arraycopy(msg, 87, data, 0, data.length);
		
		// 充电时长
		data = info.getChargeDuration();
		System.arraycopy(msg, 89, data, 0, data.length);
		data = info.getAccumulativeEle();
		System.arraycopy(msg, 93, data, 0, data.length);
		
		data = info.getCharge_read_before();
		System.arraycopy(msg, 97, data, 0, data.length);
		data = info.getCharge_read();
		System.arraycopy(msg, 101, data, 0, data.length);
		
		info.setChargeStartType(msg[105]);
		// 充电策略
		info.setChargeType(msg[106]);
		data = info.getChargeTypeParam();
		System.arraycopy(msg, 107, data, 0, data.length);
		
		info.setOrderFlag(msg[111]);
		// 充电/预约卡号
		data = info.getCardID();
		if (msg.length > 112){

			System.arraycopy(msg, 112, data, 0, data.length);
		}

		data = info.getStartTime();
		System.out.println("=======msg==============="+ BytesUtil.bytesToHexString(msg));
		System.out.println("========msgLength========"+msg.length);
		System.out.println("=======data==============="+BytesUtil.bytesToHexString(data));
		if (msg.length > 145){

			System.arraycopy(msg, 145, data, 0, data.length);
		}

		data = info.getCardBalance();
		if (msg.length > 153){

			System.arraycopy(msg, 153, data, 0, data.length);
		}


		if(msg.length > 161){
			
			data = info.getCharge_power();
			System.arraycopy(msg, 161, data, 0, data.length);
		}

		return info;
	}

	public byte[] getZhuangId() {
		return zhuangId;
	}

	public void setZhuangId(byte[] zhuangId) {
		this.zhuangId = zhuangId;
	}

	public byte getGunCount() {
		return gunCount;
	}

	public void setGunCount(byte gunCount) {
		this.gunCount = gunCount;
	}

	public byte getGun() {
		return gun;
	}

	public void setGun(byte gun) {
		this.gun = gun;
	}

	public byte getGunType() {
		return gunType;
	}

	public void setGunType(byte gunType) {
		this.gunType = gunType;
	}

	public byte getWorkState() {
		return workState;
	}

	public void setWorkState(byte workState) {
		this.workState = workState;
	}

	public byte getCarConnState() {
		return carConnState;
	}

	public void setCarConnState(byte carConnState) {
		this.carConnState = carConnState;
	}

	public byte[] getCurMoney() {
		return curMoney;
	}

	public void setCurMoney(byte[] curMoney) {
		this.curMoney = curMoney;
	}

	public byte[] getChargeDuration() {
		return chargeDuration;
	}

	public void setChargeDuration(byte[] chargeDuration) {
		this.chargeDuration = chargeDuration;
	}

	public byte[] getAccumulativeEle() {
		return accumulativeEle;
	}

	public void setAccumulativeEle(byte[] accumulativeEle) {
		this.accumulativeEle = accumulativeEle;
	}

	public byte getChargeType() {
		return chargeType;
	}

	public void setChargeType(byte chargeType) {
		this.chargeType = chargeType;
	}

	public byte getChargeStartType() {
		return chargeStartType;
	}

	public void setChargeStartType(byte chargeStartType) {
		this.chargeStartType = chargeStartType;
	}

	public byte[] getChargeTypeParam() {
		return chargeTypeParam;
	}

	public void setChargeTypeParam(byte[] chargeTypeParam) {
		this.chargeTypeParam = chargeTypeParam;
	}

	public byte getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(byte orderFlag) {
		this.orderFlag = orderFlag;
	}

	public byte[] getCardID() {
		return cardID;
	}

	public void setCardID(byte[] cardID) {
		this.cardID = cardID;
	}

	public byte[] getStartTime() {
		return startTime;
	}

	public void setStartTime(byte[] startTime) {
		this.startTime = startTime;
	}

	public byte[] getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(byte[] cardBalance) {
		this.cardBalance = cardBalance;
	}

	@Override
    public String toString() {
        return "PileStateInfo [zhuangId=" + Arrays.toString(zhuangId) + ", gunCount=" + gunCount + ", gun="
                    + gun + ", gunType=" + gunType + ", workState=" + workState + ", SOC=" + SOC + ", alarm="
                    + Arrays.toString(alarm) + ", carConnState=" + carConnState + ", curMoney="
                    + Arrays.toString(curMoney) + ", DC_voltage=" + Arrays.toString(DC_voltage)
                    + ", DC_current=" + Arrays.toString(DC_current) + ", BMS_voltage="
                    + Arrays.toString(BMS_voltage) + ", BMS_current=" + Arrays.toString(BMS_current)
                    + ", BMS_type=" + BMS_type + ", A_voltage=" + Arrays.toString(A_voltage) + ", B_voltage="
                    + Arrays.toString(B_voltage) + ", C_voltage=" + Arrays.toString(C_voltage)
                    + ", A_current=" + Arrays.toString(A_current) + ", B_current="
                    + Arrays.toString(B_current) + ", C_current=" + Arrays.toString(C_current)
                    + ", surplus_charge_time=" + Arrays.toString(surplus_charge_time) + ", chargeDuration="
                    + Arrays.toString(chargeDuration) + ", accumulativeEle="
                    + Arrays.toString(accumulativeEle) + ", charge_read_before="
                    + Arrays.toString(charge_read_before) + ", charge_read=" + Arrays.toString(charge_read)
                    + ", chargeStartType=" + chargeStartType + ", chargeType=" + chargeType
                    + ", chargeTypeParam=" + Arrays.toString(chargeTypeParam) + ", orderFlag=" + orderFlag
                    + ", cardID=" + Arrays.toString(cardID) + ", startTime=" + Arrays.toString(startTime)
                    + ", cardBalance=" + Arrays.toString(cardBalance) + ", charge_power="
                    + Arrays.toString(charge_power) + "]";
    }

	public byte getSOC() {
		return SOC;
	}

	public void setSOC(byte sOC) {
		SOC = sOC;
	}

	public byte[] getAlarm() {
		return alarm;
	}

	public void setAlarm(byte[] alarm) {
		this.alarm = alarm;
	}

	public byte[] getDC_voltage() {
		return DC_voltage;
	}

	public void setDC_voltage(byte[] dC_voltage) {
		DC_voltage = dC_voltage;
	}

	public byte[] getDC_current() {
		return DC_current;
	}

	public void setDC_current(byte[] dC_current) {
		DC_current = dC_current;
	}

	public byte[] getBMS_voltage() {
		return BMS_voltage;
	}

	public void setBMS_voltage(byte[] bMS_voltage) {
		BMS_voltage = bMS_voltage;
	}

	public byte[] getBMS_current() {
		return BMS_current;
	}

	public void setBMS_current(byte[] bMS_current) {
		BMS_current = bMS_current;
	}

	public byte getBMS_type() {
		return BMS_type;
	}

	public void setBMS_type(byte bMS_type) {
		BMS_type = bMS_type;
	}

	public byte[] getA_voltage() {
		return A_voltage;
	}

	public void setA_voltage(byte[] a_voltage) {
		A_voltage = a_voltage;
	}

	public byte[] getB_voltage() {
		return B_voltage;
	}

	public void setB_voltage(byte[] b_voltage) {
		B_voltage = b_voltage;
	}

	public byte[] getC_voltage() {
		return C_voltage;
	}

	public void setC_voltage(byte[] c_voltage) {
		C_voltage = c_voltage;
	}

	public byte[] getA_current() {
		return A_current;
	}

	public void setA_current(byte[] a_current) {
		A_current = a_current;
	}

	public byte[] getB_current() {
		return B_current;
	}

	public void setB_current(byte[] b_current) {
		B_current = b_current;
	}

	public byte[] getC_current() {
		return C_current;
	}

	public void setC_current(byte[] c_current) {
		C_current = c_current;
	}

	public byte[] getSurplus_charge_time() {
		return surplus_charge_time;
	}

	public void setSurplus_charge_time(byte[] surplus_charge_time) {
		this.surplus_charge_time = surplus_charge_time;
	}

	public byte[] getCharge_power() {
		return charge_power;
	}

	public void setCharge_power(byte[] charge_power) {
		this.charge_power = charge_power;
	}

	public byte[] getCharge_read_before() {
		return charge_read_before;
	}

	public void setCharge_read_before(byte[] charge_read_before) {
		this.charge_read_before = charge_read_before;
	}

	public byte[] getCharge_read() {
		return charge_read;
	}

	public void setCharge_read(byte[] charge_read) {
		this.charge_read = charge_read;
	}

	public String getCarIdStr() {
		return carIdStr;
	}

	public void setCarIdStr(String carIdStr) {
		this.carIdStr = carIdStr;
	}


	public String getChargeDurationStr() {
		return chargeDurationStr;
	}

	public void setChargeDurationStr(String chargeDurationStr) {
		this.chargeDurationStr = chargeDurationStr;
	}

	public String getStaTime() {
		return staTime;
	}

	public void setStaTime(String staTime) {
		this.staTime = staTime;
	}


	public int getChargeDurationInt() {
		return chargeDurationInt;
	}

	public void setChargeDurationInt(int chargeDurationInt) {
		this.chargeDurationInt = chargeDurationInt;
	}

	@Test
	public void demo(){
		//String msg = "aaf5b500109a6800000000000000000000000000000000000000000000000073000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		String msg = "aaf5b500109a";

		byte[] b = BytesUtil.hexStringToBytes(msg);

		byte[] data = new byte[1];

		System.out.println("b====="+b.length);

		System.arraycopy(b, 2, data, 0, data.length);


		System.out.println("data====="+BytesUtil.bytesToHexString(data));

		//char[] a= {'a','b','c','d','e','f'};
		//char[]	b=new char[3];


			//src表示源数组，srcPos表示源数组要复制的起始位置，desc表示目标数组，length表示要复制的长度。
		//System.arraycopy(a, 3, b, 0, b.length);
		//System.out.println(b);





	}
}
