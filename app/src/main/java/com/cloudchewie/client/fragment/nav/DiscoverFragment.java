/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:44:20
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.SearchActivity;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.fragment.internal.AttractionListFragment;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.widget.FollowingPopupWindow;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class DiscoverFragment extends Fragment implements View.OnClickListener {
    View mainView;
    Context context;
    HomeFragmentAdapter adapter;
    private FollowingPopupWindow popupWindow;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;
    private int followingOption = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_discover, null);
        StatusBarUtil.setMargin(mainView.findViewById(R.id.discover_titlebar), 0, StatusBarUtil.getHeight(getActivity()), 0, 0);
        context = getContext();
        tabLayout = mainView.findViewById(R.id.discover_tab_layout);
        viewPager = mainView.findViewById(R.id.discover_viewpager);
        initViewPager();
        initTabLayout();
        return mainView;
    }

    void initViewPager() {
        fragments = new ArrayList<>(3);
        titles = new ArrayList<>(3);
        titles.add("地点");
        titles.add("推荐");
        titles.add("关注");
        fragments.add(new AttractionListFragment());
        fragments.add(new PostListFragment());
        fragments.add(new PostListFragment());
        adapter = new HomeFragmentAdapter(getChildFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 2) tabLayout.getTabAt(position).setText("关注▾");
                else tabLayout.getTabAt(2).setText("关注");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    void initTabLayout() {
        mainView.findViewById(R.id.discover_search).setOnClickListener(this);
        {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.view.setLongClickable(false);
                    int finalI = i;
                    tab.view.setOnClickListener(v -> ((BaseFragment) fragments.get(finalI)).performRefresh());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tab.view.setTooltipText(null);
                    }
                }
            }
            tabLayout.getTabAt(2).view.setOnClickListener(view -> {
                if (viewPager.getCurrentItem() == 2) showPopupwindow(2, view);
            });
        }
    }

    private void showPopupwindow(final int position, View v) {
        if (popupWindow == null || (popupWindow != null && !popupWindow.isShowing())) {
            tabLayout.getTabAt(position).setText("关注▴");
            popupWindow = new FollowingPopupWindow(getContext(), followingOption);
            popupWindow.showPopupWindow(v);
            popupWindow.setAlignBackground(true);
            popupWindow.setAlignBackgroundGravity(Gravity.TOP);
            popupWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    followingOption = popupWindow.getCurrentOption();
                    tabLayout.getTabAt(position).setText("关注▾");
                    if (popupWindow.isOptionChanged())
                        ((PostListFragment) fragments.get(position)).performRefresh();
                }
            });
        } else {
            if (popupWindow.isShowing()) popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.discover_search)) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }
    }

    public class HomeFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public HomeFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
