package com.spider.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * HTTP请求客户端，提供GET和POST两种方式发送请求。
 *
 * @author Administrator
 */
@Service
public class HttpUtil {
    /**
     * 日志对象。
     */
//    private static Logger logger = Logger.getLogger(HttpUtil.class);

    /**
     * 默认HTTP请求客户端对象。
     */
    private CloseableHttpClient _httpclient;
    /**
     * 用户自定义消息头。
     */
    private Map<String, String> _headers;

    /**
     * 使用默认客户端对象。
     */
    public HttpUtil() {
        // 1. 创建HttpClient对象。
        _httpclient = HttpClients.createDefault();
//        logger.info("create _httpclient ...");
    }

    /**
     * 调用者指定客户端对象。
     */
    public HttpUtil(Map<String, String> headers) {
        // 1. 创建HttpClient对象。
        _httpclient = HttpClients.createDefault();
        this._headers = headers;
//        logger.info("create _httpclient ...");
    }

    /**
     * HTTP POST请求。
     *
     * @param url
     * @param params
     * @return
     * @throws InterruptedException
     */
    public String post(String url, Map<String, String> params) {
        // 2. 创建请求方法的实例，并指定请求URL，添加请求参数。
        HttpPost post = postForm(url, params);
//        logger.info("create httppost : " + url);

        return invoke(post);
    }

    /**
     * HTTP GET请求。
     *
     * @param url
     * @return
     */
    public String get(String url) {
        HttpGet get = new HttpGet(url);
//        logger.info("create httpget : " + url);

        return invoke(get);
    }

    /**
     * 发送请求，处理响应。
     *
     * @param request
     * @return
     */
    private String invoke(HttpUriRequest request) {
        String result = null;

        if (this._headers != null) {
            //
            addHeaders(request);
//            logger.info("addHeaders to http ...");
        }

        HttpResponse response = null;
        try {
            // 3. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，返回一个HttpResponse。
            response = _httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);//取出应答字符串
//            logger.info("execute http success... ; result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.info("execute http exception...");
        } finally {
            // 4. 无论执行方法是否成功，都必须释放连接。
            request.abort();
//            logger.info("release http ...");
        }

        return result;
    }

    /**
     * 获取post方法。
     *
     * @param url
     * @param params
     * @return
     */
    private HttpPost postForm(String url, Map<String, String> params) {
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        // 组装参数。
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
//            logger.info("set utf-8 form entity to httppost ...");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }

    /**
     * 增加消息头。
     *
     * @param httpost
     */
    private void addHeaders(HttpUriRequest httpost) {
        Iterator<Entry<String, String>> it = this._headers.entrySet()
                .iterator();
        Entry<String, String> entry = null;
        String name = null;
        String value = null;

        while (it.hasNext()) {
            entry = it.next();
            name = entry.getKey();
            value = entry.getValue();

            httpost.addHeader(name, value);
        }
    }

    /**
     * 关闭HTTP客户端链接。
     */
    public void shutdown() {
        try {
//            logger.info("shutdown _httpclient ...");
            _httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}