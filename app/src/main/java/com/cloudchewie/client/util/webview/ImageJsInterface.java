package com.cloudchewie.client.util.webview;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.webkit.WebView;

import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.fragment.ImageViewFragment;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

public class ImageJsInterface {
    private Rect bounds;
    private Context context;
    private List<String> imageUrls;
    private List<UserViewInfo> mThumbViewInfoList;

    public ImageJsInterface(WebView webView, Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        bounds = NineGridUtil.getBounds(webView);
        mThumbViewInfoList = new ArrayList<>();
        for (String img : imageUrls) {
            UserViewInfo userViewInfo = new UserViewInfo(img);
            userViewInfo.setBounds(bounds);
            mThumbViewInfoList.add(userViewInfo);
        }

    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        int index = imageUrls.indexOf(img.replace("file://", ""));
        if (index == -1)
            index = 0;
        if (imageUrls.size() > 9)
            GPreviewBuilder.from((Activity) context).setUserFragment(ImageViewFragment.class).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(index).setSingleFling(true).isDisableDrag(false).setType(GPreviewBuilder.IndicatorType.Number).setFullscreen(true).start();
        else
            GPreviewBuilder.from((Activity) context).setUserFragment(ImageViewFragment.class).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(index).setSingleFling(true).isDisableDrag(false).setType(GPreviewBuilder.IndicatorType.Dot).setFullscreen(true).start();
    }
}
