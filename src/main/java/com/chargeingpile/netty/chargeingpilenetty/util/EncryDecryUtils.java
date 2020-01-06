package com.chargeingpile.netty.chargeingpilenetty.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 报文加解密工具类
 */
public class EncryDecryUtils {
    // 对数据进行base64加密
    public static String base64Encrypt(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }


    // 对数据进行base64解密
    public static String base64Decrypt(String str) {
        byte[] b = null;
        String result = null;
        if (str != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(str);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    
    // 把传入的数组进行base64加密
    public static String base64Encrypt(byte[] b) {
        String s = null;
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static void main(String[] args) {
       // System.out.print(decryptFromBase64DES("F8iMTTzzt5Mw151105154545","9ALSvn97bJFqRFA4JHWaflwFTV//YBqQoEiDfVgrtNto5XXv/KfjxMlCduox5wn5B9x89lSo6hifvX6p6/5KXLB9TgZdK7UyWQhAP0taO4T1Hkw3ZOXPR3dPY6m+Pv6ndwAOBrxJ0BpzwPS2pTABMzult/Yi/ghWb95NGagdTrrQSpsYh+r4b8oqOSJBR5DsEdHi9MsO3pO9wm9O7sbXWabhjzLcRBKbzzg++CbPiMEcTj2bodtVKFdMLU9ek9t3NABUjBcNI5hkEEr+V9CYYzscNL3PcgBWMwhNU4VLaFyCYhkNacglag=="));
    }
    
    //对返回的数据 先用base64解密 再用3Des解密
   /* public static String decryptFromBase64DES(String key,String data) {
        String result = null;
        byte[] keyByte = key.getBytes();
        byte[] base64Byte = EncryDecryUtils.getFromBase64byte(data);
        try {
            result =  new String(DESUtils.Union3DesDecrypt(keyByte, base64Byte),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/
    
    //把数据 先用3des加密 再用base64加密
    /*public static String encryptFromDESBase64(String key,String data) {
        String result = null;
        result=  EncryDecryUtils.base64Encrypt(DESUtils.Union3DesEncrypt(key.getBytes(), data.getBytes()));
        return result;
    }
    */
    
    
    // 把一个base64的加密字符串转成数组 
    public static byte[] getFromBase64byte(String s) {
        byte[] b = null;
//        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  b;
    }

    public static String md5(String str) {
		if (str == null) {
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			return str;
		} catch (UnsupportedEncodingException e) {
			return str;
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
        int aa ;

		for (int i = 0; i < byteArray.length; i++) {
            aa = byteArray[i];
             aa = aa&0xff;
			if (Integer.toHexString(aa).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(aa));
			else
				md5StrBuff.append(Integer.toHexString(aa));
		}
		return md5StrBuff.toString();
	}


    //MD5_16
    public static String MD5_16(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            result = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }


}
