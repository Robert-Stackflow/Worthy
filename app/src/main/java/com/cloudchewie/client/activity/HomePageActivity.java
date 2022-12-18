/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import static java.lang.Math.abs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.widget.AppBarStateChangeListener;
import com.cloudchewie.ui.BottomSheet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class HomePageActivity extends AppCompatActivity {
    RefreshLayout swipeRefreshLayout;
    Context context;
    boolean isBlurred = false;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;
    private HomePageFragmentAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        context = getApplicationContext();
        UltimateBarX.statusBarOnly(this).fitWindow(true).transparent().apply();
        UltimateBarX.addStatusBarTopPadding(findViewById(R.id.home_page_toolbar));
        findViewById(R.id.home_page_back).setOnClickListener(v -> finish());
        findViewById(R.id.home_page_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        ((Toolbar) findViewById(R.id.home_page_toolbar)).setContentInsetsRelative(0, 0);
        ((AppBarLayout) (findViewById(R.id.home_page_appbar))).addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    ((ImageView) findViewById(R.id.home_page_background)).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.img_default_background));
                    isBlurred = false;
                } else if (state == State.COLLAPSED) {
                    isBlurred = true;
                    Blurry.with(context).animate(500).radius(abs(offset) % 40).async().capture(findViewById(R.id.home_page_background)).into(findViewById(R.id.home_page_background));
                }
                if (abs(offset) > 2 * appBarLayout.getTotalScrollRange() / 3) {
                    findViewById(R.id.home_page_small_avatar).setVisibility(View.VISIBLE);
                    findViewById(R.id.home_page_small_username).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.home_page_small_avatar).setVisibility(View.GONE);
                    findViewById(R.id.home_page_small_username).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.home_page_background)).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.img_default_background));
                    isBlurred = false;
                }
            }
        });
        initSwipeRefresh();
        initTabLayout();
        {

        }
    }

    void initTabLayout() {
        tabLayout = findViewById(R.id.home_page_content_tab_layout);
        viewPager = findViewById(R.id.home_page_content_viewpager);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("信息");
        titles.add("帖子");
        titles.add("文章");
        titles.add("去过");
        titles.add("想去");
        for (String str : titles) {
            BaseFragment fragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter = new HomePageFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
    }

    void initSwipeRefresh() {
//        swipeRefreshLayout = findViewById(R.id.home_page_swipe_refresh);
//        swipeRefreshLayout.setEnableOverScrollDrag(true);
//        swipeRefreshLayout.setEnableOverScrollBounce(true);
//        swipeRefreshLayout.setEnableLoadMore(false);
//        swipeRefreshLayout.setEnablePureScrollMode(true);
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
