package com.chargeingpile.netty.chargeingpilenetty.util;

import java.util.*;

/**
 * MD5Facade  ---  汇集多类型MD5签名逻辑
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
public class MD5Facade {
    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * @param dataMap  --需要参与MD5签名的数据
     * @param securityKey    --密钥
     * @return
     */
    public static boolean validateFormDataParamMD5(Map<String,String> dataMap,String securityKey,String currentSignature){
        if(dataMap == null) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,"");
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = MD5.md5(toMD5String,"");

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * <p>要先对Key进行按字典升序排序
     * @param dataMap  -- 数据
     * @param securityKey    --密钥
     * @param charset
     * @return
     */
    public static String getFormDataParamMD5(Map<String,String> dataMap,String securityKey,String charset){
        if(dataMap == null) return null;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,charset);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();
            System.out.println("待MD5签名字符串：" + toMD5String);

            String lastMD5Result = MD5.md5(toMD5String,charset);
            System.out.println("MD5签名后字符串:" + lastMD5Result);

            return lastMD5Result;
        }catch (Exception ex){
            //ignore
            return "";
        }
    }
    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * <p>要先对Key进行按字典升序排序
     * @param dataMap  -- 数据
     * @param securityKey    --密钥
     * @param charset
     * @return
     */
    public static String getFormDataParamMD5ByString(String payString,String securityKey,String charset){
    	
    	try{
    		String securityKeyMD5 = MD5.md5(securityKey,charset);
    		
    		payString = payString + "&" + securityKeyMD5;
    		
    		System.out.println("待MD5签名字符串：" + payString);
    		
    		String lastMD5Result = MD5.md5(payString,charset);
    		
    		System.out.println("MD5签名后字符串:" + lastMD5Result);
    		
    		return lastMD5Result;
    	}catch (Exception ex){
    		//ignore
    		return "";
    	}
    }
}