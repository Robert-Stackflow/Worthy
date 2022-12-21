/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:14:10
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.user;

import static java.lang.Math.abs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.fragment.ImageViewFragment;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.widget.AppBarStateChangeListener;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.previewlibrary.GPreviewBuilder;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class HomePageActivity extends AppCompatActivity {
    private Context mContext;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private boolean mIsBlurred = false;
    private UserViewInfo mAvatarInfo;
    private UserViewInfo mBackGroundInfo;
    private UserViewInfo mSmallAvatarInfo;
    //基本控件
    private Toolbar mToolBar;
    private AppBarLayout mAppBar;
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private ImageView mBackGroundView;
    private ImageView mAvatarView;
    private TextView mUsernameView;
    private ImageView mSmallAvatarView;
    private TextView mSmallUsernameView;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private HomePageFragmentAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mContext = getApplicationContext();
        mToolBar = findViewById(R.id.home_page_toolbar);
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
        initView();
        initBlur();
        initTabLayout();
        mAvatarInfo = new UserViewInfo("https://hellorfimg.zcool.cn/preview260/658596682.jpg");
        mSmallAvatarInfo = new UserViewInfo("https://hellorfimg.zcool.cn/preview260/658596682.jpg");
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mSmallAvatarView);
        mBackGroundInfo = new UserViewInfo("https://hellorfimg.zcool.cn/preview260/658596682.jpg");
        Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
    }


    void initView() {
        mBackButton.setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_home_page_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.home_page_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        mBackGroundView.setOnClickListener(v -> {
            List<UserViewInfo> mThumbViewInfoList = new ArrayList<>();
            mBackGroundInfo.setBounds(NineGridUtil.getBounds(mBackGroundView));
            mThumbViewInfoList.add(mBackGroundInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mAvatarView.setOnClickListener(v -> {
            List<UserViewInfo> mThumbViewInfoList = new ArrayList<>();
            mAvatarInfo.setBounds(NineGridUtil.getBounds(mAvatarView));
            mThumbViewInfoList.add(mAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mSmallAvatarView.setOnClickListener(v -> {
            List<UserViewInfo> mThumbViewInfoList = new ArrayList<>();
            mSmallAvatarInfo.setBounds(NineGridUtil.getBounds(mSmallAvatarView));
            mThumbViewInfoList.add(mSmallAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setUserFragment(ImageViewFragment.class).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
    }

    void initBlur() {
        UltimateBarX.statusBarOnly(this).fitWindow(true).transparent().apply();
        UltimateBarX.addStatusBarTopPadding(mToolBar);
        mToolBar.setContentInsetsRelative(0, 0);
        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
//                    mBackGroundView.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.img_default_background));
                    mIsBlurred = false;
                } else if (state == State.COLLAPSED) {
                    mIsBlurred = true;
                    Blurry.with(mContext).animate(500).radius(abs(offset) % 40).async().capture(mBackGroundView).into(mBackGroundView);
                }
                if (abs(offset) > 2 * appBarLayout.getTotalScrollRange() / 3) {
                    mSmallAvatarView.setVisibility(View.VISIBLE);
                    mSmallUsernameView.setVisibility(View.VISIBLE);
                } else {
                    mSmallAvatarView.setVisibility(View.GONE);
                    mSmallUsernameView.setVisibility(View.GONE);
                    Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
//                    mBackGroundView.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.img_default_background));
                    mIsBlurred = false;
                }
            }
        });
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.home_page_tab_titles));
        for (String str : mTitles) {
            BaseFragment fragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
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