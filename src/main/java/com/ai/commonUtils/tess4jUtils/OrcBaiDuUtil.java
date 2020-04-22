package com.ai.commonUtils.tess4jUtils;

import com.ai.commonUtils.dateTimeUtils.DateTimeUtils;
import com.ai.commonUtils.httpclientUtils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lixuejun
 * @date: Create in 2019/11/5 下午6:36
 * @description:
 */
public class OrcBaiDuUtil {
    public enum VAT{
        INVOICE_TYPE("INVOICE_TYPE","发票类型"),
        INVOICE_DATE("INVOICE_DATE","开票日期"),
        INVOICE_CODE("INVOICE_CODE","发票代码"),
        INVOICE_NUM("INVOICE_NUM","发票号码"),
        TOTAL_AMOUNT("TOTAL_AMOUNT","总金额"),
        TAX("TAX","税额"),
        AMOUNT_NO_TAX("AMOUNT_NO_TAX","不含税金额"),
        INVOICE_INFO("INVOICE_INFO","发票明细"),
        BUYER_NAME("BUYER_NAME","购买方名称"),
        SELLER_NAME("SELLER_NAME","销售方名称"),
        CHECK_CODE("CHECK_CODE","检查码");

        VAT(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        String value;
        String desc;
    }


    /**
     * 获取百度orc增值税发票应用的token
     * @return
     */
    private static String getBaiduAccessToken(){
        com.alibaba.fastjson.JSONObject jsonObject = null;
        //增值税发票应用token获取
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=oIWuHsuKgazU9QL4Tu9KGqQj&client_secret=2YaAsaEXgopzaDIzSB1RslzHLVca5TsA";
        try {
            jsonObject = HttpClientUtil.getInstance().get(url,new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.get("access_token").toString();
    }

    /**
     * 识别增值税发票
     *
     * @return
     */
    public static Map<String, Object> orcVat(String filepath, String baiduAccessToken) {
        Map<String,Object> vatMap = new HashMap<String,Object>();
        String wordsResultObject = "words_result";
        InputStream is = null;
        try {
            is = new FileInputStream(new File(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = getInvoiceInfo(is,baiduAccessToken);

        JSONObject job = JSONObject.fromObject(s);
        vatMap.put(VAT.INVOICE_TYPE.getDesc(), job.getJSONObject(wordsResultObject).get("InvoiceTypeOrg"));//发票类型
        vatMap.put(VAT.INVOICE_DATE.getDesc(),new SimpleDateFormat("yyyy-MM-dd").format(DateTimeUtils.stringToDate("yyyy年MM月dd日",job.getJSONObject(wordsResultObject).get("InvoiceDate").toString()))); //开票日期
        vatMap.put(VAT.INVOICE_CODE.getDesc(),job.getJSONObject(wordsResultObject).get("InvoiceCode")); //发票代码
        vatMap.put(VAT.INVOICE_NUM.getDesc(),job.getJSONObject(wordsResultObject).get("InvoiceNum")); // 发票号码
        vatMap.put(VAT.TOTAL_AMOUNT.getDesc(),job.getJSONObject(wordsResultObject).get("AmountInFiguers"));//总金额
        vatMap.put(VAT.TAX.getDesc(),job.getJSONObject(wordsResultObject).get("TotalTax")); //税额
        vatMap.put(VAT.AMOUNT_NO_TAX.getDesc(),job.getJSONObject(wordsResultObject).get("TotalAmount")); // 不含税金额
        JSONArray jsonArray = job.getJSONObject(wordsResultObject).getJSONArray("CommodityName");
        List<JSONObject> listObj = new ArrayList<>(); // 发票明细
        for (int i = 0; i < jsonArray.size(); i++) {
            listObj.add((JSONObject) jsonArray.get(i));
            vatMap.put(VAT.INVOICE_INFO.getDesc(),listObj.get(i).get("word"));
        }

        vatMap.put(VAT.BUYER_NAME.getDesc(),job.getJSONObject(wordsResultObject).get("PurchaserName")); //购买方名称
        vatMap.put(VAT.SELLER_NAME.getDesc(),getStrNotContainPhoneNumber(job.getJSONObject(wordsResultObject).get("SellerAddress").toString())); // 销售方名称、电话
        vatMap.put(VAT.CHECK_CODE.getDesc(),job.getJSONObject(wordsResultObject).get("CheckCode")); //检查码
        return Optional.ofNullable(vatMap).orElse(Collections.emptyMap());
    }

    /**
     * https://console.bce.baidu.com/ai/?_=1572951670637&fromai=1#/ai/ocr/app/list 获取client_secret 与
     * client_id
     * @param base64UrlencodedImg
     * @param baiduAccessToken
     * @return
     */
    private static String sendOrc(String base64UrlencodedImg, String baiduAccessToken) {
        CloseableHttpClient httpclient = HttpClients.createMinimal();
        HttpPost post = null;
        CloseableHttpResponse response = null;
        post = new HttpPost("https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice?access_token=" + baiduAccessToken);
        Header header = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader(header);

        try {
            HttpEntity entity = new StringEntity("image=" + base64UrlencodedImg);
            post.setEntity(entity);
            response = httpclient.execute(post);

         /*   *//**
             * 自动判断token是否有效，是否过期
             *//*
            JSONObject checkToken = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
            for(Object j : checkToken.keySet()){
                if (j.equals("error_msg")) {
                    System.out.println("baidu的token,error，重新申请赋值");
                    baiduAccessToken = getBaiduAccessToken();
                    post = new HttpPost("https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice?access_token=" + baiduAccessToken);
                    response = httpclient.execute(post);
                }
            }*/
            InputStream in = response.getEntity().getContent();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            byte[] buffer = bos.toByteArray();

            return new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInvoiceInfo(InputStream in, String baiduAccessToken) {
        byte[] fileByte = getFileBytes(in);// 获取图片字节数组
        String base64UrlencodedImg = base64Urlencode(fileByte);// 编码
        return sendOrc(base64UrlencodedImg, baiduAccessToken);// 发送给百度进行文字识别
    }

    /**
     * 图片转字节数组
     *
     * @return 图片字节数组
     */
    private static byte[] getFileBytes(InputStream in) {
        byte[] buffer = null;
        try {
            // File file = new File(filePath);
            // FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 对字节数组进行base64编码与url编码
     *
     * @param b
     * @return
     */
    private static String base64Urlencode(byte[] b) {
        byte[] base64Img = Base64.getEncoder().encode(b);
        try {
            String base64UrlencodedImg = URLEncoder.encode(new String(base64Img), "utf-8");
            return base64UrlencodedImg;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * InputStream 转String
     *
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String getStrNotContainPhoneNumber(String str){
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
}