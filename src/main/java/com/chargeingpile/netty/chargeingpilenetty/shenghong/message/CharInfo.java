package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

/**
 * 充电记录
 * 数据已解析
 * @author cj
 *
 */
public class CharInfo {
	
	private int id;
	private String zhuangId;//3
	private int gunType;//4
	private int gun;//5
	private String card;//6
	private String startTim;//7
	private String endTim;//8
	private int timeLen;//9
	private String startSOC;//10
	private String endSOC;//11
	private int reason;//12
	private double elecQua;//13
	private double staRead;//14
	private double endRead;//15
	private double money;//16
	private double staMoney;//18
//	private String recordId;//19
//	private int recordNum;//20
	private int charStra;//22
	private int charStraPara;//23
	private String carVIN;//24
	private double[] elecTim;
	private int StaMod;//74
	
	public CharInfo() {
		super();
	}

	public CharInfo(String zhuangId, int gunType, int gun, String card,
			String startTim, String endTim, int timeLen, String startSOC,
			String endSOC, int reason, double elecQua, double staRead,
			double endRead, double money, double staMoney, int charStra,
			int charStraPara, String carVIN, double[] elecTim, int staMod) {
		super();
		this.zhuangId = zhuangId;
		this.gunType = gunType;
		this.gun = gun;
		this.card = card;
		this.startTim = startTim;
		this.endTim = endTim;
		this.timeLen = timeLen;
		this.startSOC = startSOC;
		this.endSOC = endSOC;
		this.reason = reason;
		this.elecQua = elecQua;
		this.staRead = staRead;
		this.endRead = endRead;
		this.money = money;
		this.staMoney = staMoney;
		this.charStra = charStra;
		this.charStraPara = charStraPara;
		this.carVIN = carVIN;
		this.elecTim = elecTim;
		this.StaMod = staMod;
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

	public int getGunType() {
		return gunType;
	}

	public void setGunType(int gunType) {
		this.gunType = gunType;
	}

	public int getGun() {
		return gun;
	}

	public void setGun(int gun) {
		this.gun = gun;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getStartTim() {
		return startTim;
	}

	public void setStartTim(String startTim) {
		this.startTim = startTim;
	}

	public String getEndTim() {
		return endTim;
	}

	public void setEndTim(String endTim) {
		this.endTim = endTim;
	}

	public int getTimeLen() {
		return timeLen;
	}

	public void setTimeLen(int timeLen) {
		this.timeLen = timeLen;
	}

	public String getStartSOC() {
		return startSOC;
	}

	public void setStartSOC(String startSOC) {
		this.startSOC = startSOC;
	}

	public String getEndSOC() {
		return endSOC;
	}

	public void setEndSOC(String endSOC) {
		this.endSOC = endSOC;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getStaMoney() {
		return staMoney;
	}

	public void setStaMoney(double staMoney) {
		this.staMoney = staMoney;
	}

	public int getCharStra() {
		return charStra;
	}

	public void setCharStra(int charStra) {
		this.charStra = charStra;
	}

	public int getCharStraPara() {
		return charStraPara;
	}

	public void setCharStraPara(int charStraPara) {
		this.charStraPara = charStraPara;
	}

	public String getCarVIN() {
		return carVIN;
	}

	public void setCarVIN(String carVIN) {
		this.carVIN = carVIN;
	}

	public double[] getElecTim() {
		return elecTim;
	}

	public void setElecTim(double[] elecTim) {
		this.elecTim = elecTim;
	}

	public int getStaMod() {
		return StaMod;
	}

	public void setStaMod(int staMod) {
		StaMod = staMod;
	}
	
	
	
	
	

}
