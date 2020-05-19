package com.chargepile.service.collection;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class Http {

//	private static final String com_ip  = "192.168.23.6";
//	private static final String com_por = "8080";
//	private static final String CHARGE_URL = "/chargePileServer/service/endChaPus.htm";
	//
	private static final String com_ip  = "139.196.235.78";
	private static final String com_por = "80";
	private static final String CHARGE_URL = "/service/endChaPus.htm";

	/**
	 * 
	 * @param cha_num
	 * @param cha_ele
	 * @return
	 * @throws Exception
	 */
	public static JSONObject staCha(String cha_num, double cha_ele) throws Exception {

		JSONObject chaJson = new JSONObject();

		// 3.取出调用运营平台端接口，拼接传入参数
		StringBuilder inpUrl = new StringBuilder();

		inpUrl.append("http://");

		inpUrl.append(com_ip );

		inpUrl.append(":");

		inpUrl.append(com_por );

		inpUrl.append(CHARGE_URL);

		inpUrl.append("?cha_num=");

		inpUrl.append(cha_num);

		inpUrl.append("&cha_ele=");

		inpUrl.append(cha_ele);

		// 预约状态返回，成功返回1，不成功返回0或其它值
		chaJson = senIns(inpUrl.toString());
		
		//System.out.println("staCha -> " + chaJson.toString()) ;

		return chaJson;
	}

	/**
	 * 向电桩发送指令
	 * 
	 * @return
	 * @throws Exception
	 */
	private static JSONObject senIns(String inpUrl) throws Exception {

		HttpURLConnection httpconn = null;

		String redLine = "";

		try {

			URL url = new URL(inpUrl);
			// 开启URL连接
			httpconn = (HttpURLConnection) url.openConnection();

			httpconn.setRequestProperty("Accept-Charset", "UTF-8");

			httpconn.setRequestProperty("contentType", "utf-8");
			// 读取返回值
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));

			redLine = rd.readLine();
			// 关闭流
			rd.close();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			// 关闭连接
			if (httpconn != null) {

				httpconn.disconnect();

				httpconn = null;
			}
		}
		if (redLine.isEmpty()) {

			JSONObject obj = new JSONObject();

			JSONObject obj1 = new JSONObject();

			obj1.put("state", 0);

			obj.put("data", obj1);

			return obj;
		} else {

			JSONObject obj = JSONObject.parseObject(redLine);

			return obj;
		}
	}

}
