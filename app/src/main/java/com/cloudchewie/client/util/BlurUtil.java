package com.cloudchewie.client.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class BlurUtil {
    public static void setBlurBackground(Context context, View view, Bitmap bmp) {
        final Bitmap blurBmp = fastblur(context, bmp, 10);
        final Drawable drawable = getDrawable(context, blurBmp);
        view.setBackgroundDrawable(drawable);
    }

    public static Drawable getDrawable(Context context, Bitmap bm) {
        return new BitmapDrawable(context.getResources(), bm);
    }

    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);/* e.g. 3.f */
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;

    }
}
