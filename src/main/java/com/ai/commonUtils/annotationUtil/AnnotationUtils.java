package com.ai.commonUtils.annotationUtil;

import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 将其testNG里边的groups注解的值取出来 对应到指定的方法上
 */
public class AnnotationUtils {

    public static void main(String[] args) {
        Map<String,List<String>>  list = getAnnotations("com.ai.commonUtils.annotationUtil.TestAnnotation");
        System.out.println(list);
    }

    public static Map<String,List<String>> getAnnotations(String clazzLocation){
        Map<String,List<String>> map = new HashMap<>();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(clazzLocation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] declaredMethods = clazz.getDeclaredMethods();
        String [] annStrs = null;
        String [][] rememberAnnStrs =  null;
        for (int i = 0; i < declaredMethods.length; i++) {
            boolean isTestNGAnnPresent = declaredMethods[i].isAnnotationPresent(Test.class);
            if(isTestNGAnnPresent){
                Test testAnn =  declaredMethods[i].getAnnotation(Test.class);
                annStrs = testAnn.groups();
                rememberAnnStrs = new String[][]{
                        annStrs
                };
                for(String[] strings : rememberAnnStrs){
                    map.put(declaredMethods[i].getName(),Arrays.asList(strings));
                }
            }
        }

        return map;
    }
}
