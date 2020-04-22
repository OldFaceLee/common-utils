package com.ai.commonUtils.weChatUtils;

import java.util.HashMap;
import java.util.Map;

import com.ai.commonUtils.httpclientUtils.HttpClientUtil;
import org.apache.log4j.Logger;


import com.ai.commonUtils.jsonUtils.JsonUtils;

public class WeChatUtils {
		private static Logger logger = Logger.getLogger(WeChatUtils.class);
		private static String agentId = "1000002";
		private static String secret = "BDluJ3-0Ui2AEbzzLT8aEjwFUXaoAcNuMRKFj4D3O8M";
		private static String corpID = "wwb39e23949d6b4c77";
		private static String getAccessTokenURL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpID+"&corpsecret="+secret;
		private static String sendMsgURL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
		
		private static String getAccessToken() throws Exception{
			Map<String,String> header = new HashMap<String, String>();
			logger.info("获取了微信的accessToken: "+ HttpClientUtil.getInstance().get(getAccessTokenURL, header).get("access_token"));
			return HttpClientUtil.getInstance().get(getAccessTokenURL, header).get("access_token").toString();
		}
		
		/**
		 * 
		 * @param msg
		 * @throws Exception
		 */
		public static void weChatMsgSend(String msg) throws Exception{
			Map<String,String> header = new HashMap();
			Map<String,Object> param = new HashMap();
			param.put("msgtype", "text");
			param.put("agentid", agentId);
			param.put("touser", "@all");
			Map<String,Object> textValue = new HashMap();
			textValue.put("content", msg);
			param.put("text", JsonUtils.mapConvertJsonObject(textValue));
			HttpClientUtil.getInstance().post(sendMsgURL+getAccessToken(), header, param);
			logger.info("向企业微信发送消息");
		}
		
		public static void main(String[] args) throws Exception {
			weChatMsgSend("sdfsadfasd");
		}
		

}
