package com.chargepile.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*字符串 DESede(3DES) 加密  
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位  
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的  
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加  
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的  
 * 密钥，P代表明文，C代表密表，这样，  
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))  
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))  
 * */
public class ThreeDes {

    /**
     * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使  
     * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：  
     *jce.jar  
     *security/US_export_policy.jar  
     *security/local_policy.jar  
     *ext/sunjce_provider.jar   
     */

    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节      
    //src为被加密的数据缓冲区（源）  
    public static byte[] encryptMode(byte[] keybyte,byte[] src){
        try {
            //生成密钥  
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密  
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//在单一方面的加密或解密  
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception  
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节      
    //src为加密后的缓冲区  
    public static byte[] decryptMode(byte[] keybyte,byte[] src){
        try {
            //生成密钥  
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密  
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception  
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串  
    public static String byte2Hex(byte[] b){
        String hs="";
        String stmp="";
        for(int n=0; n<b.length; n++){
            stmp = (java.lang.Integer.toHexString(b[n]& 0XFF));
            if(stmp.length()==1){
                hs = hs + "0" + stmp;
            }else{
                hs = hs + stmp;
            }
            if(n<b.length-1)hs=hs+":";
        }
        return hs.toUpperCase();
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub  
        //添加新安全算法,如果用JCE就要把它添加进去  
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        final byte[] keyBytes = {0x11, 0x22, 0x4F, 0x58,
                (byte)0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51,
                (byte)0xCB,
                (byte)0xDD, 0x55, 0x66, 0x77, 0x29, 0x74,
                (byte)0x98, 0x30, 0x40, 0x36,
                (byte)0xE2
        };  //24字节的密钥
        String szSrc = "This is a 3DES test. 测试";
        System.out.println("加密前的字符串:" + szSrc);
        byte[] encoded = encryptMode(keyBytes,szSrc.getBytes());
        System.out.println("加密后的字符串:" + new String(encoded));

        byte[] srcBytes = decryptMode(keyBytes,encoded);
        System.out.println("解密后的字符串:" + (new String(srcBytes)));
    }
} 