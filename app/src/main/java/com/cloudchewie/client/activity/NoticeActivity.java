/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 21:50:45
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Notice;
import com.cloudchewie.client.fragment.NoticeListFragment;
import com.cloudchewie.client.util.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends BaseActivity {
    NoticeFragmentAdapter adapter;
    NOTICE_TYPE type;
    private TabLayout tabLayout;
    private List<String> titles;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        type = (NOTICE_TYPE) bundle.getSerializable("type");
        StatusBarUtil.setMargin(this);
        setContentView(R.layout.activity_notice);
        findViewById(R.id.activity_notice_back).setOnClickListener(v -> finish());
        initTabLayout();
    }

    void initTabLayout() {
        viewPager = findViewById(R.id.activity_notice_viewpager);
        tabLayout = findViewById(R.id.activity_notice_tab_layout);
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        switch (type) {
            case COMMENT_REPLY:
                titles.add("评论");
                titles.add("回复");
                NoticeListFragment fragment = new NoticeListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.COMMENT);
                fragment.setArguments(bundle);
                fragments.add(fragment);
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.REPLY);
                fragment.setArguments(bundle);
                fragments.add(fragment);
                break;
            case THUMBUP_COLLECT:
                titles.add("点赞");
                titles.add("收藏");
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.THUMBUP);
                fragment.setArguments(bundle);
                fragments.add(fragment);
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.COLLECT);
                fragment.setArguments(bundle);
                fragments.add(fragment);
                break;
            case FOLLOW:
                titles.add("关注");
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.FOLLOW);
                fragment.setArguments(bundle);
                fragments.add(fragment);
                break;
        }
        adapter = new NoticeFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
    }

    public enum NOTICE_TYPE {
        COMMENT_REPLY, THUMBUP_COLLECT, FOLLOW
    }

    public class NoticeFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public NoticeFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
