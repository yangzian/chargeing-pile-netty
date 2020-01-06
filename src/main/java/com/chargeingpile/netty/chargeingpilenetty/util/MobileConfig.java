package com.chargepile.util;

import java.util.ResourceBundle;

public class MobileConfig {
	private static ResourceBundle bundle = ResourceBundle.getBundle("bundleResponse");
//	private static ResourceBundle bundle = ResourceBundle.getBundle("config.bundleResponse");
//	private static ResourceBundle bundle = ResourceBundle.getBundle("bundleResponse");
	//private static ResourceBundle bundle = ResourceBundle.getBundle("config.bundleResponse");
//	private static ResourceBundle bundle = ResourceBundle.getBundle(System.getProperty("user.dir") + "\\mean\\bundleResponse");
	
	
	
	public static String getString(String key){
		return bundle.getString(key);
	}
	
	public static int getInt(String key){
		return Integer.parseInt(bundle.getString(key));
	}
	
	public static String getString(String key, String value){
		return null == bundle.getString(key) ? value : bundle.getString(key);
	}
	
	public static String getStringCode(String key){
		return bundle.getString(key)!=null?bundle.getString(key):"";
	}

	public static String get(String key) {
		return bundle.getString(key);
	}
	public static void main(String[] args) {
		
		System.out.println(getString("code.global.success"));
		
		
		
	}
}
