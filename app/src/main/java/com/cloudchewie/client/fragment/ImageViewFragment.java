/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/20 14:18:09
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.ui.BottomSheet;
import com.previewlibrary.view.BasePhotoFragment;

public class ImageViewFragment extends BasePhotoFragment {
    private UserViewInfo bean;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bean = (UserViewInfo) getBeanViewInfo();
        imageView.setOnLongClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(getActivity());
            bottomSheet.setMainLayout(R.layout.layout_image_operation);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.image_operation_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
            return false;
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.previewlibrary.R.layout.fragment_image_photo_layout, container, false);
    }
}

