package com.previewlibrary.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

    /**
     * 获取状态栏高度
     *
     * @param context context对象
     * @return 返回状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            @SuppressLint("InternalInsetResource") int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏颜色
     *
     * @param window         window对象
     * @param statusBarColor 状态栏颜色
     */
    public static void setStatusBarColor(@NonNull Window window, @ColorInt int statusBarColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(statusBarColor);
        setStatusBarTextColor(window, !isDarkColor(statusBarColor));
    }

    /**
     * 设置状态栏颜色
     *
     * @param context        context对象
     * @param statusBarColor 状态栏颜色
     */
    public static void setStatusBarColor(Context context, @ColorInt int statusBarColor) {
        if (context instanceof Activity) {
            setStatusBarColor(((Activity) context).getWindow(), statusBarColor);
        }
    }

    /**
     * 根据深色模式，设置状态栏字体图标颜色
     *
     * @param window     window对象
     * @param isDarkMode 是否为深色模式
     */
    private static void setStatusBarTextColor(Window window, boolean isDarkMode) {
        if (isDarkMode) setDarkStatusBar(window);
        else setLightStatusBar(window);
    }

    /**
     * 根据深色模式，设置状态栏字体图标颜色
     *
     * @param context    context对象
     * @param isDarkMode 是否为深色模式
     */
    public static void setStatusBarTextColor(Context context, boolean isDarkMode) {
        if (context instanceof Activity) {
            setStatusBarTextColor(((Activity) context).getWindow(), isDarkMode);
        }
    }

    /**
     * 设置状态栏字体图标为浅色
     *
     * @param window window对象
     */
    private static void setLightStatusBar(@NonNull Window window) {
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 设置状态栏字体图标为深色
     *
     * @param window window对象
     */
    private static void setDarkStatusBar(@NonNull Window window) {
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(systemUiVisibility ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 设置MIUI系统状态栏字体图标为深色
     *
     * @param window window对象
     * @param isDark 是否设置为深色
     */
    private static void setMIUIDark(Window window, boolean isDark) {
        try {
            Class<? extends Window> clazz = window.getClass();
            int darkModeFlag;
            @SuppressLint("PrivateApi") Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDark ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Flyme系统状态栏字体图标为深色
     *
     * @param window window对象
     * @param isDark 是否设置为深色
     */
    private static void setFlymeDark(Window window, boolean isDark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置状态栏透明
     *
     * @param window window对象
     */
    public static void setStatusBarTransparent(@NonNull Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏透明
     *
     * @param context context对象
     */
    public static void setStatusBarTransparent(Context context) {
        if (context instanceof Activity) setStatusBarTransparent(((Activity) context).getWindow());
    }

    /**
     * 设置状态栏的marginTop，防止遮挡布局
     *
     * @param context context对象
     */
    public static void setStatusBarMarginTop(Context context) {
        setStatusBarMarginTop(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), 0, getStatusBarHeight(context), 0, 0);
    }

    /**
     * 设置状态栏的margin
     *
     * @param view   内容根布局
     * @param left   左margin
     * @param top    上margin
     * @param right  右margin
     * @param bottom 下margin
     */
    public static void setStatusBarMarginTop(@NonNull View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
