package com.spider.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataUtil {
	/**
	 * 获取HTML数据
	 * @param urlStr url地址
	 * @return
	 */
	public static String doGet(String urlStr) //throws CommonException
	{
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			//Document doc = Jsoup.connect(urlString).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get();  
			conn.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while((len = is.read(buf)) != -1){
					sb.append(new String(buf, 0, len, "UTF-8"));
				}
				is.close();
//				System.out.println(conn.getResponseCode());
//				System.out.println(sb.toString());
			}else{
				System.out.println(conn.getResponseCode());
//				throw new CommonException("访问网络失败00");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new CommonException("访问网络失败11");
		}
		return sb.toString();
	}
	
	//----------------------------------------------------------------------
	/**
	 * 指定编码类型
	 * 获取HTML数据
	 * @param urlStr url地址
	 * @return
	 */
	public static String doGetCharset(String urlStr,String charset) //throws CommonException
	{
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			//Document doc = Jsoup.connect(urlString).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get();  
			conn.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(conn.getResponseCode() == 200){
				InputStream is = conn.getInputStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while((len = is.read(buf)) != -1){
					sb.append(new String(buf, 0, len, charset));
				}
				is.close();
//				System.out.println(conn.getResponseCode());
//				System.out.println(sb.toString());
			}else{
				System.out.println(conn.getResponseCode());
//				throw new CommonException("访问网络失败00");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new CommonException("访问网络失败11");
		}
		return sb.toString();
	}
	
	
	
}
