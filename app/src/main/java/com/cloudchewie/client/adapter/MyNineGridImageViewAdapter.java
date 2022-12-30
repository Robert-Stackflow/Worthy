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
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.ninegrid.NineGridImageViewAdapter;

import java.util.List;

public class MyNineGridImageViewAdapter extends NineGridImageViewAdapter<UserViewInfo> {
    public MyNineGridImageViewAdapter() {
    }

    public MyNineGridImageViewAdapter(Context context, List<UserViewInfo> imageInfo) {
        super(context, imageInfo);
    }

    @Override
    protected void onDisplayImage(Context context, ImageView imageView, @NonNull UserViewInfo s) {
        Glide.with(context).load(s.getUrl()).apply(RequestOptions.placeholderOf(R.drawable.ic_state_background).error(R.drawable.ic_state_image_load_fail)).into(imageView);
    }

    @Override
    protected ImageView generateImageView(Context context) {
        return super.generateImageView(context);
    }

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<UserViewInfo> list) {
    }
}
