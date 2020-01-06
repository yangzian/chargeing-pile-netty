package com.chargepile.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetMobelNum {
	
	@Autowired
	private EhcacheUtil ehcacheUtil;
	
	public String getMobelNum(String userId){
		
		String mobile = "";
		try {
			
			Map<String,Object> map = (Map<String,Object>)ehcacheUtil.getCacheElement(userId);
			
			mobile = map.get("mobile").toString();
			
		} catch (Exception e) {
			
		}
		
		return mobile;
		
	}
	
	public String getSendsMSS(String userId){
		
		String mobile = "";
		try {
			
			Map<String,Object> map = (Map<String,Object>)ehcacheUtil.getCacheElement(userId);
			
			mobile = map.get("SendsMSS").toString();
			
		} catch (Exception e) {
			
		}
		
		return mobile;
		
	}
	
	public void setSendsMSS(String userId,int x_param){
		
		Map<String, Object> map;
		
		try {
			
			map = (Map<String,Object>)ehcacheUtil.getCacheElement(userId);
			
			map.put("SendsMSS", x_param);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
