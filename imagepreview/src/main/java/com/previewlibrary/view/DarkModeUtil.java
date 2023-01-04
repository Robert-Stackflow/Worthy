package com.previewlibrary.view;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkModeUtil {
    public static boolean isDarkMode(Context context) {
        return isAlwaysDarkMode() || (context.getResources().getConfiguration().uiMode == 0x21 && (isAlwaysSystemMode() || isAlwaysUnspecifiedMode()));
    }

    public static boolean isLightMode(Context context) {
        return isAlwaysLightMode() || (context.getResources().getConfiguration().uiMode == 0x11 && (isAlwaysSystemMode() || isAlwaysUnspecifiedMode()));
    }

    public static boolean isAlwaysDarkMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }


    public static boolean isAlwaysLightMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO;
    }

    public static boolean isAlwaysUnspecifiedMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
    }


    public static boolean isAlwaysSystemMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }

    public static void switchToAlwaysDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static void switchToAlwaysLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void switchToAlwaysSystemMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void switchToAlwaysUnspecifiedMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
    }

    public static void toggle(Context context) {
        if (isDarkMode(context))
            switchToAlwaysLightMode();
        else
            switchToAlwaysDarkMode();
    }
}
