package com.ai.commonUtils.qrUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.log4j.Logger;

public class QRCodeGenerator {

    private static Logger log  = Logger.getLogger(QRCodeGenerator.class);

    /**
     * 生成QR 二维码， content可以为链接地址， wight 与 height 可以设置大一点， 例如500
     * @param content
     * @param wight
     * @param height
     * @param imageGenneratePath
     */
    public static void generateQRCodeImage(String content,int wight,int height,String imageGenneratePath){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE,wight,height,hints);
            Path path = FileSystems.getDefault().getPath(imageGenneratePath);
            try {
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                log.info("生成二维码图片地址："+imageGenneratePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generateQRCodeImage("可以存放任何内容",500,500,"C:\\Users\\lixuejun\\Desktop\\qr.png");
    }
}
