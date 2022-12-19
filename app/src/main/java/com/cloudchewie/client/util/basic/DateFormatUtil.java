/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 15:00:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.basic;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatUtil {
    public static String FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String YMD_FORMAT_WITH_BAR = "yyyy-MM-dd";
    public static String YMD_FORMAT_WITH_SLASH = "yyyy/MM/dd";
    public static String YMD_FORMAT_WITH_CHARACTERS = "yyyy年MM月dd日";
    public static String MD_FORMAT_WITH_BAR = "MM-dd";
    public static String MD_FORMAT_WITH_SLASH = "MM/dd";
    public static String MD_FORMAT_WITH_CHARACTERS = "MM月dd日";
    public static String HM_FORMAT = "HH:mm";
    public static String HMS_FORMAT = "HH:mm:ss";
    public static String MS_FORMAT = "mm:ss";

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.CHINA);
    }
}
