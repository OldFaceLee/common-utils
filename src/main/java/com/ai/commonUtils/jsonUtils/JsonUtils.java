package com.ai.commonUtils.jsonUtils;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import net.sf.json.JSONArray;


public class JsonUtils {

	public static void main(String[] args) {
		/*String gatewayServiceUrl = "https://xin-sandbox.asiainfo.com:16020/oauth2/partner/authorize";
		String serviceUname = "ai-crm-oss";
		String servicePwd = "f379eaf3c831b04de153469d1bec345e";
		String clientSecret = "JDSZ2psTVa0Sbv0oNKF7fVz7ipLtOtA1";
		String clientId = "u2ru3LGpbYJiniiUn1BTDh3Q1SBB51WD";

		Auth2Body auth2Body = new Auth2Body();
		auth2Body.setUsername(serviceUname);
		auth2Body.setPassword(servicePwd);
		auth2Body.setClient_secret(clientSecret);
		auth2Body.setClient_id(clientId);
		String jsonBody = JSON.toJSONString(auth2Body);

		System.out.println(jsonBody);


		Map<String,String> header = new HashedMap();
		header.put("Content-Type","application/json");
		header.put("Authorization","Basic "+ Base64.getEncoder().encodeToString(String.join(":", "ai-crm-common", "ai-crm-common").getBytes(Charset.defaultCharset())));

		try {
			net.sf.json.JSONObject re = HttpClientUtils.post(gatewayServiceUrl,header,jsonBody);
			System.out.println(re);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	/**
	 * 将map类型转成com.alibaba.fastjson.JSONObject
	 * @param map
	 * @return
	 */
	public static JSONObject mapConvertJsonObject( Map <String, Object> map){
		return JSONObject.parseObject(JSON.toJSONString(map));
	}
	
	/**
	 * 将List类型转为com.alibaba.fastjson.JSONObject
	 * @param list
	 * @return
	 */
	public static JSONArray listConvertJsonArray(List list){
		return JSONArray.fromObject(list);
	}
	
	/**
	 * 将Object[] Array类型com.alibaba.fastjson.JSONObject
	 * @param array
	 * @return
	 */
	public static JSONArray arrayConvertJsonArray (Object [] array){
		return JSONArray.fromObject(array);
	}
	
	/**
	 * 读取json文件，返回com.alibaba.fastjson.JSONObject
	 * @param jsonFile
	 * @return
	 * @throws IOException
	 */
	public static JSONObject readJsonFile(String jsonFile) throws IOException{
		File file=new File(jsonFile);
        String content= FileUtils.readFileToString(file, "UTF-8");
        JSONObject json = JSON.parseObject(content);
        return json;	
	}
	
	/**
	 * @Description 获取json数据中含有一个jsonArray数据里边的key对应的value值
	 * @return
	 * @throws Exception 
	 */
	public static String getJsonArrayKey(net.sf.json.JSONObject ob,String mainJsonKey, String jsonArrayKey,String jsonArrayKeySubKey) throws Exception{
		StringBuilder returnString = new StringBuilder();
		if(ob != null){
			for(int i=0; i<ob.getJSONObject(mainJsonKey).getJSONArray(jsonArrayKey).size();i++){
				returnString.append(JSONObject.parseObject(ob.getJSONObject(mainJsonKey).getJSONArray(jsonArrayKey).getString(i)).get(jsonArrayKeySubKey).toString()).append("\n");	
			}
		}else{
			throw new Exception(ob.toString() + "is null");
		}

		return returnString.substring(0, returnString.length());
	}

	public HashMap<String, Object> jsonToMap(String jsonStr){
		HashMap<String, Object> map = new HashMap<>();
		JSONObject json = JSONObject.parseObject(jsonStr);
		for(Object k : json.keySet()){
			Object v = json.get(k);
			if(v instanceof com.alibaba.fastjson.JSONArray){
				List<Map<String, Object>> list = new ArrayList<>();
				Iterator it = ((com.alibaba.fastjson.JSONArray)v).iterator();
				while(it.hasNext()){
					Object json2 = it.next();
					list.add(jsonToMap(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}



	
	

}
