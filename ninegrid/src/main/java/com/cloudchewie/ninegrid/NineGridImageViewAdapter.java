package com.cloudchewie.ninegrid;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

public abstract class NineGridImageViewAdapter<T> {
    protected Context context;
    protected int radius = 10;
    private List<T> imageInfo;

    public NineGridImageViewAdapter(Context context, List<T> imageInfo) {
        this.context = context;
        this.imageInfo = imageInfo;
    }

    public NineGridImageViewAdapter() {

    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    protected abstract void onDisplayImage(Context context, ImageView imageView, int count, int index, T t);

    protected void onItemImageClick(Context context, ImageView imageView, int index, List<T> list) {
    }

    protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list) {
        return false;
    }

    protected ImageView generateImageView(int count, int index, Context context) {
        GridImageView imageView = null;
//        switch (count) {
//            case 1:
//                imageView = loadWithOneChildren(context, imageView, index);
//                break;
//            case 2:
//                imageView = loadWithTwoChildren(context, imageView, index);
//                break;
//            case 3:
//                imageView = loadWithThreeChildren(context, imageView, index);
//                break;
//            case 4:
//                imageView = loadWithFourChildren(context, imageView, index);
//                break;
//            case 5:
//                imageView = loadWithFiveChildren(context, imageView, index);
//                break;
//            case 6:
//                imageView = loadWithSixChildren(context, imageView, index);
//                break;
//            case 7:
//                imageView = loadWithSevenChildren(context, imageView, index);
//                break;
//            case 8:
//                imageView = loadWithEightChildren(context, imageView, index);
//                break;
//            case 9:
//            default:
//                imageView = loadWithNineChildren(context, imageView, index);
//                break;
//        }
        if (imageView == null)
            imageView = new GridImageView(context, radius);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    GridImageView loadWithOneChildren(Context context, ImageView imageView, int index) {
        return new GridImageView(context, radius);
    }

    GridImageView loadWithTwoChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, true, true, false, false);
            case 1:
                return new GridImageView(context, radius, false, false, true, true);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithThreeChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, true, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, true, true);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithFourChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 1:
                return new GridImageView(context, radius, false, false, false, true);
            case 2:
                return new GridImageView(context, radius, true, false, false, false);
            case 3:
                return new GridImageView(context, radius, false, false, true, false);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithFiveChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, false, true);
            case 3:
                return new GridImageView(context, radius, true, false, false, false);
            case 4:
                return new GridImageView(context, radius, false, false, true, false);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithSixChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, false, true);
            case 3:
                return new GridImageView(context, radius, true, false, false, false);
            case 5:
                return new GridImageView(context, radius, false, false, true, false);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithSevenChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, false, true);
            case 6:
                return new GridImageView(context, radius, true, false, false, false);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithEightChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, false, true);
            case 6:
                return new GridImageView(context, radius, true, false, false, false);
            default:
                return new GridImageView(context);
        }
    }

    GridImageView loadWithNineChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                return new GridImageView(context, radius, false, true, false, false);
            case 2:
                return new GridImageView(context, radius, false, false, false, true);
            case 6:
                return new GridImageView(context, radius, true, false, false, false);
            case 8:
                return new GridImageView(context, radius, false, false, true, false);
            default:
                return new GridImageView(context);
        }
    }
}