package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import java.io.Serializable;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;





/**
 * 盛宏协议数据格式定义
 * 
 * @author cj
 *
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 6017843213318598852L;

	/**
	 * 命令代码
	 */
	protected String m_cmd;


	public Message() {
		super();
	}
	

	/**
	 * @param sequence 序列号
	 * @return
	 */
	public byte[] getMsgByte(int sequence) {
		StringBuffer sb = new StringBuffer();	
		add(sb);	
		byte[] cmd = BytesUtil.hexStringToBytes(m_cmd);
		byte[] data = BytesUtil.hexStringToBytes(sb.toString());
		return SHUtils.makeData(sequence, cmd, data);
	}
	

	public abstract void add(StringBuffer sb);



	public String getM_cmd() {
		return m_cmd;
	}


	public void setM_cmd(String m_cmd) {
		this.m_cmd = m_cmd;
	}
	
	
}
