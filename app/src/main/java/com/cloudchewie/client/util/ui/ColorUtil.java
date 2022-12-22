package com.cloudchewie.client.util.ui;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

public class ColorUtil {
    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
