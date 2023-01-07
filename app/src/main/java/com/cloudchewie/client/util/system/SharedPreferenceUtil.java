package com.cloudchewie.client.util.system;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * SharedPreferences工具类
 */
public class SharedPreferenceUtil {
    private static final String NAME = "config";

    public static void putBoolean(String key, boolean value, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String key, String value, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    @Contract("_, _, null -> !null")
    public static String getString(String key, String defValue, Context context) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            return sp.getString(key, defValue);
        }
        return "";
    }

    public static void putInt(String key, int value, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String key, @NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static void putObject(String key, Object object, @NonNull Context context) {
        putString(key, JSON.toJSONString(object), context);
    }

    public static Object getObject(String key, Class clazz, @NonNull Context context) {
        return JSON.parseObject(getString(key, "", context), clazz);
    }

    public static void putArray(String key, List<Object> objects, @NonNull Context context) {
        putString(key, JSON.toJSONString(objects), context);
    }

    public static List<Object> getArray(String key, Class clazz, @NonNull Context context) {
        return JSON.parseArray(getString(key, "", context), clazz);
    }

    public static void putStringArray(String key, List<String> strings, @NonNull Context context) {
        putString(key, JSON.toJSONString(strings), context);
    }

    public static List<String> getStringArray(String key, @NonNull Context context) {
        return JSON.parseArray(getString(key, "", context), String.class);
    }
}
