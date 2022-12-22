/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 22:57:21
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.message;

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
import com.cloudchewie.client.domin.Notice;
import com.cloudchewie.client.fragment.internal.NoticeListFragment;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoticeActivity extends BaseActivity {
    NOTICE_TYPE mType;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private NoticeFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_notice);
        mBackButton = findViewById(R.id.activity_notice_back);
        mViewPager = findViewById(R.id.activity_notice_viewpager);
        mTabLayout = findViewById(R.id.activity_notice_tab_layout);
        initView();
        initViewPager();
    }

    void initView() {
        Bundle bundle = getIntent().getExtras();
        mType = (NOTICE_TYPE) bundle.getSerializable("type");
        mBackButton.setOnClickListener(v -> finish());
    }

    void initViewPager() {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
        switch (mType) {
            case COMMENT_REPLY:
                mTitles = Arrays.asList(getResources().getStringArray(R.array.notice_comment_reply_tab_titles));
                NoticeListFragment fragment = new NoticeListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.COMMENT);
                fragment.setArguments(bundle);
                mFragments.add(fragment);
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.REPLY);
                fragment.setArguments(bundle);
                mFragments.add(fragment);
                break;
            case THUMBUP_COLLECT:
                mTitles = Arrays.asList(getResources().getStringArray(R.array.notice_thumbup_collection_tab_titles));
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.THUMBUP);
                fragment.setArguments(bundle);
                mFragments.add(fragment);
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.COLLECT);
                fragment.setArguments(bundle);
                mFragments.add(fragment);
                break;
            case FOLLOW:
                mTitles = Arrays.asList(getResources().getStringArray(R.array.notice_follow_tab_titles));
                fragment = new NoticeListFragment();
                bundle = new Bundle();
                bundle.putSerializable("type", Notice.NOTICE_TYPE.FOLLOW);
                fragment.setArguments(bundle);
                mFragments.add(fragment);
                break;
        }
        mAdapter = new NoticeFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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
