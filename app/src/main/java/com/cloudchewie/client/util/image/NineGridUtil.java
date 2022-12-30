/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/20 10:47:34
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.image;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.fragment.ImageViewFragment;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.view.BasePhotoFragment;

import java.util.List;

public class NineGridUtil {
    public static Rect getBounds(ImageView imageView) {
        Rect bounds = new Rect();
        imageView.getGlobalVisibleRect(bounds);
        return bounds;
    }

    public static Rect getBounds(WebView imageView) {
        Rect bounds = new Rect();
        imageView.getGlobalVisibleRect(bounds);
        return bounds;
    }

    public static void computeBoundsBackward(NineGridImageView<UserViewInfo> nineGridImageViewer, List<UserViewInfo> list) {
        for (int i = 0; i < nineGridImageViewer.getChildCount(); i++) {
            View itemView = nineGridImageViewer.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView;
                thumbView.getGlobalVisibleRect(bounds);
            }
            list.get(i).setBounds(bounds);
            list.get(i).setUrl(list.get(i).getUrl());
        }
        for (int i = nineGridImageViewer.getChildCount(); i < list.size(); i++) {
            View itemView = nineGridImageViewer.getChildAt(nineGridImageViewer.getChildCount() - 1);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView;
                thumbView.getGlobalVisibleRect(bounds);
            }
            list.get(i).setBounds(bounds);
            list.get(i).setUrl(list.get(i).getUrl());
        }
    }

    public static void setDataSource(NineGridImageView<UserViewInfo> nineGridImageViewer, List<UserViewInfo> userViewInfos) {
        setDataSource(nineGridImageViewer, userViewInfos, true, ImageViewFragment.class);
    }

    public static void setDataSourceWithoutUserFragment(NineGridImageView<UserViewInfo> nineGridImageViewer, List<UserViewInfo> userViewInfos) {
        setDataSource(nineGridImageViewer, userViewInfos, false, ImageViewFragment.class);
    }

    public static void setDataSource(NineGridImageView<UserViewInfo> nineGridImageViewer, List<UserViewInfo> userViewInfos, boolean isUserFragment, @NonNull Class<? extends BasePhotoFragment> className) {
        GPreviewBuilder.IndicatorType mIndicatorType;
        nineGridImageViewer.setImagesData(userViewInfos);
        if (userViewInfos.size() > 9) mIndicatorType = GPreviewBuilder.IndicatorType.Number;
        else mIndicatorType = GPreviewBuilder.IndicatorType.Dot;
        nineGridImageViewer.setItemImageClickListener((context, imageView, index, list) -> {
            computeBoundsBackward(nineGridImageViewer, list);
            if (isUserFragment)
                GPreviewBuilder.from((Activity) context).setData(list).setUserFragment(className).setIsScale(true).setCurrentIndex(index).setFullscreen(true).setSingleFling(true).isDisableDrag(false).setType(mIndicatorType).start();
            else
                GPreviewBuilder.from((Activity) context).setData(list).setIsScale(true).setCurrentIndex(index).setFullscreen(true).setSingleFling(true).isDisableDrag(false).setType(mIndicatorType).start();
        });
    }
}
