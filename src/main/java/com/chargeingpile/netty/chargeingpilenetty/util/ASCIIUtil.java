package com.chargeingpile.netty.chargeingpilenetty.util;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import org.junit.Test;

public class ASCIIUtil {

    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * Convert ASCII byte[] to int string<br>
     * 解析返回的卡号
     * 
     * @param m byte[]
     * @param q 起始位置
     * @param j 停止位置(不包括j)
     * @return
     */
    public static String ASCII2Int(byte[] m, int q, int j) {
       
        String string = BytesUtil.bytesToHexString(m, q, j-q);
        
        string = convertHexToString(string);
        
        return string.trim();
    }

    /**
     * Convert ASCII string to hex string
     * 盛宏协议（补齐32位，不足补’\0’ ）
     * 
     * @param src
     * @return
     */
    public static String ASCII2HexString(String ascii) {
        StringBuilder builder = new StringBuilder("");
        if (ascii == null || ascii.isEmpty()) {
            return null;
        }
        char[] res = ascii.toCharArray();
        for (int i = 0; i < res.length; i++) {
            int s = res[i]; // to ascii value(10进制)
            builder.append(Integer.toHexString(s));
        }
        // 补齐32位，不足补’\0’
        while (builder.length() < 64) {
            builder.append("0");
        }

        return builder.toString();
    }


    @Test
    public void demo(){
        String a = ASCII2HexString("");
        System.out.println(a);
    }


    /**
     * 10进制转换为相应的ascii码16进制形式
     * 
     * @param ascii
     * @return
     */
    public static String int2AsciiHex(String ascii) {
        StringBuilder builder = new StringBuilder("");
        if (ascii == null || ascii.isEmpty()) {
            return null;
        }
        char[] res = ascii.toCharArray();
        for (int i = 0; i < res.length; i++) {
            int s = res[i]; // to ascii value(10进制)
            builder.append(Integer.toHexString(s));
        }

        return builder.toString();
    }

}
