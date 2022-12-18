/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.fragment.PostsFragment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.NoScrollViewPager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AttractionDetailActivity extends BaseActivity {
    Attraction attraction;
    AttractionDetailFragmentAdapter adapter;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        Intent intent = this.getIntent();
        attraction = (Attraction) intent.getSerializableExtra("attraction");
        findViewById(R.id.attraction_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.attraction_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        ((TextView) findViewById(R.id.attraction_detail_name)).setText(attraction.getName());
        ((TextView) findViewById(R.id.attraction_detail_location)).setText(attraction.getLocation());
        ((TextView) findViewById(R.id.attraction_detail_describe)).setText(attraction.getDescribe());
        tabLayout = findViewById(R.id.attraction_detail_content_tab_layout);
        viewPager = findViewById(R.id.attraction_detail_content_viewpager);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("帖子");
        titles.add("文章");
        titles.add("相册");
        fragments.add(new PostsFragment());
        fragments.add(new PostsFragment());
        BaseFragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "相册");
        fragment.setArguments(bundle);
        fragments.add(fragment);
        adapter = new AttractionDetailFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class AttractionDetailFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList;
        private final List<String> titleList;

        public AttractionDetailFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
            super(fragmentManager);
            fragmentList = fragments;
            titleList = titles;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}