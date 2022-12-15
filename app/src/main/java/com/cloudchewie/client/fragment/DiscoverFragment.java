package com.cloudchewie.client.fragment;

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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.SearchActivity;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.cloudchewie.client.util.StatusBarUtil;
import com.cloudchewie.client.widget.FollowingPopupWindow;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class DiscoverFragment extends Fragment implements View.OnClickListener {
    View mainView;
    Context context;
    private FollowingPopupWindow popupWindow;
    HomeFragmentAdapter adapter;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
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
        titles.add(" 地点 ");
        titles.add(" 推荐 ");
        titles.add(" 关注 ");
        fragments.add(new AttractionsFragment());
        fragments.add(new PostsFragment());
        fragments.add(new PostsFragment());
        adapter = new HomeFragmentAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) tabLayout.getTabAt(position).setText(" 关注▾");
                else tabLayout.getTabAt(2).setText(" 关注 ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
            tabLayout.getTabAt(position).setText(" 关注▴");
            popupWindow = new FollowingPopupWindow(getContext(), followingOption);
            popupWindow.showPopupWindow(v);
            popupWindow.setAlignBackground(true);
            popupWindow.setAlignBackgroundGravity(Gravity.TOP);
            popupWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    followingOption = popupWindow.getCurrentOption();
                    tabLayout.getTabAt(position).setText(" 关注▾");
                    if (popupWindow.isOptionChanged())
                        ((PostsFragment) fragments.get(position)).performRefresh();
                }
            });
        } else {
            if (popupWindow.isShowing())
                popupWindow.dismiss();
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

    public class HomeFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList;
        private final List<String> titleList;

        public HomeFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
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
