package com.ai.commonUtils.base64Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/27 下午2:48
 * @description:
 */
public class Base64Util {

    /**
     *
     * @param bytes
     * @return
     */
    public static String base64Encode(byte [] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static void main(String[] args) {

    }

    public static String encodeBase64Str(String str){
        String base64Str = null;
        try {
            base64Str = new BASE64Encoder().encode(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64Str;
    }

    public static String decodeBase64Str(String base64Str){
        byte[] strByte = null;
        try {
            strByte = new BASE64Decoder().decodeBuffer(base64Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(strByte);
    }

    public static String encodeBase64File(File file){
        byte[] buffer = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(file);
            buffer = new byte[(int)file.length()];
            fis.read(buffer);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(buffer);
    }

    public static void decodeBase64File(String base64Str,String filePath){
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Str);
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
