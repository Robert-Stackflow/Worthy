/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.cloudchewie.client.fragment.global.StateFragment;
import com.cloudchewie.client.fragment.internal.AttractionListFragment;
import com.cloudchewie.client.fragment.internal.PostListFragment;
import com.cloudchewie.client.fragment.internal.UserListFragment;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.search.SearchLayout;
import com.cloudchewie.ui.search.SearchList;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    SearchLayout searchLayout;
    SearchList searchList;
    TextView confirmView;
    ImageView backButton;
    EditText searchInput;
    View divider;
    private Context mContext;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private SearchResultFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_search);
        searchLayout = findViewById(R.id.activity_search_search_layout);
        searchList = findViewById(R.id.activity_search_search_list);
        confirmView = findViewById(R.id.activity_search_confirm);
        backButton = findViewById(R.id.activity_search_back);
        divider = findViewById(R.id.activity_search_result_divider);
        mContext = this;
        mTabLayout = findViewById(R.id.activity_search_result_tab_layout);
        mViewPager = findViewById(R.id.activity_search_result_viewpager);
        searchInput = searchLayout.getSearchEdit();
        initView();
        initTabLayout();
        toggleMode(false);
    }

    void initView() {
        confirmView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        searchLayout.setOnTextSearchListener(s -> {
            if (TextUtils.isEmpty(s)) {
                confirmView.setEnabled(false);
                confirmView.setClickable(false);
                confirmView.setTextColor(getColor(R.color.color_gray));
                toggleMode(false);
            } else {
                confirmView.setEnabled(true);
                confirmView.setClickable(true);
                confirmView.setTextColor(getColor(R.color.color_prominent));
            }
            return null;
        }, s -> {
            searchList.doSearchContent(s);
            performSearch();
            return null;
        });
        searchList.setOnHistoryItemClickListener((s, integer) -> {
            searchInput.setText(s);
            performSearch();
            return null;
        });
        confirmView.setEnabled(false);
        confirmView.setClickable(false);
        confirmView.setTextColor(getColor(R.color.color_gray));
    }

    void toggleMode(boolean isSearchResult) {
        if (isSearchResult) {
            mViewPager.setCurrentItem(0);
            mTabLayout.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            searchList.setVisibility(View.VISIBLE);
            divider.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == confirmView) {
            searchList.doSearchContent(searchInput.getText().toString());
            performSearch();
        } else if (view == backButton)
            finish();
    }

    public void performSearch() {
        toggleMode(true);
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.search_result_titles));
        mFragments.add(new StateFragment());
        mFragments.add(new AttractionListFragment());
        mFragments.add(new PostListFragment());
        mFragments.add(new PostListFragment());
        mFragments.add(new UserListFragment());
        mFragments.add(new StateFragment());
        mAdapter = new SearchResultFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public class SearchResultFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public SearchResultFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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