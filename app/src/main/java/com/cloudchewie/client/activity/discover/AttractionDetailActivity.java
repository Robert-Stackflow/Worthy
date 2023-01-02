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
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.fragment.global.CreateDialogFragment;
import com.cloudchewie.client.fragment.global.StateFragment;
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

public class AttractionDetailActivity extends BaseActivity implements View.OnClickListener {
    private List<String> mTitles;
    private Attraction mAttraction;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mNameView;
    private TextView mSmallNameView;
    private AppBarLayout mAppBar;
    private TextView mLocationView;
    private TextView mDescribeView;
    private ConstraintLayout mTitleBar;
    private ConstraintLayout mTitleBar2;
    private RelativeLayout mMainLayout;
    private ConstraintLayout mContentBar;
    private Button mFollowButton;
    private Button mSmallFollowButton;
    private Button mSortButton;
    private ConstraintLayout mFabLayout;
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
        mSmallNameView = findViewById(R.id.activity_attraction_detail_small_name);
        mLocationView = findViewById(R.id.activity_attraction_detail_location);
        mDescribeView = findViewById(R.id.activity_attraction_detail_describe);
        mTabLayout = findViewById(R.id.activity_attraction_detail_content_tab_layout);
        mViewPager = findViewById(R.id.activity_attraction_detail_content_viewpager);
        mAppBar = findViewById(R.id.activity_attraction_detail_appbar);
        mTitleBar = findViewById(R.id.activity_attraction_detail_titlebar);
        mTitleBar2 = findViewById(R.id.activity_attraction_detail_titlebar_2);
        mMainLayout = findViewById(R.id.activity_attraction_detail_layout);
        mContentBar = findViewById(R.id.activity_attraction_detail_content_bar);
        mFollowButton = findViewById(R.id.activity_attraction_detail_follow);
        mSmallFollowButton = findViewById(R.id.activity_attraction_detail_follow_2);
        mSortButton = findViewById(R.id.activity_attraction_detail_sort);
        mFabLayout = findViewById(R.id.activity_attraction_detail_fab_layout);
        initView();
        initViewPager();
    }

    public String getAttraction() {
        return mAttraction.getName();
    }

    void initView() {
        mBackButton.setOnClickListener(this);
        mMoreButton.setOnClickListener(this);
        mFollowButton.setOnClickListener(this);
        mSmallFollowButton.setOnClickListener(this);
        mSortButton.setOnClickListener(this);
        mFabLayout.setOnClickListener(view -> new CreateDialogFragment().show(getSupportFragmentManager(), ""));
        findViewById(R.id.activity_attraction_detail_fab_button).setOnClickListener(view -> new CreateDialogFragment().show(getSupportFragmentManager(), ""));
        List<String> sortList = Arrays.asList(getResources().getStringArray(R.array.sort));
        mSortButton.setText(sortList.get(0));
        findViewById(R.id.activity_attraction_detail_back_2).setOnClickListener(this);
        Intent intent = this.getIntent();
        mAttraction = (Attraction) intent.getSerializableExtra("attraction");
        mNameView.setText(mAttraction.getName());
        mSmallNameView.setText(mAttraction.getName());
        mLocationView.setText(mAttraction.getLocation());
        mDescribeView.setText(mAttraction.getDescribe());
        if (mAttraction.getMyType() != Attraction.FOLLOW_TYPE.NONE) {
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
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_content_top_radius_unclickable));
                } else if (state == State.COLLAPSED) {
                    mTitleBar2.setAlpha(1f);
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mMainLayout.setBackgroundColor(getColor(R.color.content_background));
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_content));
                } else {
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mTitleBar2.setAlpha(1f * Math.abs(offset) / appBarLayout.getTotalScrollRange());
                    mMainLayout.setBackgroundColor(ColorUtils.blendARGB(getColor(R.color.color_prominent), getColor(R.color.content_background), 1f * Math.abs(offset) / appBarLayout.getTotalScrollRange()));
                    mContentBar.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.shape_content_top_radius_unclickable));
                }
            }
        });
        mTitleBar2.setAlpha(0f);
    }

    void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.attraction_detail_tab_titles));
        mFragments.add(new PostListFragment(mAttraction));
        mFragments.add(new PostListFragment(mAttraction));
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

    @Override
    public void onClick(View view) {
        if (view == mFollowButton || view == mSmallFollowButton) {
            if (mAttraction.getMyType() == Attraction.FOLLOW_TYPE.NONE) {
                mAttraction.setMyType(Attraction.FOLLOW_TYPE.WANT);
                mFollowButton.setText("已关注");
                mSmallFollowButton.setText("已关注");
            } else {
                mAttraction.setMyType(Attraction.FOLLOW_TYPE.NONE);
                mFollowButton.setText("关注地点");
                mSmallFollowButton.setText("关注地点");
            }
        } else if (view == mBackButton || view == findViewById(R.id.activity_attraction_detail_back_2)) {
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