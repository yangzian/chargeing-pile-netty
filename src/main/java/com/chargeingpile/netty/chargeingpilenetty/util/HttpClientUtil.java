package com.chargeingpile.netty.chargeingpilenetty.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * Http协议客户端
 * User: 韩彦伟
 * Date: 14-8-9
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientUtil {

    public static String sendHttp(String url, String data,String charset) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        //请求超时时间  10S
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
        //读取超时时间  30S
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30000);

        HttpPost httpPost = new HttpPost(url);

        HttpEntity entity = new StringEntity(data);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        StatusLine status = response.getStatusLine();
        if (HttpStatus.SC_OK != status.getStatusCode()) {
            System.out.println("Http通讯失败,httpcode=" + status.getStatusCode());
            throw new Exception("Http通讯失败,httpcode=" + status.getStatusCode());
        }

        entity = response.getEntity();
        return EntityUtils.toString(entity, charset);
    }
}
