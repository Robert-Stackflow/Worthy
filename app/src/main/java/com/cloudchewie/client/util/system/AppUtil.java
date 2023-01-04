package com.cloudchewie.client.util.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.jetbrains.annotations.Contract;

public class AppUtil {
    @Contract("_, null -> false")
    public static boolean isAppInstalled(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty())
            return false;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static int getAppVersionCode(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty())
            return -1;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    @Contract("_, null -> null")
    public static String getAppVersionName(Context context, String packageName) {
        if (packageName == null || packageName.isEmpty())
            return null;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
