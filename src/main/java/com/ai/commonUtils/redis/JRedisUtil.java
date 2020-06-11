package com.ai.commonUtils.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Author OldFace
 * @Date 2020/6/8 17:23.
 */
public class JRedisUtil {
    private static final Logger log = LoggerFactory.getLogger(JRedisUtil.class);

    /**
     * 检查值是否为null,如果为null返回true,不为null返回false
     * @param obj
     * @return
     */
    private static boolean isValueNull(Object... obj) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null || "".equals(obj[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置String类型的key-value
     * @param key
     * @param value
     * @return
     */
    public static int setValue(String key, String value){
            if (isValueNull(key, value)) {
                return 0;
            }
            Jedis jedis = null;
            try {
                jedis = JedisPoolUtil.getJedis();
                if (jedis.set(key, value).equalsIgnoreCase("ok")) {
                    log.info(String.format("设置Key【%s】,设置Value【%s】,执行结果为【%d】",key,value,1));
                    return 1;
                } else {
                    log.info(String.format("设置Key【%s】,设置Value【%s】,执行结果为【%d】",key,value,0));
                    return 0;
                }
            } finally {
                JedisPoolUtil.closeJedis(jedis);
            }
        }

    /**
     * 设置String类型的key-value, 设置timeout时间为秒
     * @param key
     * @param value
     * @param timeoutSeconds
     * @return
     */
    public static int setValue(String key, String value, int timeoutSeconds) {
        if (isValueNull(key, value)) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            if (jedis.setex(key, timeoutSeconds, value).equalsIgnoreCase("ok")) {
                log.info(String.format("设置Key【%s】,设置Value【%s】,设置timeout【%d】秒,执行结果为【%d】",key,value,timeoutSeconds,1));
                return 1;
            } else {
                log.info(String.format("设置Key【%s】,设置Value【%s】,设置timeout【%d】秒, 执行结果为【%d】",key,value,timeoutSeconds,0));
                return 0;
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    public static void setListValue(String key, List<String> value){
        if (!StringUtils.isEmpty(key) && null != value && value.size() != 0){
            Jedis jedis = null;
            try {
                jedis = JedisPoolUtil.getJedis();
                for (int i = 0; i < value.size(); i++) {
                    jedis.rpush(key,new String[]{(String)value.get(i)});
                }
                log.info("设置key【"+key+"】,value="+value);
            } finally {
                JedisPoolUtil.closeJedis(jedis);
            }
        }else {
            log.error("key与values不能为空");
        }
    }

    public static void setListValue(String key, List<String> value,int timeOut){
        if (!StringUtils.isEmpty(key) && null != value && value.size() != 0){
            Jedis jedis = null;
            try {
                jedis = JedisPoolUtil.getJedis();
                for (int i = 0; i < value.size(); i++) {
                    jedis.rpush(key,new String[]{(String)value.get(i)});
                }
                jedis.expire(key,timeOut);
                log.info("设置key【"+key+"】,value=【"+value+"】,timeOut【"+timeOut+"】秒");
            } finally {
                JedisPoolUtil.closeJedis(jedis);
            }
        }else {
            log.error("key与values不能为空");
        }
    }

    /**
     * 获取key对应的value
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            log.info(String.format("key【%s】对应的value【%s】",key,jedis.get(key)));
            return jedis.get(key);
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    public static List<String> getListValue(String key){
        List<String> value = new ArrayList<>();
        if (isValueNull(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            List<String> values = jedis.lrange(key,0L,-1L);
            log.info("key【"+key+"】，对应的listValue="+values);
            value = values;
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
        return value;
    }

    /**
     * 判断key是否存在redis里
     * @param key
     * @return
     */
    public static boolean isKeyExsit(String key){
        boolean isKeyExsit = false;
        if (isValueNull(key)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            if(jedis.exists(key)){
                log.info(String.format("key【%s】存在redis里",key));
                return true;
            }else {
                log.info(String.format("key【%s】不存在redis里",key));
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
        return isKeyExsit;
    }

    /**
     * 列出redis里所有的keys
     * @return
     */
    public static List<String> getKeys(String pattern){
        Jedis jedis = null;
        List<String> keyList = new ArrayList<>();
        try {
            jedis = JedisPoolUtil.getJedis();
            Set<String> keys = jedis.keys(pattern);
            for(String str : keys){
                keyList.add(str);
            }
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
        log.info("redis中所有的key为："+keyList);
        return keyList;
    }

    public static void deleteKey(String key){
        if (!isKeyExsit(key)) {
            log.info(String.format("key【%s】不存在，直接退出方法",key));
            return;
        }
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtil.getJedis();
            jedis.del(key);
            log.info(String.format("删除key【%s】",key));
        } finally {
            JedisPoolUtil.closeJedis(jedis);
        }
    }

    public static void deleteKeys(String... keys){
        Jedis jedis = null;
        jedis = JedisPoolUtil.getJedis();
        for (int i = 0; i < keys.length; i++) {
            if (!isKeyExsit(keys[i])) {
                log.info(String.format("key【%s】不存在，直接退出方法",keys[i]));
                return;
            }
            jedis.del(keys[i]);

        }
    }

    public static void main(String[] args) {
        List<String> list = Stream.of("a","b","c").collect(Collectors.toList());
        getKeys("*");
        setValue("uuuu","dddd");


    }


}
