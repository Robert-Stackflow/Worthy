package com.cloudchewie.client.util.ui;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class MatricsUtil {
    public static int getDisplayWidth(Context context) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            int heightPixels = outMetrics.heightPixels;
            return outMetrics.widthPixels;
        }
        return 0;
    }

    public static int getDisplayHeight(Context context) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            int heightPixels = outMetrics.heightPixels;
            return outMetrics.widthPixels;
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            int heightPixels = outMetrics.heightPixels;
            return outMetrics.widthPixels;
        }
        return 0;
    }

    public static int getScreenHeight(Context context) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            int heightPixels = outMetrics.heightPixels;
            return outMetrics.widthPixels;
        }
        return 0;
    }
}
