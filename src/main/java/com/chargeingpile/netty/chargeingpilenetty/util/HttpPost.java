package com.chargepile.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpPost {
	public static String http(String url, String sendString) throws Exception {

		URL u = null;

		HttpURLConnection con = null;

		// 构建请求参数

		System.out.println("ERP连接:" + url);
		System.out.println("发送给ERP信息:" + sendString);
//		logger.info("ERP连接:" + url);
//		logger.info("发送给ERP信息:" + sb.toString());

		// 尝试发送请求

		try {

			u = new URL(url);

			con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setDoOutput(true);

			con.setDoInput(true);

			con.setUseCaches(false);

			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

 			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");

			osw.write(sendString);

			osw.flush();

			osw.close();

		} catch (Exception e) {
            e.printStackTrace();
			throw new Exception("与服务器连接发生错误");

		} finally {

			if (con != null) {

				con.disconnect();

			}

		}

		// 读取返回内容

		StringBuffer buffer = new StringBuffer();

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

			String temp;

			while ((temp = br.readLine()) != null) {

				buffer.append(temp);

				buffer.append("\n");

			}

		} catch (Exception e) {
            e.printStackTrace();
			throw new Exception("从服务器获取数据失败");

		}

		return buffer.toString();

	}

}
