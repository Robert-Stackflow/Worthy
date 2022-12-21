/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.settings.SettingsActivity;
import com.cloudchewie.client.activity.user.FavoritesListActivity;
import com.cloudchewie.client.activity.user.FollowListActivity;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class UserFragment extends Fragment implements View.OnClickListener {
    View mainView;
    RefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_user, null);
        StatusBarUtil.setMargin(mainView.findViewById(R.id.user_titlebar), 0, StatusBarUtil.getHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.user_settings).setOnClickListener(this);
        mainView.findViewById(R.id.switch_daynight).setOnClickListener(this);
        mainView.findViewById(R.id.entry_home_page).setOnClickListener(this);
        mainView.findViewById(R.id.user_entry_collection).setOnClickListener(this);
        mainView.findViewById(R.id.user_entry_following).setOnClickListener(this);
        mainView.findViewById(R.id.user_entry_creation).setOnClickListener(this);
        mainView.findViewById(R.id.user_entry_footprint).setOnClickListener(this);
        initSwipeRefresh();
        return mainView;
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_user_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.user_settings)) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.switch_daynight)) {
//            ImageButton switchDaynight = (ImageButton) view;
//            if (switchDaynight != null) switchDaynight.setOnClickListener(v -> {
//                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED && (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES) == 0) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                } else {
//                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//            });
        } else if (view == mainView.findViewById(R.id.user_entry_collection)) {
            Intent intent = new Intent(getActivity(), FavoritesListActivity.class);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.user_entry_creation)) {

        } else if (view == mainView.findViewById(R.id.user_entry_following)) {
            Intent intent = new Intent(getActivity(), FollowListActivity.class);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.user_entry_footprint)) {

        } else if (view == mainView.findViewById(R.id.entry_home_page)) {
            Intent intent = new Intent(getActivity(), HomePageActivity.class);
            startActivity(intent);
        }
    }
}