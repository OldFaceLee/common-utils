package com.ai.commonUtils.stringutils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author: lixuejun
 * @date: Create in 2020/4/13 上午9:17
 * @description:
 */
public class TestParamPool {
    private Map<String,String> map = null;

    public TestParamPool(Map<String, String> map) {
        this.map = map;
    }

    public String getString(String key){
        String value = map.get(key);
        if(" ".equals(value) || "空格".equals(value)){
            return " ";
        }else if("空".equals(value) || StringUtils.isEmpty(value) || "null".equals(value)){
            return null;
        }
        return value;
    }

    public Long getLong(String key){
        String value = map.get(key);
        Long result = Long.parseLong(value);
        return result;
    }

    public Integer getInt(String key){
        String value = map.get(key);
        Integer result = Integer.parseInt(value);
        return result;
    }

    private static Date stringToDate(String sdfPattern,String dateTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
        Date date = null;
        try {
            date = sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(date.getTime());
    }

    private static long dateToStamp(String sdfPattern, String dateTimePatten) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
        Date date = null;
        Timestamp ts = null;
        try {
            date = sdf.parse(dateTimePatten);
            ts = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts.getTime();
    }

    public long getTimeStamp(String key){
        String value = map.get(key);
        //时间格式
        String yyyy_MM_dd_HH_mm_ss_Pattern = "yyyy-MM-dd HH:mm:ss";
        //正则捕获时间格式
        String yyyy_MM_dd_HH_mm_ss = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        if(Pattern.compile(yyyy_MM_dd_HH_mm_ss).matcher(value).matches()){
            return dateToStamp(yyyy_MM_dd_HH_mm_ss_Pattern,value);
        }
        return 0L;
    }

    public Date getDate(String key){
        String value = map.get(key);
        //时间格式
        String yyyy_MM_dd_HH_mm_ss_Pattern = "yyyy-MM-dd HH:mm:ss";
        String yyyy_MM_dd_Patten = "yyyy-MM-dd";
        String HH_mm_ss_Pattern = "HH:mm:ss";
        //正则捕获时间格式
        String yyyy_MM_dd_HH_mm_ss = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        String yyyy_MM_dd = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$";
        String HH_mm_ss = "((((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        if(Pattern.compile(yyyy_MM_dd_HH_mm_ss).matcher(value).matches()){
            return stringToDate(yyyy_MM_dd_HH_mm_ss_Pattern,value);
        }else if(Pattern.compile(yyyy_MM_dd_Patten).matcher(value).matches()){
            return stringToDate(yyyy_MM_dd,value);
        }else if(Pattern.compile(HH_mm_ss).matcher(value).matches()){
            return stringToDate(HH_mm_ss_Pattern,value);
        }
        return null;
    }

    public boolean getBoolean(String key){
        String value = map.get(key);
        boolean rs = false;
        if(value.equalsIgnoreCase("true")){
            return true;
        }else if(value.equalsIgnoreCase("false")){
            return false;
        }else {
            try {
                throw new Exception("参数不为true或者false");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    public Double getDouble(String key){
        String value = map.get(key);
        Double rs = Double.parseDouble(value);
        return rs;
    }
}
