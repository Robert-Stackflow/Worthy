package com.cloudchewie.client.util.system;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtil {
    public static void copy(Context context, String content) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        manager.setPrimaryClip(mClipData);
    }
}
