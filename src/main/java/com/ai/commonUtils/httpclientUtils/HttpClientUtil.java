package com.ai.commonUtils.httpclientUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author: lixuejun
 * @date: Create in 2020/4/13 下午12:14
 * @description:
 */
public class HttpClientUtil {
	private HttpClientUtil(){}
	private static class Singleton{
		private static final HttpClientUtil instance = new HttpClientUtil();
	}
	public static HttpClientUtil getInstance(){
		return Singleton.instance;
	}

	private final Logger log = Logger.getLogger(HttpClientUtil.class);
	private CloseableHttpClient httpClient;
	private CloseableHttpResponse httpResponse;
	private HttpEntity httpEntity;
	private JSONObject jsonObject;

	/**
	 * 公共返回响应
	 * @return
	 */
	private JSONObject response(){
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		try {
			if( statusCode == 200){
				jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
				log.info("httpResponseCode=200");
				log.info("responseBody="+ JSON.toJSONString(jsonObject));
			}else {
				jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
				log.info("httpResponseCode="+statusCode);
				log.info("responseBody="+ JSON.toJSONString(jsonObject));
				throw new Exception("httpResponseCode="+statusCode+",不是200");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
					log.info("关闭CloseableHttpClient对象");

				}
				if(httpResponse != null){
					httpResponse.close();
					log.info("关闭CloseableHttpResponse对象");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}


	/**
	 * 模拟上传文件，"Content-Type","application/octet-stream" 类型
	 * @param url
	 * @param header
	 * @param filePath
	 * @return
	 */
	public JSONObject postUploadFile(String url, Map<String,String> header,String filePath){
		InputStream inputStream = null;
		try {
			log.info("传入的文件地址="+filePath);
			inputStream = new FileInputStream(new File(filePath));
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_ENCODING,"UTF-8");
			log.info("设置header请求编码=UTF-8");
			log.info("传入的header参数="+JSON.toJSONString(header));
			header.forEach((k,v)->{
				httpPost.addHeader(k,v);
			});
			httpPost.setEntity(new InputStreamEntity(inputStream));
			httpResponse = httpClient.execute(httpPost);
			log.info("模拟http请求...");
			httpEntity = httpResponse.getEntity();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(inputStream != null){
					inputStream.close();
					log.info("关闭文件流InputStream对象");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response();
	}

	/**
	 * 私有方法，抽象Post——Common
	 * @param url
	 * @param header
	 * @param jsonParam
	 */
	private void postCommon(String url,Map<String,String>header,String jsonParam){
		try {
			HttpPost httpPost = new HttpPost(url);
			httpClient = HttpClients.createDefault();
			header.forEach((k,v)->{
				httpPost.addHeader(k,v);
			});
			log.info("传入的URi="+url);
			log.info("传入的header参数="+JSON.toJSONString(header));
			log.info("传入的requestBodyJsonString="+jsonParam);
			StringEntity stringEntity = new StringEntity(jsonParam,"UTF-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 传入jsonParam的post
	 * @param url
	 * @param header
	 * @param jsonParam
	 * @return
	 */
	public JSONObject post(String url,Map<String,String>header,String jsonParam){
		postCommon(url,header,jsonParam);
		return response();
	}

	/**
	 * 传入map风格的post
	 * @param url
	 * @param header
	 * @param param
	 * @return
	 */
	public JSONObject post(String url,Map<String,String>header,Map<String,Object>param){
		String jsonParam = JSON.toJSONString(param);
		postCommon(url,header,jsonParam);
		return response();
	}


	/**
	 * get请求
	 * @param url
	 * @param header
	 * @return
	 */
	public JSONObject get(String url,Map<String,String>header){
		try {
			log.info("传入的URi="+url);
			log.info("传入的header="+JSON.toJSONString(header));
			httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			header.forEach((k,v)->{
				httpGet.addHeader(k,v);
			});
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response();
	}

	/**
	 * httpclient模拟post(applecation/x-www-form-urlencoded方式)请求，如下是使用方法
	 * 将其List<NameValuePair>封装成Map<String,Object>
	 * @param url
	 * @param param
	 * @return
	 */
	public String postForm(String url,Map<String,Object> param){
		String result = "";
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded"); // 添加请求头
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = param.entrySet().iterator();
			while (iterator.hasNext()){
				Map.Entry<String,Object> elem = (Map.Entry<String, Object>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(),elem.getValue().toString()));
			}
			if(list.size() > 0){
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"utf-8");
				httpPost.setEntity(entity);
			}
			System.out.println("请求头: "+httpPost.getEntity().toString());
			System.out.println("请求体:" +list.toString());
			httpResponse = httpclient.execute(httpPost);
			if(httpResponse != null){
				HttpEntity resEntity = httpResponse.getEntity();
				if(resEntity !=null){
					result = EntityUtils.toString(resEntity,"utf-8");
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

}

