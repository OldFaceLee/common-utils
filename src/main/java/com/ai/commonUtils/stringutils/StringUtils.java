package com.ai.commonUtils.stringutils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/15 下午5:06
 * @description:
 */
public class StringUtils {

    /**
     * 判断字符的长度
     *
     * @param s 字符串
     * @return 返回长度
     */
    public static int getStringLength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 判断中文和英文
     *
     * @param c 字符
     * @return true为中文，false为英文
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 包括座机  后者 手机
     * @param str
     * @return
     */
    public static String getStrNotContainPhoneNumber(String str){
        String subStr = null;

        String regex = "1[35789]\\d{9}"; //验证手机号
        Pattern pMobile = Pattern.compile(regex);
        Matcher mobile = pMobile.matcher(str);
        List<String> phoneNumber = new ArrayList<>();
        while (mobile.find()) { //一定需要先查找再调用group获取电话号码
            phoneNumber.add(mobile.group());
        }

        String prexRegex = "[0]{1}[0-9]{2,3}-[0-9]{7,8}"; //验证区号
        Pattern pPrexPhone = Pattern.compile(prexRegex);
        Matcher prexPhone = pPrexPhone.matcher(str);
        List<String> prexPhoneNumber = new ArrayList<>();
        while (prexPhone.find()) {
            prexPhoneNumber.add(prexPhone.group());
        }
        List<String> size = phoneNumber.size()!=0 ? phoneNumber : prexPhoneNumber;
        for (int i = 0; i < size.size(); i++) {
            if (str.contains(size.get(i))) {
                subStr = str.substring(0,
                        size.size() != 0 ? str.length() - size.get(i).length():null);
            }
        }
        return subStr.trim();
    }


    public static void main(String[] args) {
        System.out.println(getInteger("3456"));
    }
    /**
     * 获取"空" ， "空格" ，"null"的string
     * @return
     */
    public static String getString(String key){
        String value = null;
        if(" ".equals(key)){
            System.out.println("返回空格");
            return " ";
        }else if("".equals(key)){
            System.out.println("返回\"\"空值");
            return "";
        }else if(org.apache.commons.lang3.StringUtils.isBlank(key)){
            System.out.println("返回org.apache.commons.lang3.StringUtils.isBlank的null");
            return null;
        }
        else {
            value = key;
        }
        return value;
    }

    public static Long getLong(String key){
        return Long.valueOf(getString(key));
    }

    public static Integer getInteger(String key){
        return Integer.valueOf(getString(key));
    }


}
