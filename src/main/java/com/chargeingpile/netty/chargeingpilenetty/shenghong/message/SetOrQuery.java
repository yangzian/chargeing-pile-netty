package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;

/**
 * 设置/查询工作参数
 * 
 *
 */
public class SetOrQuery extends Message {

	private static final long serialVersionUID = 1L;

	private String yuliu1 = "0000";// (2字节)
	private String yuliu2 = "0000";// (2字节)
	/**
	 * 类型 查询0，设置1 (1字节)
	 */
	private String type;
	/**
	 * 设置/查询参数 起始地址 (4字节)
	 */
	private String addr;
	/**
	 * 设置/查询个数
	 */
	private String num = "01";

	/**
	 * 设置参数字节数
	 */
	private String paraLen = "0400";
	/**
	 * 设置数据 ：当类型为设置时才有 (N字节)
	 */
	private String set;

	public SetOrQuery() {
		super();
		setM_cmd(ShengHong.PARA_SLAVE);
	}

	/**
	 * @param type
	 *            类型（查询0，设置1）
	 * @param addr
	 *            参数起始地址
	 */
	public SetOrQuery(int type, int addr) {
		this();
		this.type = BytesUtil.int2HexString(type);
		this.addr = BytesUtil.intTo4HexString(addr);
	}

	/**
	 * @param type
	 *            类型（查询0，设置1）
	 * @param addr
	 *            参数起始地址
	 * @param set
	 *            设置数据
	 */
	public SetOrQuery(int type, int addr, String set) {
		this(type, addr);
		this.set = set;
	}

	@Override
	public void add(StringBuffer sb) {
		sb.append(yuliu1).append(yuliu2).append(type).append(addr).append(num).append(paraLen);
		if ( !CommonUtil.isEmpty(set) ) {
			sb.append(set);
		}
	}

}
