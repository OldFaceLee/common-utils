package com.ai.commonUtils.pdfUtil.convertUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/28 上午11:21
 * @description:
 */
public class PdfUtil {

    private static final Logger log = Logger.getLogger(PdfUtil.class);

    /**
     * 将其pdf文件添加水印
     * @param sourceFilePath
     * @param targetFilePath
     * @param waterMarkName
     */
    public static void waterMarkPdf(String sourceFilePath,String targetFilePath,String waterMarkName){
        try {
            PdfReader reader = new PdfReader(sourceFilePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    targetFilePath));
            //这里的字体设置比较关键，这个设置是支持中文的写法
            BaseFont base = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 使用系统字体
            int total = reader.getNumberOfPages() + 1;

            PdfContentByte under;
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                pageRect = stamper.getReader().
                        getPageSizeWithRotation(i);
                // 计算水印X,Y坐标
                float x = pageRect.getWidth()/2;
                float y = pageRect.getHeight()/2;
                // 获得PDF最顶层
                under = stamper.getOverContent(i);
                under.saveState();
                // set Transparency
                PdfGState gs = new PdfGState();
                // 设置透明度为0.2
                gs.setFillOpacity(1.f);
                under.setGState(gs);
                under.restoreState();
                under.beginText();
                under.setFontAndSize(base, 60);
                under.setColorFill(BaseColor.LIGHT_GRAY);

                // 水印文字成45度角倾斜
                under.showTextAligned(Element.ALIGN_CENTER
                        , waterMarkName, x,
                        y, 55);
                // 添加水印文字
                under.endText();
                under.setLineWidth(1f);
                under.stroke();
                log.info(String.format("添加水印[%s]成功",waterMarkName));
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换为pdf
     * @param soureFilePath
     * @param pdfFilePath
     */
    public static void imgConvertPdf(String soureFilePath,String pdfFilePath){
        FileInputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        Document document = new Document();
        try {
            inputStream = new FileInputStream(new File(soureFilePath));
            fileOutputStream = new FileOutputStream(new File(pdfFilePath));
            PdfWriter.getInstance(document,fileOutputStream);
            document.open();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, arrayOutputStream);
            Image image = null;
            image = Image.getInstance(arrayOutputStream.toByteArray());
            //重新设置宽高
            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            //重新设置宽高
            float documentHeight = documentWidth * image.getHeight()/image.getWidth();
            image.scaleAbsolute(documentWidth, documentHeight);
            document.add(image);
            log.info("转换img图片为pdf成功");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        String source = "/Users/macos/Desktop/a/img.png";
        String to = "/Users/macos/Desktop/a/img.pdf";
        imgConvertPdf(source,to);
//        waterMarkPdf(source,to,"自动化测试项目组");

    }
}
