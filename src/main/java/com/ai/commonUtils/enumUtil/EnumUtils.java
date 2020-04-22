package com.ai.commonUtils.enumUtil;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/18 下午12:42
 * @description:
 */
public enum EnumUtils {
    RESULT_CODE_SUCCSS("1","000000");

    EnumUtils(String value, String desc) {
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

        public static String getDesc(String value) {
            EnumUtils[] values = EnumUtils.values();
            for (EnumUtils e : values) {
                if (e.getValue().equals(value)) {
                    return e.getDesc();
                }
            }
            return null;
        }



}
