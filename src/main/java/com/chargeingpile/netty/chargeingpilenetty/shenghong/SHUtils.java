package com.chargeingpile.netty.chargeingpilenetty.shenghong;

import org.springframework.stereotype.Component;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;

@Component
public class SHUtils {
	
	
	public static String getPileNum(byte[] msg) {
		
		String pile = ASCIIUtil.ASCII2Int(msg, 12, 44);
		
		return pile.trim();
	}

	// 盛宏桩
	public static boolean isShengHong(byte[] msg) {
		byte[] b = new byte[] { (byte) 0xAA, (byte) 0xf5 };
		if (msg[0] == b[0] && msg[1] == b[1]) {
			return true;
		} else
			return false;
	}

	
	

	/**
	 * 生成要发送的协议数据
	 * 
	 * @param cmd
	 *            指令码
	 * @param data
	 *            数据域
	 * @return
	 */
	public static byte[] makeData(int sequence, byte[] cmd, byte[] data) {
		byte[] res = null;
		int len = 0;
		if (data != null && data.length > 0) {
			len = 2 + 2 + 1 + 1 + 2 + data.length + 1;
			res = new byte[len];
			System.arraycopy(data, 0, res, 8, data.length);
		} else {
			len = 2 + 2 + 1 + 1 + 2 + 1;
			res = new byte[len];
		}
		res[0] = (byte) 0xAA;//
		res[1] = (byte) 0xF5;
		// 长度
		byte[] lenb = BytesUtil.toBytes(len);
		System.arraycopy(lenb, 0, res, 2, 2);
		res[4] = (byte) 0x10;
		res[5] = (byte) Integer.parseInt(Integer.toHexString(sequence));
		// cmd
		System.arraycopy(cmd, 0, res, 6, 2);

		// 校验码
		if (data != null && data.length > 0) {
			byte[] check = new byte[data.length + 2];
			System.arraycopy(cmd, 0, check, 0, 2);
			System.arraycopy(data, 0, check, 2, data.length);
			int code = calcuShCheckbit(check, check.length);
			byte[] crc = BytesUtil.toByteArray(code, 2);

			res[len - 1] = crc[0];
		}else{
			byte[] check = new byte[ 2];
			System.arraycopy(cmd, 0, check, 0, 2);
			int code = calcuShCheckbit(check, check.length);
			byte[] crc = BytesUtil.toByteArray(code, 2);

			res[len - 1] = crc[0];
		}
		

		return res;
	}

	/**
	 * 盛宏,计算校验位
	 * 
	 * @param data
	 *            生成校验位的数据
	 * @return 校验位
	 */
	public static int calcuShCheckbit(byte[] data, int len) {
		int total = 0;
		for (int i = 0; i < len; i++) {
			total += data[i];
		}
		return total & 0xff;
	}

}
