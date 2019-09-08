package com.je.portal.Enum;

/**
 * 首页日志枚举类
 */
public enum PortalLogEnum {
    RZ("RZ", "日志"),
    ZB("ZB", "周报"),
    YB("YB", "月报"),
    ;
    private String key;
    private String value;

    PortalLogEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


}
