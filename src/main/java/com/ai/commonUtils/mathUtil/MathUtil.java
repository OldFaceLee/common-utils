package com.ai.commonUtils.mathUtil;

import java.math.BigDecimal;

/**
 * @Author lixuejun19
 * @Date 2020/6/11 16:38
 * @Description:
 */
public class MathUtil {

    public static void main(String[] args) {
        int x = 4;
        int y = 1;

        double result = Math.sqrt(Math.pow((double)(x-y),2));

        BigDecimal a = new BigDecimal(Double.toString(5.001));
        BigDecimal b = new BigDecimal(Double.toString(5.0001));
        System.out.println(a.add(b).doubleValue());




    }
}
