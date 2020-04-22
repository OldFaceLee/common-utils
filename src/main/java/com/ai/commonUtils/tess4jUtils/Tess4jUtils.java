package com.ai.commonUtils.tess4jUtils;

import java.io.File;

import org.apache.log4j.Logger;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;


public class Tess4jUtils {
	private static Logger logger = Logger.getLogger(Tess4jUtils.class);
	
	/**
	 * 如果isCN 为true 那么支持识别中文， 如果为false那么不支持中文识别
	 * @param path
	 * @param isCN
	 * @return
	 */
	public static String orc(String path, boolean isCN){
		String str =null;
		try {
			File imageFile = new File(path);
			ITesseract instance = new Tesseract();
			if(isCN){
				instance.setDatapath(System.getProperty("user.dir")+File.separator+"tessdata");
				System.out.println(System.getProperty("user.dir")+File.separator+"tessdata");
				instance.setLanguage("chi_sim");
				logger.info("识别图片" + path +" 中的中文数字字母： "+instance.doOCR(imageFile));
			}else{
				logger.info("识别图片" + path +" 中的英文字母与数字为： "+instance.doOCR(imageFile));
			}
			str = instance.doOCR(imageFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	
	
}
