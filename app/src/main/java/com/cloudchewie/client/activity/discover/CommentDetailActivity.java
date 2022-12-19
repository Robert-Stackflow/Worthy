/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 16:24:47
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import static com.cloudchewie.client.util.basic.DateUtil.beautifyTime;
import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

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
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.fragment.internal.CommentListFragment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentDetailActivity extends BaseActivity {
    private Comment mComment;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mUserNameView;
    private TextView mTimeView;
    private TextView mContentView;
    private IconTextItem mReplyCountView;
    private IconTextItem mThumbupCountView;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private CommentDetailFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        mBackButton = findViewById(R.id.activity_comment_detail_back);
        mMoreButton = findViewById(R.id.activity_comment_detail_more);
        mViewPager = findViewById(R.id.activity_comment_detail_content_viewpager);
        mTabLayout = findViewById(R.id.activity_comment_detail_comment_tab_layout);
        mUserNameView = findViewById(R.id.activity_comment_detail_username);
        mTimeView = findViewById(R.id.activity_comment_detail_time);
        mContentView = findViewById(R.id.activity_comment_detail_content);
        mReplyCountView = findViewById(R.id.activity_comment_detail_replies_count);
        mThumbupCountView = findViewById(R.id.activity_comment_detail_thumbup_count);
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
        mComment = (Comment) intent.getSerializableExtra("comment");
        mUserNameView.setText(mComment.getUsername());
        mTimeView.setText(beautifyTime(mComment.getDate()));
        mContentView.setText(handleLineBreaks(mComment.getContent()));
        mReplyCountView.setText(String.valueOf(mComment.getReplyCount()));
        mThumbupCountView.setText(String.valueOf(mComment.getThumbupCount()));
    }

    void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.comment_detail_tab_titles));
        mTitles.set(mTitles.size() - 1, mTitles.get(mTitles.size() - 1) + "(" + mComment.getReplyCount() + ")");
        mFragments.add(new CommentListFragment());
        mFragments.add(new CommentListFragment());
        mAdapter = new CommentDetailActivity.CommentDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public class CommentDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public CommentDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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