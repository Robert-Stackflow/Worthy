/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/20 14:18:09
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.global;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.ShareItemAdapter;
import com.cloudchewie.client.domin.ShareItem;
import com.cloudchewie.client.util.enumeration.Direction;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.mess.LinearItemDecoration;
import com.cloudchewie.client.util.system.ShareUtil;
import com.cloudchewie.client.util.ui.SizeUtil;
import com.cloudchewie.ui.BottomSheet;
import com.previewlibrary.view.BasePhotoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageViewFragment extends BasePhotoFragment {
    private ImageViewInfo bean;
    private Map<String, String> appPackageNameMap = new HashMap<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bean = (ImageViewInfo) getBeanViewInfo();
        imageView.setOnLongClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(getActivity());
            bottomSheet.setMainLayout(R.layout.layout_image_operation);
            bottomSheet.show();
            RecyclerView recyclerView = bottomSheet.findViewById(R.id.image_operation_share_recyclerview);
            recyclerView.setAdapter(new ShareItemAdapter(getShareItem(), getContext()));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new LinearItemDecoration(getContext(), SizeUtil.dp2px(getContext(), 20), Direction.RIGHT));
            bottomSheet.findViewById(R.id.image_operation_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
            bottomSheet.findViewById(R.id.image_operation_more).setOnClickListener(v2 -> shareSingleImage());
            return false;
        });
    }

    public List<ShareItem> getShareItem() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        List<ResolveInfo> resolveInfos = new ArrayList<>();
        ArrayList<ShareItem> shareItems = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(intent, 0))
            if (ShareUtil.getSupportedPackages().containsValue(resolveInfo.activityInfo.packageName))
                resolveInfos.add(resolveInfo);
        for (ResolveInfo resolveInfo : resolveInfos)
            shareItems.add(new ShareItem(resolveInfo.loadLabel(packageManager), resolveInfo.activityInfo.packageName, resolveInfo.loadIcon(packageManager)));
        return shareItems;
    }

    public void shareSingleImage() {
//        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), Glide.with(getContext()).load(bean.getUrl()).into(), null, null));
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        intent.setType("image/*");
//        startActivity(Intent.createChooser(intent, "分享到"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.previewlibrary.R.layout.fragment_image_photo_layout, container, false);
    }
}

