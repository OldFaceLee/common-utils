/*
package com.ai.commonUtils.redis;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import redis.clients.jedis.Jedis;

*/
/**
 * @author: oldFace
 * @date: create in 2020/6/4
 * @description:
 *//*

public class JRedisUtil {
        private static final Logger log = LoggerFactory.getLogger(JRedisUtil.class);
        private static Map<String, JRedisUtil> redisUtilMap = new HashMap();
        public static final String GLOBAL = "GLOBAL";
        public static final String CLASS = "CLASS";
        private static final String GROUP = "GROUP";
        public static final String NONE = "NONE";
        private final String HOST = "192.168.183.125";
        private final int PORT = 22002;
        private final String PASSWORD = "mypassword";
        private final Integer expireSeconds = 86400000;
        private String RD = "";
        private Jedis redis;
        private static final Semaphore semaphore = new Semaphore(1);

        public JRedisUtil() {
        }

        private String getClassName() {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            if (stack != null && stack.length > 1 && stack[1] != null) {
                StackTraceElement[] var2 = stack;
                int var3 = stack.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    StackTraceElement st = var2[var4];
                    if (st.getClassName().startsWith("com.jd.qa.test")) {
                        return st.getClassName();
                    }
                }
            }

            return "NON_STACK";
        }

        private void getRD(String prefix) {
            if (prefix.equals("GLOBAL")) {
                this.RD = "RD:" + TaskSession.batchNumber + ":" + "GLOBAL" + ":";
            } else if (prefix.equals("CLASS")) {
                this.RD = "RD:" + TaskSession.batchNumber + ":" + "CLASS" + ":" + this.getClassName() + ":";
            } else {
                this.RD = "RD:" + TaskSession.batchNumber + ":" + "GROUP" + ":" + prefix + ":";
            }

        }

        private void syncInit() {
            if (this.redis == null || !this.redis.isConnected()) {
                try {
                    this.redis = new Jedis("192.168.183.125", 22002);
                    this.redis.auth("mypassword");
                    log.info("Connect Redis Server Successful!");
                } catch (Exception var2) {
                    log.error("Failed to Connect Redis Server!");
                    Assert.fail("Failed to Connect Redis Server!");
                }
            }

        }

        public static JRedisUtil getInstance(String prefix) {
            JRedisUtil var2;
            try {
                semaphore.acquire();
                JRedisUtil JRedisUtil = new JRedisUtil();
                JRedisUtil.getRD(prefix);
                if (redisUtilMap == null || redisUtilMap.size() < 1 || redisUtilMap.get(JRedisUtil.RD) == null) {
                    JRedisUtil.syncInit();
                    redisUtilMap.put(JRedisUtil.RD, JRedisUtil);
                }

                var2 = (JRedisUtil)redisUtilMap.get(JRedisUtil.RD);
                return var2;
            } catch (InterruptedException var6) {
                Assert.fail("getInstance failed!");
                var2 = null;
            } finally {
                semaphore.release();
            }

            return var2;
        }

        public static JRedisUtil getInstance() {
            return getInstance("CLASS");
        }

        public void disconnect() {
            synchronized(this.redis) {
                if (this.redis != null) {
                    try {
                        this.redis.disconnect();
                        log.info("disconnect Redis Server!");
                    } catch (Exception var4) {
                        log.error("Failed to disconnect Redis Server!");
                    }
                }

            }
        }

        public void setValue(String key, String value) {
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                synchronized(this.redis) {
                    try {
                        if (!key.contains(this.RD)) {
                            key = this.RD + key;
                        }

                        this.redis.set(key, value);
                        this.redis.expire(key, this.expireSeconds);
                        if (StringUtils.isEmpty(this.redis.get(key))) {
                            log.error("Set value failed!");
                        }
                    } catch (Exception var6) {
                        log.error("Failed Set key!");
                    }
                }
            } else {
                log.error("写入key或value不能为空");
            }

        }

        public void setListValue(String key, List<String> value) {
            if (!StringUtils.isEmpty(key) && null != value && value.size() != 0) {
                synchronized(this.redis) {
                    try {
                        if (!key.contains(this.RD)) {
                            key = this.RD + key;
                        }

                        this.redis.del(key);

                        for(int i = 0; i < value.size(); ++i) {
                            this.redis.rpush(key, new String[]{(String)value.get(i)});
                        }

                        this.redis.expire(key, this.expireSeconds);
                    } catch (Exception var6) {
                        log.error("Failed Set ListValue!");
                    }
                }
            } else {
                log.error("写入key或value不能为空");
            }

        }

        public void addListValue(String key, String value) {
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                synchronized(this.redis) {
                    try {
                        if (!key.contains(this.RD)) {
                            key = this.RD + key;
                        }

                        this.redis.lpush(key, new String[]{value});
                        this.redis.expire(key, this.expireSeconds);
                    } catch (Exception var6) {
                        log.error("Failed Set ListValue!");
                        var6.printStackTrace();
                    }
                }
            } else {
                log.error("写入key或value不能为空");
            }

        }

        private boolean existsKey(String key) {
            synchronized(this.redis) {
                boolean var10000;
                try {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    var10000 = this.redis.exists(key);
                } catch (Exception var5) {
                    log.error("Not exits key!");
                    return false;
                }

                return var10000;
            }
        }

        public void delKey(String key) {
            synchronized(this.redis) {
                try {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    if (!this.existsKey(key)) {
                        return;
                    }

                    this.redis.del(key);
                } catch (Exception var5) {
                    log.error("Failed Del key!");
                }

            }
        }

        public String getValue(String key) {
            synchronized(this.redis) {
                String var10000;
                try {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    if (!this.existsKey(key)) {
                        var10000 = null;
                        return var10000;
                    }

                    var10000 = this.redis.get(key);
                } catch (Exception var5) {
                    return null;
                }

                return var10000;
            }
        }

        public List<String> getListValues(String key) {
            synchronized(this.redis) {
                List var10000;
                try {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    if (!this.existsKey(key)) {
                        var10000 = Collections.emptyList();
                        return var10000;
                    }

                    List<String> values = this.redis.lrange(key, 0L, -1L);
                    var10000 = values;
                } catch (Exception var5) {
                    log.error("Failed Get ListValue!");
                    return Collections.emptyList();
                }

                return var10000;
            }
        }

        public List<String> getListValues(String key, Integer sum) {
            synchronized(this.redis) {
                List var10000;
                try {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    if (!this.existsKey(key)) {
                        var10000 = Collections.emptyList();
                        return var10000;
                    }

                    List<String> values = this.redis.lrange(key, 0L, (long)sum);
                    var10000 = values;
                } catch (Exception var6) {
                    log.error("Failed Get ListValue!");
                    return Collections.emptyList();
                }

                return var10000;
            }
        }

        public <T> T getBean(String key, Class<T> typeOfClass) {
            synchronized(this.redis) {
                if (!key.contains(this.RD)) {
                    key = this.RD + key;
                }

                return GsonUtil.Instance().fromJson(this.getValue(key), typeOfClass);
            }
        }

        public void setBean(String key, Object bean) {
            if (!StringUtils.isEmpty(key) && null != bean) {
                synchronized(this.redis) {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    this.setValue(key, GsonUtil.Instance().toJson(bean));
                }
            } else {
                log.error("写入key或value不能为空");
            }

        }

        public Map<String, String> hgetAll(String key) {
            synchronized(this.redis) {
                if (!key.contains(this.RD)) {
                    key = this.RD + key;
                }

                return this.redis.hgetAll(key);
            }
        }

        public long hset(String key, String field, String value) {
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {
                synchronized(this.redis) {
                    if (!key.contains(this.RD)) {
                        key = this.RD + key;
                    }

                    long status = this.redis.hset(key, field, value);
                    this.redis.expire(key, this.expireSeconds);
                    return status;
                }
            } else {
                log.error("写入key或value不能为空");
                return -1L;
            }
        }

}
*/
