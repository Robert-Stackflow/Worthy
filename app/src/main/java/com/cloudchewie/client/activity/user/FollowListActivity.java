/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 11:54:34
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.user;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.fragment.internal.AttractionListFragment;
import com.cloudchewie.client.fragment.internal.UserListFragment;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FollowListActivity extends BaseActivity {
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FollowListFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_follow_list);
        mBackButton = findViewById(R.id.activity_follow_list_back);
        mViewPager = findViewById(R.id.activity_follow_list_viewpager);
        mTabLayout = findViewById(R.id.activity_follow_list_tab_layout);
        initView();
        initTabLayout();
    }

    void initView() {
        mBackButton.setOnClickListener(v -> finish());
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.follow_list_tab_titles));
        mFragments.add(new UserListFragment());
        mFragments.add(new AttractionListFragment());
        mFragments.add(new UserListFragment());
        mAdapter = new FollowListFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tab.view.setTooltipText(null);
                }
            }
        }
    }

    public class FollowListFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public FollowListFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}