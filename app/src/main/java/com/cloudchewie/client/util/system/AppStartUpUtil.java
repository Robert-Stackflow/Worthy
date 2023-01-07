package com.cloudchewie.client.util.system;

import static com.cloudchewie.client.util.basic.DateFormatUtil.MD_FORMAT_WITH_BAR;

import android.content.Context;

import com.cloudchewie.client.util.basic.Constant;
import com.cloudchewie.client.util.basic.DateFormatUtil;

import java.util.Date;

/**
 * APP启动判断工具类
 */
public class AppStartUpUtil {
    private static String DEFAULT_STARTUP_DAY = "2023-01-01";

    /**
     * 判断是否是首次启动
     *
     * @param context
     * @return
     */
    public static boolean isFirstStartApp(Context context) {
        boolean isFirst = SharedPreferenceUtil.getBoolean(Constant.APP_FIRST_START, true, context);
        if (isFirst) {
            SharedPreferenceUtil.putBoolean(Constant.APP_FIRST_START, false, context);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是今日首次启动APP
     *
     * @param context
     * @return
     */
    public static boolean isTodayFirstStartApp(Context context) {
        String defaultDay = SharedPreferenceUtil.getString(Constant.START_UP_APP_TIME, DEFAULT_STARTUP_DAY, context);
        String today = DateFormatUtil.getSimpleDateFormat(MD_FORMAT_WITH_BAR).format(new Date());
        if (!defaultDay.equals(today)) {
            SharedPreferenceUtil.putString(Constant.START_UP_APP_TIME, today, context);
            return true;
        } else {
            return false;
        }
    }
}
