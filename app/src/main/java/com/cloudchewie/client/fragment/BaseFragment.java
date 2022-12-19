/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:51:03
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.client.R;

public class BaseFragment extends Fragment {
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) title = getArguments().getString("title", "base");
        else title = "base";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_base, null);
        if (view.findViewById(R.id.tv_title) != null)
            ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        return view;
    }

    public void performRefresh() {
    }
}
