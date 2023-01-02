package com.cloudchewie.client.util.system;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;

public class ClipBoardUtil {
    /**
     * 复制content到剪切板中
     *
     * @param context Context对象
     * @param content 待复制内容
     */
    public static void copy(@NonNull Context context, String content) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        manager.setPrimaryClip(mClipData);
    }
}
