package com.chargeingpile.netty.chargeingpilenetty.shenghong.utils;

import java.math.BigInteger;

/**
 * IEEE754标准<br>
 * (-1)^s * 2^(e-127) * (1+f)
 * 
 * @author cj
 *
 */
public class IEEE754Utils {
	
	/**
	 * 将float转为低字节在前，高字节在后的  16进制String
	 * 
	 * @param value
	 * @return 低字节在前，高字节在后的 16进制 String
	 */
	public static String floatToHex(float value)  
	{  
	    int accum = Float.floatToRawIntBits(value);  
	    byte[] byteRet = new byte[4];  
	    byteRet[0] = (byte)(accum & 0xFF);  
	    byteRet[1] = (byte)((accum>>8) & 0xFF);  
	    byteRet[2] = (byte)((accum>>16) & 0xFF);  
	    byteRet[3] = (byte)((accum>>24) & 0xFF);  
	    
	    String hex = BytesUtil.bytesToHexString(byteRet);
	    return hex;  
	} 

	/**
	 * 将float转为低字节在前，高字节在后的 4位byte数组
	 * 
	 * @param Value
	 * @return byte[] 4位byte数组
	 */
	public static byte[] floatToBytes(float value)  
	{  
	    int accum = Float.floatToRawIntBits(value);  
	    byte[] byteRet = new byte[4];  
	    byteRet[0] = (byte)(accum & 0xFF);  
	    byteRet[1] = (byte)((accum>>8) & 0xFF);  
	    byteRet[2] = (byte)((accum>>16) & 0xFF);  
	    byteRet[3] = (byte)((accum>>24) & 0xFF);  
	    return byteRet;  
	} 
	
	
	/**
	 * 16进制string转换为float
	 * @param hex 16进制，低位在前，高位在后
	 * @return
	 */
	public static float hexStrToFloat(String  hex) {
		byte[] bs = BytesUtil.hexStringToBytes(hex);
		
		return bytesToFloat(bs, 0);
	}
	/**
	 * 将低字节在前，高字节在后的 4位byte数组转换为 float
	 * 
	 * @param bs
	 *            4位byte数组
	 * @param pos
	 *            byte[] 起始索引
	 * @return 
	 */
	public static float bytesToFloat(byte[] bs, int pos) {
		int accum = 0;
		accum = bs[pos + 0] & 0xFF;
		accum |= (bs[pos + 1] & 0xFF) << 8;
		accum |= (bs[pos + 2] & 0xFF) << 16;
		accum |= (bs[pos + 3] & 0xFF) << 24;
		
		return Float.intBitsToFloat(accum);
	}

	
	// ----
	 public static double bytesToDouble(byte[] bytes) throws Exception {
	 String hex = BytesUtil.bytesToHexString(bytes);
	 return hexToFloat(hex);
	 }
	
	 public static byte[] doubleToBytes(double d) throws Exception {
	 String hex = floatToHex(d);
	 return BytesUtil.hexStringToBytes(hex);
	 }
	
	 /**
	 * 浮点数转换为16进制的数据
	 *
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	 public static String floatToHex2(double f) throws Exception {
	 StringBuilder sBuilder = new StringBuilder();
	
	 // 符号位
	 if (f < 0) {
	 sBuilder.append("1");
	 f *= -1;
	 } else {
	 sBuilder.append("0");
	 }
	 // 转换成二进制数 1010010.01
	 String b = decimal2Binary(f);
	
	 // 1010010.01 = 1.01001001×2^6
	 // 阶码
	 int index = 0;
	 if (b.contains(".")) {
	 index = b.indexOf(".") - 1;
	 }
	 // System.out.println(index);
	
	 int e = index + 127;
	 // System.out.println( "e =" +decimal2Binary(e));
	 sBuilder.append(decimal2Binary(e));
	
	 // 尾数
	 double m = Math.scalb(1, index);
	 m = f / m - 1;
	 String mString = decimal2Binary(m);
	 // System.out.println(m);
	 StringBuilder eString = new StringBuilder();
	 if (m < 0) {
	 eString.append(String.valueOf(mString).substring(3));
	 } else {
	 eString.append(String.valueOf(mString).substring(2));
	 }
	 while (eString.length() < 23) {
	 eString.append("0");
	 }
	 sBuilder.append(eString);
	
	 // 2进制转16进制
	 BigInteger bigInt = new BigInteger(sBuilder.toString(), 2);
	 String s = bigInt.toString(16);
	
	 return s;
	 }
	
	 /**
	 * 浮点数转换为16进制的数据
	 *
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	 public static String floatToHex(double f) throws Exception {
	 StringBuilder sBuilder = new StringBuilder();
	
	 // 符号位
	 if (f < 0) {
	 sBuilder.append("1");
	 f *= -1;
	 } else {
	 sBuilder.append("0");
	 }
	 // 转换成二进制数 1010010.01
	 String b = decimal2Binary(f);
	
	 // 1010010.01 = 1.01001001×2^6
	 // 阶码
	 int index = 0;
	 if (b.contains(".")) {
	 index = b.indexOf(".") - 1;
	 }
	 // System.out.println(index);
	
	 int e = index + 127;
	 // System.out.println( "e =" +decimal2Binary(e));
	 sBuilder.append(decimal2Binary(e));
	
	 // 尾数
	 double m = Math.scalb(1, index);
	 m = f / m - 1;
	 String mString = decimal2Binary(m);
	 // System.out.println(m);
	 StringBuilder eString = new StringBuilder();
	 if (m < 0) {
	 eString.append(String.valueOf(mString).substring(3));
	 } else {
	 eString.append(String.valueOf(mString).substring(2));
	 }
	 while (eString.length() < 23) {
	 eString.append("0");
	 }
	 sBuilder.append(eString);
	
	 // 2进制转16进制
	 BigInteger bigInt = new BigInteger(sBuilder.toString(), 2);
	 String s = bigInt.toString(16);
	
	 return s;
	 }
	
	 /**
	 * 16进制的数据转换为浮点数
	 *
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	 public static double hexToFloat(String hex) throws Exception {
	 // 十六进制转成二进制
	 BigInteger bigInt = new BigInteger(hex, 16);
	 StringBuilder s = new StringBuilder();
	 s.append(bigInt.toString(2));
	 while (s.length() < 32) {
	 s.insert(0, 0);
	 }
	 // System.out.println(s);
	
	 // 符号位
	 String str1 = s.substring(0, 1);
	 // 阶码
	 String str2 = s.substring(1, 9);
	 // 尾数
	 String str3 = s.substring(9, s.length());
	 str3 = "0." + str3.replaceAll("0*$", "");
	 // System.out.println(str1 + "-" + str2 + "-" + str3);
	
	 // 真实阶码
	 int i = Integer.parseInt(str2, 2);
	 // System.out.println(i);
	 i -= 127;
	 // System.out.println(i);
	
	 double d = 1.0d + Double.valueOf(str3);
	 // 2进制浮点数转换成10进制浮点数
	 d = binary2Decimal(String.valueOf(d));
	
	 d = Math.scalb(d, i); // d × 2^i
	 // DecimalFormat df = new DecimalFormat("#.00");//保留两位小数
	 // BigDecimal bg=new BigDecimal(d);
	 // System.out.println(df.format(bg));
	 // System.out.println(bg.toPlainString());
	 // DecimalFormat dfa = new DecimalFormat("0");
	 // Double da = new Double("4.99958333E7");
	 // System.out.println(df.format(da));
	
	 // 加上符号位
	 if (str1.equalsIgnoreCase("1")) { // 负
	 d *= -1;
	 }
	
	 // DecimalFormat format = new DecimalFormat("########.00");
	 // d = Double.parseDouble(format.format(d));
	
	 return d;
	 }
	
	 /**
	 * 转 2进制小数转double
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	 public static Float binary2Decimal(String value) throws Exception {
	 if (CommonUtil.isEmpty(value)) {
	 return null;
	 }
	 float count = 0;
	 String in = "";
	 String r = "";
	 if (value.contains(".")) {
	 String[] str = value.split("\\.");
	 // 整数部分的值
	 in = str[0];
	 // 小数部分的值
	 r = str[1];
	 } else {
	 in = value;
	 }
	 // System.out.println("The integer is: " + in);
	 // System.out.println("The decimal number is: " + r);
	
	 // 将整数部分转化为10进制
	 for (int i = 0; i < in.length(); i++) {
	 int s = in.charAt(i) - 48;
	 count = count * 2 + s;
	 }
	
	 // 将小数部分转化为10进制
	 float num = 0;
	 for (int i = r.length() - 1; i >= 0; i--) {
	 int s = r.charAt(i) - 48;
	 num = (num + s) / 2;
	 }
	 count += num;
	 System.out.println("binary2Decimal -> " + count);
	 return count;
	
	 }
	
	 /**
	 * double 转 2进制
	 *
	 * @param value
	 * 正数
	 * @return
	 * @throws Exception
	 */
	 public static String decimal2Binary(double value) throws Exception {
	 // 整数部分的值
	 int in = (int) value;
	 // System.out.println("The integer is: " + in);
	 // 小数部分的值
	 double r = value - in;
	 // System.out.println("The decimal number is: " + r);
	
	 StringBuilder stringBuilder = new StringBuilder();
	 // 将整数部分转化为二进制
	 if (in == 0) {
	 stringBuilder.append(0);
	 } else {
	 int remainder = 0;
	 int quotient = 0;
	 while (in != 0) {
	 // 得商
	 quotient = in / 2;
	 // 得余数
	 remainder = in % 2;
	 stringBuilder.append(remainder);
	 in = quotient;
	 }
	 stringBuilder.reverse();
	 }
	
	 if (r > 0) {
	 stringBuilder.append(".");
	
	 // 将小数部分转化为二进制
	 int count = 32; // 限制小数部分位数最多为32位，如果超过32为则抛出异常
	 double num = 0;
	 while (r > 0.0000000001) {
	 count--;
	 if (count == 0) {
	 throw new Exception("Cannot change the decimal number to binary!");
	 }
	 num = r * 2;
	 if (num >= 1) {
	 stringBuilder.append(1);
	 r = num - 1;
	 } else {
	 stringBuilder.append(0);
	 r = num;
	 }
	 }
	 }
	
	 // System.out.println( "decimal2Binary -> " + stringBuilder.toString());
	 return stringBuilder.toString();
	 }

}
