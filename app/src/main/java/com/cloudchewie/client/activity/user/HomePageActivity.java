/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:14:10
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.entity.User;
import com.cloudchewie.client.fragment.global.BaseFragment;
import com.cloudchewie.client.fragment.global.ImageViewFragment;
import com.cloudchewie.client.fragment.global.StateFragment;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.listener.AppBarStateChangeListener;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.previewlibrary.GPreviewBuilder;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private User mUser;
    private Context mContext;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ImageViewInfo mAvatarInfo;
    private ImageViewInfo mBackGroundInfo;
    private ImageViewInfo mSmallAvatarInfo;
    //基本控件
    private AppBarLayout mAppBar;
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private ImageView mBackGroundView;
    private ImageView mAvatarView;
    private TextView mUsernameView;
    private ImageView mSmallAvatarView;
    private ConstraintLayout mTitleBar;
    private ConstraintLayout mTitleBar2;
    private RelativeLayout mMainLayout;
    private TextView mSmallUsernameView;
    private IconTextItem mLocationView;
    private IconTextItem mGenderView;
    private ConstraintLayout mContentBar;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private HomePageFragmentAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mContext = getApplicationContext();
        mAppBar = findViewById(R.id.home_page_appbar);
        mBackButton = findViewById(R.id.home_page_back);
        mMoreButton = findViewById(R.id.home_page_more);
        mBackGroundView = findViewById(R.id.home_page_background);
        mAvatarView = findViewById(R.id.home_page_avatar);
        mUsernameView = findViewById(R.id.home_page_username);
        mSmallAvatarView = findViewById(R.id.home_page_small_avatar);
        mSmallUsernameView = findViewById(R.id.home_page_small_username);
        mTabLayout = findViewById(R.id.home_page_content_tab_layout);
        mViewPager = findViewById(R.id.home_page_content_viewpager);
        mLocationView = findViewById(R.id.home_page_ipaddress);
        mGenderView = findViewById(R.id.home_page_gender);
        mTitleBar = findViewById(R.id.home_page_titlebar);
        mTitleBar2 = findViewById(R.id.home_page_titlebar_2);
        mMainLayout = findViewById(R.id.home_page_layout);
        mContentBar = findViewById(R.id.home_page_content_bar);
        initView();
        initTabLayout();
        initGlide();
    }

    void initGlide() {
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mSmallAvatarView);
        Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
        if (StatusBarUtil.setStatusBarTextColor(HomePageActivity.this, mBackGroundInfo.getUrl())) {
            mBackButton.setImageTintList(getColorStateList(R.color.color_selector_icon_light));
            mMoreButton.setImageTintList(getColorStateList(R.color.color_selector_icon_light));
        } else {
            mBackButton.setImageTintList(getColorStateList(R.color.color_selector_icon_dark));
            mMoreButton.setImageTintList(getColorStateList(R.color.color_selector_icon_dark));
        }
    }

    void initView() {
        Intent intent = this.getIntent();
        mUser = (User) intent.getSerializableExtra("user");
        mAvatarInfo = new ImageViewInfo(mUser.getAvatarUrl());
        mSmallAvatarInfo = new ImageViewInfo(mUser.getAvatarUrl());
        mBackGroundInfo = new ImageViewInfo(mUser.getBackgroundUrl());
        mBackButton.setOnClickListener(v -> finish());
        findViewById(R.id.home_page_back_2).setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_home_page_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.home_page_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        mBackGroundView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mBackGroundInfo.setBounds(NineGridUtil.getBounds(mBackGroundView));
            mThumbViewInfoList.add(mBackGroundInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mAvatarInfo.setBounds(NineGridUtil.getBounds(mAvatarView));
            mThumbViewInfoList.add(mAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mSmallAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mSmallAvatarInfo.setBounds(NineGridUtil.getBounds(mSmallAvatarView));
            mThumbViewInfoList.add(mSmallAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mUsernameView.setText(mUser.getUsername());
        mSmallUsernameView.setText(mUser.getUsername());
        mGenderView.setText(mUser.getGenderInfo());
        mLocationView.setText(mUser.getCurrentLocation());
        mTitleBar2.setAlpha(0f);
        UltimateBarX.statusBarOnly(this).fitWindow(true).transparent().apply();
        UltimateBarX.addStatusBarTopPadding(mTitleBar);
        UltimateBarX.addStatusBarTopPadding(mTitleBar2);
        UltimateBarX.addStatusBarTopPadding(mContentBar);
        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    mTitleBar2.setAlpha(0f);
                    mTitleBar2.setVisibility(View.GONE);
                    Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
                } else if (state == State.COLLAPSED) {
                    mTitleBar2.setAlpha(1f);
                    mTitleBar2.setVisibility(View.VISIBLE);
                }
                mTitleBar2.setVisibility(View.VISIBLE);
                mTitleBar2.setAlpha(1f * Math.abs(offset) / appBarLayout.getTotalScrollRange());
                Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
            }
        });
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.home_page_tab_titles));
        BaseFragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", mTitles.get(0));
        fragment.setArguments(bundle);
        mFragments.add(new StateFragment());
        mFragments.add(new PostListFragment());
        mFragments.add(new PostListFragment());
        mAdapter = new HomePageFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public class HomePageFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public HomePageFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
