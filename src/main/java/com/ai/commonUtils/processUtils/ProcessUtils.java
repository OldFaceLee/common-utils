package com.ai.commonUtils.processUtils;

import java.io.IOException;

import org.apache.log4j.Logger;

public class ProcessUtils {
	
private static Logger logger = Logger.getLogger(ProcessUtils.class);
	
/**
 * 杀死windows下的进程
 * @param processName
 */
	public static void killWindowsProcess(String processName){
		String cmdCommand = "taskkill /f /im " + processName;
		try {
			Runtime.getRuntime().exec(cmdCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("杀死"+processName+"进程");
	}
	
	
	/**
	 * 执行exe文件
	 * @param exeFilePath
	 */
	public static void runExe(String exeFilePath){
		Runtime rn = Runtime.getRuntime();
		Process p = null;
		try {
			logger.info("\u8c03\u7528"+" .exe "+"\u6587\u4ef6"+ " : " +exeFilePath);
			p = rn.exec(exeFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
