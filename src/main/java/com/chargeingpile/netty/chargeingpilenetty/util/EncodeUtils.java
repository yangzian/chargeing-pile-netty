package com.chargeingpile.netty.chargeingpilenetty.util;

/**
 * 字符串转换器
 *
 * @author: 韩彦伟
 * @since: 12-7-31 下午2:55
 * @version: 1.0.0
 */
public class EncodeUtils {

    /**
     * 十六进制字符串转二进制
     *
     * @param str 十六进制串
     * @return
     */
    public static byte[] hexString2Byte(String str) {
        int len = str.length();
        String stmp = null;
        byte bt[] = new byte[len / 2];
        for (int n = 0; n < len / 2; n++) {
            stmp = str.substring(n * 2, n * 2 + 2);
            bt[n] = (byte) (Integer.parseInt(stmp, 16));
        }
        return bt;
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2HexString(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + "";
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 十进制字符串转十六进制字符串
     *
     * @param str
     * @return
     */
    public static String string2Hex(String str) {
        String hs = "";
        for (int i = 0; i < str.length(); i++) {
            int ch = (int) str.charAt(i);
            String s4 = Integer.toHexString(ch);
            hs = hs + s4;
        }
        return hs;
    }

    /**
     * 十六进制字符串转十进制字符串
     *
     * @param hexString
     * @return
     */
    public static String hex2String(String hexString) {
        byte[] baKeyword = new byte[hexString.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            hexString = new String(baKeyword, "utf-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return hexString;
    }

    /**
     * 不是8位倍数时补足
     *
     * @param b
     * @return
     */
    public static byte[] complementZero(byte[] b) {
        int len = b.length;

        //data不足8位以0补足8位
        if (b.length % 8 != 0) {
            len = b.length - b.length % 8 + 8;
        } else {
            return b;
        }
        byte[] needData = null;
        needData = new byte[len];
        for (int i = 0; i < len; i++) {
            needData[i] = 0x00;
        }
        System.arraycopy(b, 0, needData, 0, b.length);
        return needData;
    }

}
