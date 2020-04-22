package com.ai.commonUtils.NumberFormatUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberFormatUtils {

    private static String decimalFormatPatternThousandthDisplays = ",###,##0.00";  //千分位保留两位小数


    /**
     * 千分位显示保留两位小数
     * @param
     * @return
     */
    public static String getThousandth(Object value){
        /*DecimalFormat df=new DecimalFormat(decimalFormatPatternThousandthDisplays);
        return df.format(Double.parseDouble(number));*/
        if (value == null) {
            return "0.00";
        }
        DecimalFormat addPattenFormat = new DecimalFormat();
        addPattenFormat.applyPattern(decimalFormatPatternThousandthDisplays);//#,###.00
        BigDecimal bigDecimal = new BigDecimal(value.toString());
        return ".00".equals(addPattenFormat.format(bigDecimal)) ? "0.00" : addPattenFormat.format(bigDecimal);

    }

    /**
     * 对Double类型数值进行格式转换，不以科学计数法表示
     *
     * @param number 需要转换的Double数值
     * @return
     */
    public static String formatDoubleNumber(Double number){
        if(number == null){
            return "null";
        }else{
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置
            return decimalFormat.format(number);
        }
    }

    public static String formatDoubleNumber(Double number, int percent){
        if(number == null){
            return "null";
        }else{
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置
            return decimalFormat.format(number*percent);
        }
    }

    /**
     * 对Long类型数值进行格式转换，处理null
     *
     * @param number 需要转换的Double数值
     * @return
     */
    public static String formatLongNumber(Long number){
        return number==null?"0":number.toString();
    }

    /**
     * 对Integer类型数值进行格式转换，处理null
     *
     * @param number 需要转换的Integer数值
     * @return
     */
    public static String formatIntegerNumber(Integer number){
        return number==null?"0":number.toString();
    }

    /**
     * 对Integer类型数值进行格式转换，处理null
     *
     * @param number 需要转换的Integer数值
     * @return
     */
    public static String formatBigDecimalNumber(BigDecimal number){
        if(number == null){
            return "0";
        }else{
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return df.format(number);
        }
    }

    /**
     * 对Double类型数值进行格式转换，不以科学计数法表示
     *
     * @param number1 分子
     * @param number2 分母
     * @return
     */
    public static String getDoublePercent(Double number1, Double number2){
        String percent = "0";
        if(!(number1==null || number2 == null)){
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(0);
            double number = number1.doubleValue() / number2.doubleValue() * 100;
            if(number>0 && number<1){
                percent = "1";
            }else{
                percent = numberFormat.format(number);
            }
        }
        return percent;
    }

    public static String getZeroDoubleNumStr(){
        return "0.00";
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("\\d+(.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
