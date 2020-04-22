package com.ai.commonUtils.okclient.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;


/**
 * @author: lixuejun
 * @date: Create in 2019/11/11 下午2:36
 * @description: OKHttp 工具类
 */
public class OkHttpUtil {

    private static final transient Logger log = Logger.getLogger(OkHttpUtil.class);
    private static Response response = null;
    private static Headers headers = null;
    private static RequestBody requestBody = null;

    public static void main(String[] args) {
        String url = "https://karagw-inner.asiainfo.com/api/v1.0.0/mdm/ops/employee/getEmployeeByNT/chenzq7";
//        String url = "https://xin-sandbox.asiainfo.com:16020/api/v1.0.0/auth/login";
//        String url = "https://karagw.asiainfo.com/api/v1.0.0/file/upload/icon";
        Map<String, String> header = new HashMap<>();
//        header.put("Content-Type","application/json");
        header.put("authorization","bearer awJfN6b8y66TGr6IhajrP0j6l76ScOGN");
        System.out.println(get(url));

    }


    /**
     * 共用设置header的方法封装
     * @param headersParams
     * @return
     */
    private static Headers setHeaders(Map<String, String> headersParams) {
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                log.info(String.format("设置Headers其中[key] = [%s], [value] = [%s]", key, headersParams.get(key)));
            }
        }
        headers = headersbuilder.build();

        return headers;
    }

    /**
     * 共用方法设置requestBody
     * @param BodyParams
     * @return
     */
    private RequestBody SetRequestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }

    /**
     * 返回共用的response.string
     * @param request
     * @return
     */
    private static String response(Request request){
        String responseStr = null;
        try {
            response = OkHttpInstance.getClient().newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                log.info(String.format("[HTTP_CODE] = [%s]",status));
                responseStr = response.body().string();
                log.info(String.format("[RESPONSE_STRING] =%s",responseStr));
                return responseStr;
            }else {
                log.info(String.format("[HTTP_CODE] = [%s]",status));
                responseStr = response.body().string();
                log.info(String.format("[RESPONSE_STRING] =%s",responseStr));
                return responseStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                log.info("response不为空，关闭response");
                response.close();
            }
        }
        return responseStr;
    }

    /**
     * get请求，只传url
     * @param url
     * @return
     */
    public static JSONObject get(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        return JSONObject.parseObject(response(request));
    }

    /**
     * 直接传url,headers可为空
     * @param url
     * @param headerParams
     * @return
     */
    public static JSONObject get(String url, Map<String, String> headerParams) {
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headerParams))
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s", JSON.toJSONString(headerParams)));
        return JSONObject.parseObject(response(request));
    }


    /**
     * GET，url 带参数，headers可为空
     * @param url
     * @param headerParams
     * @param urlParams
     * @return
     */
    public static JSONObject get(String url, Map<String, String> headerParams, Map<String, Object> urlParams) {
        StringBuffer sb = new StringBuffer(url);
        if (urlParams != null && urlParams.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = urlParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Request request = new Request.Builder()
                .url(sb.toString())
                .headers(setHeaders(headerParams))
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s", JSON.toJSONString(headerParams)));
        return JSONObject.parseObject(response(request));
    }


    /**
     * post,传入stringJson
     * @param url
     * @param headers
     * @param jsonParams
     * @return
     */
    public static JSONObject post(String url, Map<String,String> headers, String jsonParams){
        requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headers))
                .post(requestBody)
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s",JSON.toJSONString(headers)));
        log.info(String.format("[RequestBody] = %s",jsonParams));
        return JSONObject.parseObject(response(request));
    }

    /**
     * post请求， 模拟application/json
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static JSONObject post(String url,Map<String,String> headers, Map<String,Object> params){
        requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JSON.toJSONString(params));
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headers))
                .post(requestBody)
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s",JSON.toJSONString(headers)));
        log.info(String.format("[RequestBody] = %s",JSON.toJSONString(params)));
        return JSONObject.parseObject(response(request));
    }

    /**
     * post模拟(applecation/x-www-form-urlencoded方式)请求
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postForm(String url, Map<String,String>headers,Map<String,Object>params){
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key).toString());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(headers))
                .post(builder.build())
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s",headers));
        log.info(String.format("[RequestBody-form] = %s",params));
        return response(request);
    }

    /**
     * post模拟上传文件， application/octet-stream
     * @param url
     * @param file
     * @param header
     * @return
     */
    public static JSONObject post(String url, File file, Map<String,String> header){
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        /*requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .build();*/
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeaders(header))
                .post(fileBody)
                .build();
        log.info(String.format("[RequestURL] = %s", request.url()));
        log.info(String.format("[RequestHeader] = %s",JSON.toJSONString(header)));
        return JSONObject.parseObject(response(request));
    }

    public static <T> T executeRequest(Request request, Class<T> clazz){
        String url = request.url().toString();
        OkHttpClient client = OkHttpInstance.getClient();
        String result = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if( null == response || null == response.body()){
                log.error("调用接口["+url+"]异常:返回Response为空");
                return null;
            }
            result = response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null){
                response.close();
            };
        }
        if(StringUtils.isBlank(result)){
            return null;
        }
        return JSONObject.parseObject(result, clazz);
    }

}
