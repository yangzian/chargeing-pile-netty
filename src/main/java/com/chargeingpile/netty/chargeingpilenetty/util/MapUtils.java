package com.chargepile.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 树强
 * Date: 15-11-6
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
public class MapUtils {

    public static Map<String, String> mapRemoveNull (Map<String, String> valueMap){
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(valueMap);
        for(String key : valueMap.keySet()){
            if(map.get(key) == null || "".equals(map.get(key))){
                map.remove(key);
            }
        }
        return  map;
    }

}
