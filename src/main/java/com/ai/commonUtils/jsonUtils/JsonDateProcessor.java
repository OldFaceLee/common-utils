package com.ai.commonUtils.jsonUtils;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 由于net.sf.json  对转化Date类型时不正确 做出一些处理
 */
public class JsonDateProcessor implements JsonValueProcessor {
    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    private Object process(Object obj) {
        if (obj == null) {// 如果时间为null，则返回空字串
            return "";
        }
        if (obj instanceof Date) {
            obj = new java.util.Date(((Date) obj).getTime());
        }
        if (obj instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINA);// 格式化时间为yyyy-MM-dd类型
            return sdf.format(obj);
        } else {
            return new Object();
        }
    }



}
