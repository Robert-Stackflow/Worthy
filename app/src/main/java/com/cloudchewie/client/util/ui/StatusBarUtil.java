/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.ui;

import static com.cloudchewie.client.util.ui.ColorUtil.isDarkColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cloudchewie.client.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

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

    public static void setStatusBarColor(@NonNull Window window, @ColorInt int statusBarColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(statusBarColor);
        setStatusBarTextColor(window, !isDarkColor(statusBarColor));
    }

    public static void setStatusBarColor(Context context, @ColorInt int color) {
        if (context instanceof Activity) {
            setStatusBarColor(((Activity) context).getWindow(), color);
        }
    }

    private static void setStatusBarTextColor(Window window, boolean isDarkMode) {
        if (isDarkMode) setDarkStatusBar(window);
        else setLightStatusBar(window);
    }

    private static void setLightStatusBar(Window window) {
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private static void setDarkStatusBar(Window window) {
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(systemUiVisibility ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void setStatusBarTextColor(Context context, boolean isDarkMode) {
        if (context instanceof Activity) {
            setStatusBarTextColor(((Activity) context).getWindow(), isDarkMode);
        }
    }

    /**
     * 返回某个Bitmap是否为深色图片
     *
     * @param bitmap 图片
     * @param left   左边界
     * @param top    上边界
     * @param right  右边界
     * @param bottom 下边界
     * @return -1表示不确定；0表示不是深色；1表示是深色
     */
    public static int isDarkBitmap(Bitmap bitmap, int left, int top, int right, int bottom) {
        final int[] isDark = {1};
        Palette.from(bitmap).setRegion(left, top, right, bottom).generate(palette -> {
            Palette.Swatch mostPopularSwatch = null;
            for (Palette.Swatch swatch : palette.getSwatches())
                if (mostPopularSwatch == null || mostPopularSwatch.getPopulation() < swatch.getPopulation())
                    mostPopularSwatch = swatch;
            if (mostPopularSwatch != null)
                isDark[0] = ColorUtils.calculateLuminance(mostPopularSwatch.getRgb()) < 0.5 ? 1 : 0;
        });
        return isDark[0];
    }

    public static boolean setStatusBarTextColor(Context context, Bitmap bitmap) {
        boolean isDark = DarkModeUtil.isDarkMode(context);
        switch (isDarkBitmap(bitmap, 0, 0, MatricsUtil.getScreenWidth(context), getStatusBarHeight(context))) {
            case 0:
                isDark = false;
                break;
            case 1:
                isDark = true;
            default:
                break;
        }
        if (isDark) setLightStatusBar(((Activity) context).getWindow());
        else setDarkStatusBar(((Activity) context).getWindow());
        return isDark;
    }

    public static boolean setStatusBarTextColor(Context context, String url) {
        final boolean[] isDark = new boolean[1];
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
        Glide.with(context).asBitmap().load(url).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if (bitmap != null) isDark[0] = setStatusBarTextColor(context, bitmap);
            }
        });
        return isDark[0];
    }

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

    public static void setStatusBarTransparent(@NonNull Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public static void setStatusBarTransparent(Context context) {
        if (context instanceof Activity) setStatusBarTransparent(((Activity) context).getWindow());
    }

    public static void setStatusBarMargin(Context context) {
        setStatusBarMargin(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), 0, getStatusBarHeight(context), 0, 0);
    }

    public static void setStatusBarMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
