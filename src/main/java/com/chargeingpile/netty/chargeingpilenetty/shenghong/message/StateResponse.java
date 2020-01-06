package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

/**
 * 桩状态信息回复报文
 * 后台计费模式需回复 card balance money信息
 */
public class StateResponse extends Message {
	private static final long serialVersionUID = 1L;
	private String yuliu1 = "0000";// (2字节)
	private String yuliu2 = "0000";// (2字节)
	private String gun = "00";// 充电枪口（1字节）
	private String card = "0000000000000000000000000000000000000000000000000000000000000000";// 充电卡号（32字节）
	private String balance = "00000000";// 余额（4字节）
	private String money = "00000000";// 余额（4字节）

	public StateResponse() {
		super();
		setM_cmd(ShengHong.STATE_SLAVE);
	}
	
	/**
	 * @param gun：充电枪口
	 *            addr：命令起始标志 num：命令个数 para：命令参数
	 * @author pwt
	 */
	public StateResponse(String gun, String card) {
		this();
		this.gun = gun;
		this.card = ASCIIUtil.ASCII2HexString(card);
	}

	/**
	 * @param gun：充电枪口
	 *            addr：命令起始标志 num：命令个数 para：命令参数
	 * @author pwt
	 */
	public StateResponse(int gun, String card, int balance, int money) {
		this();
		this.gun = BytesUtil.int2HexString(gun);
		this.card = ASCIIUtil.ASCII2HexString(card);
		this.balance = BytesUtil.intTo4HexString(balance);
		this.money = BytesUtil.intTo4HexString(money);
	}

	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(gun)
		.append(card).append(balance).append(money)
		;

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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

}
