package com.cloudchewie.client.util.enumeration;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    CITY_HISTORY("city_history", "搜索城市列表");
    private final String key;
    private final String describe;

    SharedPreferenceCode(String key, String describe) {
        this.key = key;
        this.describe = describe;
    }

    @Contract(pure = true)
    public String getKey() {
        return key;
    }

    @Contract(pure = true)
    public String getDescribe() {
        return describe;
    }
}
