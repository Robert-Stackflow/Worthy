package com.cloudchewie.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.cloudchewie.client.util.StatusBarUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AttractionDetailActivity extends BaseActivity {
    Attraction attraction;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
    AttractionDetailFragmentAdapter adapter;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        attraction = (Attraction) intent.getSerializableExtra("attraction");
        setContentView(R.layout.activity_attraction_detail);
        findViewById(R.id.attraction_detail_back).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.attraction_detail_name)).setText(attraction.getName());
        ((TextView) findViewById(R.id.attraction_detail_location)).setText(attraction.getLocation());
        ((TextView) findViewById(R.id.attraction_detail_describe)).setText(attraction.getDescribe());
        tabLayout = findViewById(R.id.attraction_detail_content_tab_layout);
        viewPager = findViewById(R.id.attraction_detail_content_viewpager);
        fragments = new ArrayList<>(3);
        titles = new ArrayList<>(3);
        titles.add("帖子");
        titles.add("文章");
        titles.add("相册");
        for (String str : titles) {
            BaseFragment fragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
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