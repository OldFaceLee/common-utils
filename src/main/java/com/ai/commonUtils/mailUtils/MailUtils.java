package com.ai.commonUtils.mailUtils;

import org.apache.log4j.Logger;

import java.util.Properties;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {

	private static final Logger logger = Logger.getLogger(MailUtils.class);
	
	
		
	  /**
	   * 发送邮件
	   * @param smtpIp
	   * @param sendUser
	   * @param password
	   * @param torecipientsList
	   * @param mailSubject
	   * @param mailContent
	   * @throws Exception
	   */
	  public static void sendMail(String smtpIp,String sendUser, String password, 
	    		String torecipientsList, String toCCPerson, String toBccPerson,
	    		String mailSubject, String mailContent) {
	    	
	    	// 配置发送邮件的环境属性
	    	final Properties props = new Properties();
	    	// 表示SMTP发送邮件，需要进行身份验证
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.host", smtpIp);
	    	// 发件人的账号
	    	props.put("mail.user", sendUser);
	    	// 访问SMTP服务时需要提供的密码
	    	props.put("mail.password", password);


	    	// 构建授权信息，用于进行SMTP进行身份验证
	    	Authenticator authenticator = new Authenticator() {
	    	    @Override
	    	    protected PasswordAuthentication getPasswordAuthentication() {
	    	        // 用户名、密码
	    	        String userName = props.getProperty("mail.user");
	    	        String password = props.getProperty("mail.password");
	    	        return new PasswordAuthentication(userName, password);
	    	    }
	    	};
		  logger.info("smtpIP:"+props.getProperty("mail.smtp.host"));
		  logger.info("sendUser:"+props.getProperty("mail.user"));
		  logger.info("sendPwd:"+props.getProperty("mail.password"));
	    	// 使用环境属性和授权信息，创建邮件会话
	    	Session mailSession = Session.getInstance(props, authenticator);
	    	// 创建邮件消息
	    	MimeMessage message = new MimeMessage(mailSession);
	    	// 设置发件人
		    InternetAddress form = null;
		  try {
			  form = new InternetAddress(props.getProperty("mail.user"));
		  } catch (AddressException e) {
			  e.printStackTrace();
		  }
		  try {
			  message.setFrom(form);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  // 设置收件人
		  String toList = torecipientsList;
		  InternetAddress[] iaToList = new InternetAddress[0]; // 设置多个收件人
		  try {
			  iaToList = new InternetAddress().parse(toList);
		  } catch (AddressException e) {
			  e.printStackTrace();
		  }
		  try {
			  message.setRecipients(RecipientType.TO, iaToList);
			  logger.info("收件人："+toList);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  // 设置抄送
		  InternetAddress[] iaToListCC = new InternetAddress[0]; // 设置多个收件人
		  try {
			  iaToListCC = new InternetAddress().parse(toCCPerson);
		  } catch (AddressException e) {
			  e.printStackTrace();
		  }
		  try {
			  message.setRecipients(RecipientType.CC, iaToListCC);
			  logger.info("CC："+toCCPerson);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  // 设置密送，其他的收件人不能看到密送的邮件地址
		  InternetAddress[] iaToListBCC = new InternetAddress[0]; // 设置多个收件人
		  try {
			  iaToListBCC = new InternetAddress().parse(toBccPerson);
		  } catch (AddressException e) {
			  e.printStackTrace();
		  }
		  try {
			  message.setRecipients(RecipientType.BCC, iaToListBCC);
			  logger.info("BCC："+toBccPerson);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  // 设置邮件标题
		  try {
			  message.setSubject(mailSubject);
			  logger.info("邮件标题： "+mailSubject);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  try {
			  message.setContent(mailContent, "text/html;charset=UTF-8");
			  logger.info("邮件正文strings:" + mailContent);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

		  // 发送邮件
		  try {
			  Transport.send(message);
		  } catch (MessagingException e) {
			  e.printStackTrace();
		  }

	  }

	public static void main(String[] args) {
		MailUtils.sendMail("mail.asiainfo.com","lixj5@asiainfo.com","cashc0wA3",
				"lixj5@asiainfo.com,lixuejun_email@126.com","lixj5@asiainfo.com,lixuejun_email@126.com"
		,"lixj5@asiainfo.com,lixuejun_email@126.com","邮件主题","邮件正文内容");
	}

}
