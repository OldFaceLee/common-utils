package com.ai.commonUtils.entityUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: lixuejun
 * @date: Create in 2020/4/7 下午4:09
 * @description:
 */
public class EntityUtil {

    private EntityUtil(){}

    private static class Single{
        private static EntityUtil entityUtil = new EntityUtil();
    }
    public static EntityUtil getInstance(){
        return Single.entityUtil;
    }

    public HashMap entityToHashMap(Object obj){
        HashMap hashMap = new HashMap();
        for(Field field : obj.getClass().getDeclaredFields()){
            Object o = null;
            try {
                o = field.get(obj);
                hashMap.put(field.getName(), o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    public HashMap<String, Object> jsonToMap(String jsonStr){
        HashMap<String, Object> map = new HashMap<>();
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                Iterator it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    Object json2 = it.next();
                    list.add(jsonToMap(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        AtomicLong l = new AtomicLong(1);
        Long ll = new Long(1);
        System.out.println(l);
        System.out.println(ll);
        System.out.println(l.equals(ll));
    }



}
