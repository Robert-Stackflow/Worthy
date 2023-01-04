package com.cloudchewie.ninegrid;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

public abstract class NineGridImageViewAdapter<T> {
    protected Context context;
    private List<T> imageInfo;

    public NineGridImageViewAdapter(Context context, List<T> imageInfo) {
        this.context = context;
        this.imageInfo = imageInfo;
    }

    public NineGridImageViewAdapter() {

    }

    protected abstract void onDisplayImage(Context context, ImageView imageView, int count, int index, T t);

    protected void onItemImageClick(Context context, ImageView imageView, int index, List<T> list) {
    }

    protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list) {
        return false;
    }

    protected ImageView generateImageView(Context context) {
        GridImageView imageView = new GridImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}