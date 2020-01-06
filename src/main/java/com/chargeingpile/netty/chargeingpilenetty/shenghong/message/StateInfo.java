package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 盛宏充电状态信息，数据已解析
 * @author cj
 *
 */
public class StateInfo {
	private int id;
	private String zhuangId;//3
	private int gunNum;//4
	private int gun;//5
	private int gunType;//6
	private int state;//7
	private String soc;//8
	private int alarm;//9
	private int conn;//10
	private double allMoney;//11
//	private String para2;//12
//	private String para3;//13
	private double direV;//14
	private double direI;//15
	private double bmsV;//16
	private double bmsI;//17
	private int bmsPatt;//18
	private double aV;//19
	private double bV;//20
	private double cV;//21
	private double aI;//22
	private double bI;//23
	private double cI;//24
	private int remTim;//25
	private int charTim;//26
	private double elecQua;//27
	private double staRead;//28
	private double endRead;//29
	private int staType;//30
	private int charStra;//31
	private float charStraPara;//32
	private int appoMark;//33
	private String card;//34
	private int timOut;//35
	private String starTim;//36
	private double balance;//37
//	private String yuLiu;//38
	private double power;//39
//	private String param3;//40
//	private String param4;//41
//	private String param5;//42
	private int alarmTyp;//9
	
	
	public static StateInfo getIns(byte[] m) {
		PileStateInfo info = PileStateInfo.getStateInfo(m);

		String[] s = BytesUtil.bytesToHexStrings(m);
		
		String zhuangId = SHUtils.getPileNum(m);
		
		int gunNum = Integer.parseInt(s[44], 16);// 4
		int gun = Integer.parseInt(s[45], 16);// 5
		int gunType = Integer.parseInt(s[46], 16);// 6
		int state = Integer.parseInt(s[47], 16);// 7
		String soc = String.valueOf(Integer.parseInt(s[48], 16));// 8

		int alarm = BytesUtil.toInt4(info.getAlarm());// 9
		int conn = Integer.parseInt(s[53], 16);// 10
		double allMoney = BytesUtil.toInt4(info.getCurMoney()) * 0.01;// 11
		// String para2;//12
		// String para3;//13
		double direV = BytesUtil.toInt2(info.getDC_voltage() ) * 0.1;// 14
		double direI = BytesUtil.toInt2(info.getDC_current()) * 0.1;// 15
		double bmsV = BytesUtil.toInt2(info.getBMS_voltage()) * 0.1;// 16
		double bmsI = BytesUtil.toInt2(info.getBMS_current()) * 0.1;// 17
		int bmsPatt = Integer.parseInt(s[74], 16);// 18
		double aV = BytesUtil.toInt2(info.getA_voltage()) * 0.1;// 19
		double bV = BytesUtil.toInt2(info.getB_voltage()) * 0.1;// 20
		double cV = BytesUtil.toInt2(info.getC_voltage()) * 0.1;// 21
		double aI = BytesUtil.toInt2(info.getA_current()) * 0.1;// 22
		double bI = BytesUtil.toInt2(info.getB_current()) * 0.1;// 23
		double cI = BytesUtil.toInt2(info.getC_current()) * 0.1;// 24
		// s
		int remTim = BytesUtil.toInt2(info.getSurplus_charge_time()) * 60;// 25
		// s
		int charTim = BytesUtil.toInt4(info.getChargeDuration());// 26
		double elecQua = BytesUtil.toInt4(info.getAccumulativeEle()) * 0.01;// 27
		double staRead = BytesUtil.toInt4(info.getCharge_read_before()) * 0.01;// 28
		double endRead = BytesUtil.toInt4(info.getCharge_read()) * 0.01;// 29
		int staType = Integer.parseInt(s[105], 16);// 30
		
		int charStra = Integer.parseInt(s[106], 16);// 31
		// TODO
		float charStraPara = BytesUtil.toInt4(info.getChargeTypeParam());// 32
		switch (charStra) { // 充电策略
		case 1: // 时间 单位秒
			break;
		case 2:// 金额 0.01元
		case 3:// 电量 0.01kw
		    charStraPara *= 0.01;
			break;
		}

		int appoMark = Integer.parseInt(s[111], 16);// 33
		String card = ASCIIUtil.ASCII2Int(m, 112, 144);// 34
		int timOut = Integer.parseInt(s[144], 16);// 35
		// TODO
		String starTim = BytesUtil.bytesToHexString(info.getStartTime());// 36
		starTim = starTim.substring(0, 2) + starTim.substring(2, 4) + "-" + starTim.substring(4, 6) + "-"
				+ starTim.substring(6, 8) + " " + starTim.substring(8, 10) + ":" + starTim.substring(10, 12) + ":"
				+ starTim.substring(12, 14);

		double balance = BytesUtil.toInt4(info.getCardBalance());// 37
		// String yuLiu;//38
		double power = BytesUtil.toInt4(info.getCharge_power());// 39
		// String param3;//40
		// String param4;//41
		// String param5;//42

		StateInfo stateInfo = new StateInfo(zhuangId, gunNum, gun, gunType, state, soc, alarm, conn, allMoney, direV,
				direI, bmsV, bmsI, bmsPatt, aV, bV, cV, aI, bI, cI, remTim, charTim, elecQua, staRead, endRead, staType,
				charStra, charStraPara, appoMark, card, timOut, starTim, balance, power);
		return stateInfo;
	}
	
	public StateInfo() {
		super();
	}

	public StateInfo(String zhuangId, int gunNum, int gun, int gunType,
			int state, String soc, int alarm, int conn, double allMoney,
			double direV, double direI, double bmsV, double bmsI, int bmsPatt,
			double aV, double bV, double cV, double aI, double bI, double cI,
			int remTim, int charTim, double elecQua, double staRead,
			double endRead, int staType, int charStra, float charStraPara,
			int appoMark, String card, int timOut, String starTim,
			double balance, double power) {
		super();
		this.zhuangId = zhuangId;
		this.gunNum = gunNum;
		this.gun = gun;
		this.gunType = gunType;
		this.state = state;
		this.soc = soc;
		this.alarm = alarm;
		this.conn = conn;
		this.allMoney = allMoney;
		this.direV = direV;
		this.direI = direI;
		this.bmsV = bmsV;
		this.bmsI = bmsI;
		this.bmsPatt = bmsPatt;
		this.aV = aV;
		this.bV = bV;
		this.cV = cV;
		this.aI = aI;
		this.bI = bI;
		this.cI = cI;
		this.remTim = remTim;
		this.charTim = charTim;
		this.elecQua = elecQua;
		this.staRead = staRead;
		this.endRead = endRead;
		this.staType = staType;
		this.charStra = charStra;
		this.charStraPara = charStraPara;
		this.appoMark = appoMark;
		this.card = card;
		this.timOut = timOut;
		this.starTim = starTim;
		this.balance = balance;
		this.power = power;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZhuangId() {
		return zhuangId;
	}

	public void setZhuangId(String zhuangId) {
		this.zhuangId = zhuangId;
	}

	public int getGunNum() {
		return gunNum;
	}

	public void setGunNum(int gunNum) {
		this.gunNum = gunNum;
	}

	public int getGun() {
		return gun;
	}

	public void setGun(int gun) {
		this.gun = gun;
	}

	public int getGunType() {
		return gunType;
	}

	public void setGunType(int gunType) {
		this.gunType = gunType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSoc() {
		return soc;
	}

	public void setSoc(String soc) {
		this.soc = soc;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public int getConn() {
		return conn;
	}

	public void setConn(int conn) {
		this.conn = conn;
	}

	public double getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(double allMoney) {
		this.allMoney = allMoney;
	}

	public double getDireV() {
		return direV;
	}

	public void setDireV(double direV) {
		this.direV = direV;
	}

	public double getDireI() {
		return direI;
	}

	public void setDireI(double direI) {
		this.direI = direI;
	}

	public double getBmsV() {
		return bmsV;
	}

	public void setBmsV(double bmsV) {
		this.bmsV = bmsV;
	}

	public double getBmsI() {
		return bmsI;
	}

	public void setBmsI(double bmsI) {
		this.bmsI = bmsI;
	}

	public int getBmsPatt() {
		return bmsPatt;
	}

	public void setBmsPatt(int bmsPatt) {
		this.bmsPatt = bmsPatt;
	}

	public double getaV() {
		return aV;
	}

	public void setaV(double aV) {
		this.aV = aV;
	}

	public double getbV() {
		return bV;
	}

	public void setbV(double bV) {
		this.bV = bV;
	}

	public double getcV() {
		return cV;
	}

	public void setcV(double cV) {
		this.cV = cV;
	}

	public double getaI() {
		return aI;
	}

	public void setaI(double aI) {
		this.aI = aI;
	}

	public double getbI() {
		return bI;
	}

	public void setbI(double bI) {
		this.bI = bI;
	}

	public double getcI() {
		return cI;
	}

	public void setcI(double cI) {
		this.cI = cI;
	}

	public int getRemTim() {
		return remTim;
	}

	public void setRemTim(int remTim) {
		this.remTim = remTim;
	}

	public int getCharTim() {
		return charTim;
	}

	public void setCharTim(int charTim) {
		this.charTim = charTim;
	}

	public double getElecQua() {
		return elecQua;
	}

	public void setElecQua(double elecQua) {
		this.elecQua = elecQua;
	}

	public double getStaRead() {
		return staRead;
	}

	public void setStaRead(double staRead) {
		this.staRead = staRead;
	}

	public double getEndRead() {
		return endRead;
	}

	public void setEndRead(double endRead) {
		this.endRead = endRead;
	}

	public int getStaType() {
		return staType;
	}

	public void setStaType(int staType) {
		this.staType = staType;
	}

	public int getCharStra() {
		return charStra;
	}

	public void setCharStra(int charStra) {
		this.charStra = charStra;
	}


	public int getAppoMark() {
		return appoMark;
	}

	public void setAppoMark(int appoMark) {
		this.appoMark = appoMark;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public int getTimOut() {
		return timOut;
	}

	public void setTimOut(int timOut) {
		this.timOut = timOut;
	}

	public String getStarTim() {
		return starTim;
	}

	public void setStarTim(String starTim) {
		this.starTim = starTim;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public int getAlarmTyp() {
		return alarmTyp;
	}

	public void setAlarmTyp(int alarmTyp) {
		this.alarmTyp = alarmTyp;
	}

    public float getCharStraPara() {
        return charStraPara;
    }

    public void setCharStraPara(float charStraPara) {
        this.charStraPara = charStraPara;
    }

}
