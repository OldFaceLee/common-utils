package com.ai.commonUtils.randomUtils;

import java.util.Random;

public class RandomUtils {
	
	/**
	 * 给出count, 获取count里边的随机值
	 * @param count
	 * @return
	 */
	 public static int getRandom(int count){
			return (int)Math.round(Math.random()*(count-1));
		}
	 
	 /**
	  * 随机的生成长度为length的字符串
	  * @param length
	  * @return
	  */
	 public static String generateRandomString(int length) {
			StringBuffer sb = new StringBuffer();
			Random random = new Random();

			for (int i = 0; i < length; ++i) {
				sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
						.charAt(random.nextInt("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
			}

			return sb.toString();
		}
	 /**
	  * 随机生成长度为length的数字
	  * @param length
	  * @return
	  */
		public static String generateRandomNumber(int length) {
			StringBuffer sb = new StringBuffer();
			Random random = new Random();

			for (int i = 0; i < length; ++i) {
				sb.append("0123456789".charAt(random.nextInt("0123456789".length())));
			}

			return sb.toString();
		}


	public static void main(String[] args) {
		System.out.println(generateRandomString(50));
	}

}
