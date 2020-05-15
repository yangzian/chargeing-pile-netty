package com.chargeingpile.netty.chargeingpilenetty.shenghong.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class BytesUtil {

	/**
	 * 2进制
	 * 
	 * @param b
	 * @return
	 */
	public static String toBinary(byte b) {
		StringBuilder stringBuilder = new StringBuilder();
		String val = BytesUtil.byteToHexString(b);
		BigInteger integer = new BigInteger(val, 16);
		stringBuilder.append(integer.toString(2));
		while (stringBuilder.length() < 8) {
			stringBuilder.insert(0, "0");
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取高四位
	 * @param data
	 * @return
	 */
	public static int getHeight4(byte data){
	    int height;
	    height = ((data & 0xf0) >> 4);
	    return height;
	}

	/**
	 * 获取低四位
	 * @param data
	 * @return
	 */
	public static int getLow4(byte data){
	    int low;
	    low = (data & 0x0f);
	    return low;
	}
	/**
	 * 去除int最高位
	 * 
	 * @param val
	 * @return
	 */
	public static int delH(int val) {
		return val = (val << 1 & 0xff) >>> 1;
	}

	public static String byteToHexString(byte src) {
		StringBuilder stringBuilder = new StringBuilder("");
		int v = src & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);
		return stringBuilder.toString();
	}

	/**
	 * Convert byte[] to hex string. 把字节数组转化为字符串
	 * 
	 * @param src
	 * @param begin
	 *            byte[] 起始位置
	 * @param len
	 *            长度
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int begin, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		// 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
		int end = begin + len;
		for (int i = begin; i < end; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert byte[] to hex string. 把字节数组转化为字符串
	 * 
	 * 
	 * @param src
	 *            byte[]
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		// 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 空格分隔
	 * @param src 
	 * @return
	 */
	public static String bytesToHexString2(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		// 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv).append(" ");
		}
		return stringBuilder.toString();
	}

	/**
	 * 字节数组转成16进制表示格式的字符串
	 *
	 * @param byteArray
	 *            需要转换的字节数组
	 * @return 16进制表示格式的字符串
	 **/
	public static String toHexString(byte[] byteArray) {
		if (byteArray == null || byteArray.length < 1)
			throw new IllegalArgumentException("this byteArray must not be null or empty");

		final StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
				hexString.append("0");
			hexString.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return hexString.toString().toLowerCase();
	}


	/**
	 * Convert byte[] to hex string[]. 把字节数组转化为字符串数组
	 * 
	 */
	public static String[] bytesToHexStrings(byte[] src) {
		String s = bytesToHexString(src);
		String[] ss = new String[src.length];
		for (int j = 0; j < ss.length; j++) {
			ss[j] = s.substring(2 * j, 2 * (j + 1));
		}
		return ss;
	}

	/**
	 * Convert hex string to byte[] 把字符串转化为字节数组
	 * 
	 * @param hexString
	 *            the hex string, 两位string一个byte, 允许有空格分隔
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.replaceAll(" ", "");
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		
		
		return d;
	}

	// ------------

	/**
	 * 将 10进制int 转换为占一个字节的hexString
	 * 
	 * @param src
	 * @return
	 */
	public static String int2HexString(int src) {
		StringBuilder stringBuilder = new StringBuilder();
		String hv = Integer.toHexString(src);
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);
		return stringBuilder.toString();

	}

	/**
     * 将10进制int转换为占2个字节的hexString(低字节在前)
     * 
     * @param src
     * @return
     */
    public static String intToHexString(int src) {
        byte[] b = BytesUtil.toBytes(src);
        return BytesUtil.bytesToHexString(b);
    }
	/**
	 * 将10进制int转换为占2个字节的hexString(高字节在前)
	 * 
	 * @param src
	 * @return
	 */
	public static String intToHexStringH(int src) {
		byte[] b = BytesUtil.toBytesH(src);
		return BytesUtil.bytesToHexString(b);
	}

	/**
	 * 将10进制String 转换为占2个字节的hexString(低位在前)
	 * 
	 * @param src
	 * @return
	 */
	public static String toHexString(String src) {
		return intToHexString(Integer.valueOf(src));
	}

	/**
	 * 将10进制int转换为占4个字节的hexString
	 * 
	 * @param src
	 * @return
	 */
	public static String intTo4HexString(int src) {
		return BytesUtil.bytesToHexString(BytesUtil.intToBytes(src));

	}

	/**
	 * int -> byte[iArrayLen]
	 * 
	 * @param iSource
	 * @param iArrayLen
	 *            <=4
	 * @return
	 */
	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; i < iArrayLen; i++) {
			// bLocalArr[i] = (byte) (iSource >> 8 * (iArrayLen - i - 1) &
			// 0xFF);
			bLocalArr[i] = (byte) (iSource >> 8 * i);
		}
		return bLocalArr;
	}

	/**
	 * int -> byte[2]
	 * 
	 * @param
	 * @return 低位在前
	 */
	public static byte[] toBytes(int v) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (0xff & v);
		targets[1] = (byte) (0xff & (v >> 8));
		return targets;
	}
	/**
     * int -> byte[2]
     * 
     * @param
     * @return 高位在前
     */
    public static byte[] toBytesH(int v) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (0xff & v);
        targets[0] = (byte) (0xff & (v >> 8));
        return targets;
    }

	/**
	 * int -> byte[4]
	 * 低位在前
	 * 
	 * @param num
	 * @return 
	 */
	public static byte[] intToBytes(int num) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (num & 0xff);// 最低位
		targets[1] = (byte) ((num >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((num >> 16) & 0xff);// 次高位
		targets[3] = (byte) (num >>> 24);// 最高位,无符号右移。
		return targets;
	}

	/**
	 * 低位在前 ，3位的byte[] 转 int
	 * 
	 * @param res
	 *            byte数组
	 * @return
	 */
	public static int toInt3(byte[] bs, int begain) {
		int accum = 0;
		accum = bs[begain + 0] & 0xFF;
		accum |= (bs[begain + 1] & 0xFF) << 8;
		accum |= (bs[begain + 2] & 0xFF) << 16;
		return accum;
	}

	/**
	 * 低位在前 ，4位的byte 转 int
	 * 
	 * @param res
	 *            低位在前 byte数组
	 * @return
	 */
	public static int toInt4(byte[] bs) {
		int accum = 0;
		accum = bs[0] & 0xFF;
		accum |= (bs[1] & 0xFF) << 8;
		accum |= (bs[2] & 0xFF) << 16;
		accum |= (bs[3] & 0xFF) << 24;
		return accum;
	}

	/**
	 * 获取 高位在前的4位byte int值<br>
	 * 
	 * @param res
	 *            高位在前 byte数组
	 * @return 
	 */
	public static int toIntByH(byte[] bs, int start) {
        int accum = 0;
        accum   = bs[start + 3] & 0xFF;
        accum |= (bs[start + 2] & 0xFF) << 8;
        accum |= (bs[start + 1] & 0xFF) << 16;
        accum |= (bs[start + 0] & 0xFF) << 24;
        return accum;
	}
        
	/**
	 * 将低位在前 ，4位的byte 转 int
	 * 
	 * @param bs
	 *            byte数组
	 * @param pos
	 *            起始位置
	 * @return
	 */
	public static int toInt4(byte[] bs, int pos) {
		int accum = 0;
		accum = bs[pos + 0] & 0xFF;
		accum |= (bs[pos + 1] & 0xFF) << 8;
		accum |= (bs[pos + 2] & 0xFF) << 16;
		accum |= (bs[pos + 3] & 0xFF) << 24;
		return accum;
	}

	/**
     * 低位在前 的 byte[2] 转 int, int*0.01转成double并保留两位小数
     * 
     * @param res 
     * @param begainIdx
     *            起始位置
     * @return
     */
    public static double toDouble(byte[] res, int begainIdx) {
        Double data = toInt2(res, begainIdx) * 0.01;
        DecimalFormat bd = new DecimalFormat("#.00");
        data = Double.valueOf( bd.format(data) );
        
        return data;
    }
	/**
	 * 低位在前 的 byte[2] 转 int
	 * 
	 * @param res 
	 * @param begainIdx
	 *            起始位置
	 * @return
	 */
	public static int toInt2(byte[] res, int begainIdx) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		int targets = (res[begainIdx] & 0xff) | ((res[begainIdx + 1] << 8) & 0xff00);

		return targets;
	}

	/**
	 * byte[] 转 int
	 * 
	 * @param res
	 *            低位在前 ，2位的byte数组
	 * @return
	 */
	public static int toInt2(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
		return targets;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 获取接收到的信息中的指令码
	 * 
	 * @param msg
	 *            接收到的信息
	 * @return
	 */
	public static String getMsgCmd(byte[] msg) {
		byte[] cmd = { msg[6], msg[7] };
		return bytesToHexString(cmd);

	}

	/**
	 * 将i进制字符串转换成j进制字符串
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static String itoj(String str, int i, int j) {
		String[] strs = str.split("\\s+");
		String result = "";
		for (String string : strs) {
			String hex = Integer.toString(Integer.parseInt(string, i), j);
			result += hex;
		}
		return result;

	}

}
