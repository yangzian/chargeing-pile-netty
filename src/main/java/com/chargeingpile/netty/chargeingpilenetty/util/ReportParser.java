package com.chargepile.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 报文解析器
 * User: 韩彦伟
 * Date: 14-8-7
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class ReportParser {

    /**
     * 表单类型报文解析
     * @param reportContent
     * @param charset
     * @return
     */
    public static Map<String,String> parseFormDataPatternReport(String reportContent,String charset) throws Exception{

        String[] domainArray = reportContent.split("&");

        Map<String,String> key_value_map = new HashMap<String, String>();
        for(String domain : domainArray){
            String[] kvArray = domain.split("=");

            if(kvArray.length == 2){
                String decodeString = URLDecoder.decode(kvArray[1], charset);
                String lastInnerValue = new String(decodeString.getBytes(charset), "utf-8");

                if(lastInnerValue!=null&&!"".equals(lastInnerValue))
                    key_value_map.put(kvArray[0], lastInnerValue);
            }
        }

        return key_value_map;
    }

}
