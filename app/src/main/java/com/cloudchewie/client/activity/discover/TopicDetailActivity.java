/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:57:46
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.fragment.global.CreateDialogFragment;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.client.util.listener.AppBarStateChangeListener;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TopicDetailActivity extends BaseActivity implements View.OnClickListener {
    private Topic mTopic;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mNameView;
    private TextView mSmallNameView;
    private TextView mDescribeView;
    private TextView mHotValueView;
    private ConstraintLayout mTitleBar;
    private ConstraintLayout mTitleBar2;
    private RelativeLayout mMainLayout;
    private ConstraintLayout mContentBar;
    private AppBarLayout mAppBar;
    private Button mFollowButton;
    private Button mSmallFollowButton;
    private Button mSortButton;
    private ConstraintLayout mFabLayout;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private TopicDetailFragmentAdapter mAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        mBackButton = findViewById(R.id.activity_topic_detail_back);
        mMoreButton = findViewById(R.id.activity_topic_detail_more);
        mNameView = findViewById(R.id.activity_topic_detail_name);
        mSmallNameView = findViewById(R.id.activity_topic_detail_small_name);
        mDescribeView = findViewById(R.id.activity_topic_detail_describe);
        mHotValueView = findViewById(R.id.activity_topic_detail_hotvalue);
        mTabLayout = findViewById(R.id.activity_topic_detail_content_tab_layout);
        mViewPager = findViewById(R.id.activity_topic_detail_content_viewpager);
        mAppBar = findViewById(R.id.activity_topic_detail_appbar);
        mTitleBar = findViewById(R.id.activity_topic_detail_titlebar);
        mTitleBar2 = findViewById(R.id.activity_topic_detail_titlebar_2);
        mMainLayout = findViewById(R.id.activity_topic_detail_layout);
        mContentBar = findViewById(R.id.activity_topic_detail_content_bar);
        mFollowButton = findViewById(R.id.activity_topic_detail_follow);
        mSmallFollowButton = findViewById(R.id.activity_topic_detail_follow_2);
        mSortButton = findViewById(R.id.activity_topic_detail_sort);
        mFabLayout = findViewById(R.id.activity_topic_detail_fab_layout);
        mTitleBar2.setAlpha(0f);
        initView();
        initViewPager();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        mBackButton.setOnClickListener(this);
        mMoreButton.setOnClickListener(this);
        mFollowButton.setOnClickListener(this);
        mSmallFollowButton.setOnClickListener(this);
        mSortButton.setOnClickListener(this);
        mFabLayout.setOnClickListener(view -> new CreateDialogFragment().show(getSupportFragmentManager(), ""));
        findViewById(R.id.activity_topic_detail_fab_button).setOnClickListener(view -> new CreateDialogFragment().show(getSupportFragmentManager(), ""));
        List<String> sortList = Arrays.asList(getResources().getStringArray(R.array.sort));
        mSortButton.setText(sortList.get(0));
        findViewById(R.id.activity_topic_detail_back_2).setOnClickListener(this);
        Intent intent = this.getIntent();
        mTopic = (Topic) intent.getSerializableExtra("topic");
        mNameView.setText(mTopic.getName());
        mSmallNameView.setText(mTopic.getName());
        mDescribeView.setText(mTopic.getDescribe());
        mHotValueView.setText(mTopic.getVisitorCount() + "热度 · " + mTopic.getFollowerCount() + "人关注");
        if (mTopic.getMyType() != Topic.FOLLOW_TYPE.FOLLOWED) {
            mFollowButton.setText("已关注");
            mSmallFollowButton.setText("已关注");
        }
        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    mTitleBar2.setAlpha(0f);
                    mTitleBar2.setVisibility(View.GONE);
                    mMainLayout.setBackgroundColor(getColor(R.color.color_prominent));
                    mTitleBar.setBackgroundColor(getColor(R.color.color_prominent));
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_round_top_dp15));
                    mContentBar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.content_background)));
                } else if (state == State.COLLAPSED) {
                    mTitleBar2.setAlpha(1f);
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mMainLayout.setBackgroundColor(getColor(R.color.content_background));
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_rect));
                    mContentBar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.content_background)));
                } else {
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mTitleBar2.setAlpha(1f * Math.abs(offset) / appBarLayout.getTotalScrollRange());
                    mMainLayout.setBackgroundColor(ColorUtils.blendARGB(getColor(R.color.color_prominent), getColor(R.color.content_background), 1f * Math.abs(offset) / appBarLayout.getTotalScrollRange()));
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_round_top_dp15));
                    mContentBar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.content_background)));
                }
            }
        });
    }

    void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.topic_detail_tab_titles));
        mFragments.add(new PostListFragment(mTopic));
        mFragments.add(new PostListFragment(mTopic));
        mAdapter = new TopicDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public String getTopic() {
        return mTopic.getName();
    }

    @Override
    public void onClick(View view) {
        if (view == mFollowButton || view == mSmallFollowButton) {
            if (mTopic.getMyType() == Topic.FOLLOW_TYPE.UNFOLLOWED) {
                mTopic.setMyType(Topic.FOLLOW_TYPE.FOLLOWED);
                mFollowButton.setText("已关注");
                mSmallFollowButton.setText("已关注");
            } else {
                mTopic.setMyType(Topic.FOLLOW_TYPE.UNFOLLOWED);
                mFollowButton.setText("关注话题");
                mSmallFollowButton.setText("关注话题");
            }
        } else if (view == mBackButton || view == findViewById(R.id.activity_topic_detail_back_2)) {
            finish();
        } else if (view == mMoreButton) {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        } else if (view == mSortButton) {
            List<String> sortList = Arrays.asList(getResources().getStringArray(R.array.sort));
            for (String str : sortList) {
                if (Objects.equals(mSortButton.getText(), str)) {
                    mSortButton.setText(sortList.get((sortList.indexOf(str) + 1) % sortList.size()));
                    ((BaseFragment) mFragments.get(mViewPager.getCurrentItem())).performRefresh();
                    break;
                }
            }
        }
    }

    public class TopicDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public TopicDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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