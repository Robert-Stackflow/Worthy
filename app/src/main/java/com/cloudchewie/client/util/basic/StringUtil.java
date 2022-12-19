/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.basic;

import androidx.annotation.NonNull;

public class StringUtil {
    @NonNull
    public static String handleLineBreaks(@NonNull String string) {
        return string.replace("\\n", "\n");
    }
}
