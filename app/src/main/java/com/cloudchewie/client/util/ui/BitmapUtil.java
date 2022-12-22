package com.cloudchewie.client.util.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class BitmapUtil {

    public static Bitmap getBitmap(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }
}
