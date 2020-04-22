package com.ai.commonUtils.tess4jUtils;

import com.ai.commonUtils.dateTimeUtils.DateTimeUtils;
import com.ai.commonUtils.jsonUtils.JsonUtils;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lixuejun
 * @date: Create in 2019/11/5 下午4:33
 * @description:
 */
public class App {

    public static void main(String[] args) {
        String filePath = "/Users/macos/Desktop/报销/发票2.jpeg";
        String baiduAccessToken = "24.a10692b7318f474706f0bd7a162c80cc.2592000.1575536570.282335-17699735";
        Map<String,Object> map = OrcBaiDuUtil.orcVat(filePath,baiduAccessToken);
        map.forEach((k,v)->{
            System.out.println(k+":  "+v);
        });
    }

}
