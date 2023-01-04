/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/20 09:34:28
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.ninegrid.NiceImageView;
import com.cloudchewie.ninegrid.NineGridImageViewAdapter;

import java.util.List;

public class MyNineGridImageViewAdapter extends NineGridImageViewAdapter<ImageViewInfo> {
    int radius = 5;

    public MyNineGridImageViewAdapter() {
    }

    public MyNineGridImageViewAdapter(Context context, List<ImageViewInfo> imageInfo) {
        super(context, imageInfo);
    }

    @Override
    protected void onDisplayImage(Context context, ImageView imageView, int count, int index, @NonNull ImageViewInfo s) {
        Glide.with(context).load(s.getUrl()).apply(RequestOptions.placeholderOf(R.drawable.ic_state_background).error(R.drawable.ic_state_image_load_fail)).into(imageView);
//        switch (count) {
//            case 1:
//                loadWithOneChildren(context, imageView, index);
//                break;
//            case 2:
//                loadWithTwoChildren(context, imageView, index);
//                break;
//            case 3:
//                loadWithThreeChildren(context, imageView, index);
//                break;
//            case 4:
//                loadWithFourChildren(context, imageView, index);
//                break;
//            case 5:
//                loadWithFiveChildren(context, imageView, index);
//                break;
//            case 6:
//                loadWithSixChildren(context, imageView, index);
//                break;
//            case 7:
//                loadWithSevenChildren(context, imageView, index);
//                break;
//            case 8:
//                loadWithEightChildren(context, imageView, index);
//                break;
//            case 9:
//            default:
//                loadWithNineChildren(context, imageView, index);
//                break;
//        }
    }

    @Override
    protected ImageView generateImageView(Context context) {
        return super.generateImageView(context);
    }

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<ImageViewInfo> list) {
    }

    void loadWithOneChildren(Context context, ImageView imageView, int index) {
        ((NiceImageView) imageView).setCornerRadius(radius);
    }

    void loadWithTwoChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 1:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithThreeChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithFourChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 1:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 3:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithFiveChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
            case 3:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 4:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithSixChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                break;
            case 3:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 5:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithSevenChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                break;
            case 5:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
            case 6:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithEightChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                break;
            case 5:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
            case 6:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 7:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }

    void loadWithNineChildren(Context context, ImageView imageView, int index) {
        switch (index) {
            case 0:
                ((NiceImageView) imageView).setCornerTopLeftRadius(radius);
                break;
            case 2:
                ((NiceImageView) imageView).setCornerTopRightRadius(radius);
                break;
            case 6:
                ((NiceImageView) imageView).setCornerBottomLeftRadius(radius);
                break;
            case 8:
                ((NiceImageView) imageView).setCornerBottomRightRadius(radius);
                break;
        }
    }
}
