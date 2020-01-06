package com.chargepile.util;

import java.security.MessageDigest;

public class MD5 {
    /**
     * MD5方法
     * 
     * @param text 明文
     * @param charset 密钥
     * @return 密文
     * @throws Exception
     */
	public static String md5(String text, String charset) throws Exception {
        if(charset == null || charset.length()==0)
            charset = "UTF-8";

		byte[] bytes = text.getBytes(charset);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	/**
	 * MD5验证方法
	 * 
	 * @param text 明文
	 * @param charset 字符编码
	 * @param md5 密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String charset, String md5) throws Exception {
		String md5Text = md5(text, charset);
		if(md5Text.equalsIgnoreCase(md5))
		{
			return true;
		}

			return false;
	}
}