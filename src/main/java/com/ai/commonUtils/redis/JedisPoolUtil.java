package com.ai.commonUtils.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @Author OldFace
 * @Date 2020/6/8 17:44
 */
public class JedisPoolUtil {
    private static Logger log = Logger.getLogger(JedisPoolUtil.class);
    private static final String PROPERTIES_PATH = System.getProperty("user.dir")+
            File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"redis.properties";
    private static JedisPool jedisPool;


    static {
        if (jedisPool == null) {
            try {
                init();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化Jedis连接池
     *
     * @throws IOException
     */
    private static void init() throws IOException {
        //加载配置文件
        InputStream input = new FileInputStream(new File(PROPERTIES_PATH));
        Properties p = new Properties();
        p.load(input);
        //开始配置JedisPool
        log.info(String.format("读取redis.properties文件中的属性redis.host【%s】",p.getProperty("redis.host")));
        String host = p.getProperty("redis.host") == null ? "localhost" : p.getProperty("redis.host");
        log.info(String.format("读取redis.properties文件中的属性redis.port【%s】",p.getProperty("redis.port")));
        int port = p.getProperty("redis.port") == null ? 6379 : Integer.parseInt(p.getProperty("redis.port"));
        String auth = p.getProperty("redis.auth");
        int poolTimeOut = p.getProperty("connectionTimeOut") == null ? 2000
                : Integer.parseInt(p.getProperty("connectionTimeOut"));
        //判断使用默认的配置方式还是采用自定义配置方式,
        boolean isSetDefault = p.getProperty("defaultSetting") == null ? true
                : Boolean.parseBoolean(p.getProperty("defaultSetting"));
        if (isSetDefault) {
            jedisPool = new JedisPool(new GenericObjectPoolConfig(), host, port, poolTimeOut, auth);
        } else {
            JedisPoolConfig config = new JedisPoolConfig();
            String blockWhenExhausted = p.getProperty("redis.blockWhenExhausted");
            if (blockWhenExhausted != null) {
                config.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted));
            }
            String evictionPolicyClassName = p.getProperty("redis.evictionPolicyClassName");
            if (evictionPolicyClassName != null) {
                config.setEvictionPolicyClassName(evictionPolicyClassName);
            }
            String jmxEnabled = p.getProperty("redis.jmxEnabled");
            if (jmxEnabled != null) {
                config.setJmxEnabled(Boolean.parseBoolean(jmxEnabled));
            }
            String lifo = p.getProperty("redis.lifo");
            if (lifo != null) {
                config.setLifo(Boolean.parseBoolean(lifo));
            }
            String maxIdle = p.getProperty("redis.maxIdle");
            if (maxIdle != null) {
                config.setMaxIdle(Integer.parseInt(maxIdle));
            }
            String maxTotal = p.getProperty("redis.maxTotal");
            if (maxTotal != null) {
                config.setMaxTotal(Integer.parseInt(maxTotal));
            }
            String maxWaitMillis = p.getProperty("redis.maxWaitMillis");
            if (maxWaitMillis != null) {
                config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
            }
            String minEvictableIdleTimeMillis = p.getProperty("redis.minEvictableIdleTimeMillis");
            if (minEvictableIdleTimeMillis != null) {
                config.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
            }
            String minIdle = p.getProperty("redis.minIdle");
            if (minIdle != null) {
                config.setMinIdle(Integer.parseInt(minIdle));
            }
            String numTestsPerEvictionRun = p.getProperty("redis.numTestsPerEvictionRun");
            if (numTestsPerEvictionRun != null) {
                config.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));
            }
            String softMinEvictableIdleTimeMillis = p.getProperty("redis.softMinEvictableIdleTimeMillis");
            if (softMinEvictableIdleTimeMillis != null) {
                config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(softMinEvictableIdleTimeMillis));
            }
            String testOnBorrow = p.getProperty("redis.testOnBorrow");
            if (testOnBorrow != null) {
                config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
            }
            String testWhileIdle = p.getProperty("redis.testWhileIdle");
            if (testWhileIdle != null) {
                config.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
            }
            String timeBetweenEvictionRunsMillis = p.getProperty("redus.timeBetweenEvictionRunsMillis");
            if (timeBetweenEvictionRunsMillis != null) {
                config.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
            }
            jedisPool = new JedisPool(config, host, port, poolTimeOut, auth);
        }

    }

    public static Jedis getJedis() {
        log.info("从jedisPool中获取jedis对象");
        return jedisPool.getResource();
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
            log.info("jedis不为null，关闭jedis");
        }
    }

}
