/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:16:16
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.fragment.StateFragment;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttractionDetailActivity extends BaseActivity {
    private List<String> mTitles;
    private Attraction mAttraction;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mNameView;
    private TextView mLocationView;
    private TextView mDescribeView;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private AttractionDetailFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        mBackButton = findViewById(R.id.activity_attraction_detail_back);
        mMoreButton = findViewById(R.id.activity_attraction_detail_more);
        mNameView = findViewById(R.id.activity_attraction_detail_name);
        mLocationView = findViewById(R.id.activity_attraction_detail_location);
        mDescribeView = findViewById(R.id.activity_attraction_detail_describe);
        mTabLayout = findViewById(R.id.activity_attraction_detail_content_tab_layout);
        mViewPager = findViewById(R.id.activity_attraction_detail_content_viewpager);
        initView();
        initViewPager();
    }

    void initView() {
        mBackButton.setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        Intent intent = this.getIntent();
        mAttraction = (Attraction) intent.getSerializableExtra("attraction");
        mNameView.setText(mAttraction.getName());
        mLocationView.setText(mAttraction.getLocation());
        mDescribeView.setText(mAttraction.getDescribe());
    }

    void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.attraction_detail_tab_titles));
        mFragments.add(new PostListFragment());
        mFragments.add(new PostListFragment());
        mFragments.add(new StateFragment());
        mAdapter = new AttractionDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public class AttractionDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public AttractionDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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