/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.integration.wechat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.common.util.MyX509TrustManager;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.wechat.entity.AccessToken;
import com.yumu.hexie.integration.wechat.entity.common.WechatResponse;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: WeixinUtilV2.java, v 0.1 2016年5月6日 下午3:12:52  Exp $
 */
public class WeixinUtilV2 {
    public final static String  ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    private static final Logger log          = LoggerFactory.getLogger(WeixinUtil.class);

    public static WechatResponse httpsRequest(String requestUrl, String requestMethod, String postStr, String accToken) {
        if (StringUtil.isNotEmpty(accToken)) {
            requestUrl = requestUrl.replace("ACCESS_TOKEN", accToken);
        }
        WechatResponse jsonObject = null;
        try {
            String result = queryForString(requestUrl, requestMethod, postStr);
            jsonObject = (WechatResponse) JacksonJsonUtil.jsonToBean(result, WechatResponse.class);
        } catch (ConnectException ce) {
            log.error("server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:", e);
        }
        return jsonObject;
    }

    public static AccessToken getAccessToken(String appId, String appSecret) {
        String requestUrl = ACCESS_TOKEN.replace("APPID", appId).replace("APPSECRET", appSecret);
        WechatResponse jsonObject = httpsRequest(requestUrl, "GET", null, null);
        // 如果请求成功
        if (null != jsonObject && StringUtil.isNotEmpty(jsonObject.getAccess_token())) {
            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jsonObject.getAccess_token());
            accessToken.setExpiresIn(jsonObject.getExpires_in());
            return accessToken;
        }
        return null;
    }

    private static String queryForString(String requestUrl, String requestMethod, String postStr)
             throws NoSuchAlgorithmException,
             NoSuchProviderException,
             KeyManagementException,
             MalformedURLException,
             IOException,
             ProtocolException,
             UnsupportedEncodingException {
        log.error("wechat request:" + requestUrl);
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        StringBuffer buffer = new StringBuffer();
        TrustManager[] tm = { new MyX509TrustManager() };
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        URL url = new URL(requestUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
        httpUrlConn.setSSLSocketFactory(ssf);

        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod(requestMethod);

        if ("GET".equalsIgnoreCase(requestMethod)) {
            httpUrlConn.connect();
        }

        // 当有数据需要提交时
        if (null != postStr) {
            OutputStream outputStream = httpUrlConn.getOutputStream();
            // 注意编码格式，防止中文乱码
            outputStream.write(postStr.getBytes("UTF-8"));
            outputStream.close();
        }

        // 将返回的输入流转换成字符串
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        inputStream = null;
        httpUrlConn.disconnect();

        String result = buffer.toString();

        log.error("wechat response:" + result);
        return result;
    }
}
