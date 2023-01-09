package com.cloudchewie.client.activity.create;

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
import com.cloudchewie.client.entity.Draft;
import com.cloudchewie.client.fragment.internal.DraftListFragment;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DraftActivity extends BaseActivity {
    private List<String> mTitles;
    private List<Fragment> mFragments;
    //基本控件
    private ImageView mBackButton;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private DraftFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_draft);
        mBackButton = findViewById(R.id.activity_draft_back);
        mViewPager = findViewById(R.id.activity_draft_viewpager);
        mTabLayout = findViewById(R.id.activity_draft_tab_layout);
        initView();
        initViewPager();
    }

    void initView() {
        mBackButton.setOnClickListener(v -> finish());
    }

    void initViewPager() {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.draft_titles));

        DraftListFragment fragment = new DraftListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", Draft.DRAFT_TYPE.POST);
        fragment.setArguments(bundle);
        mFragments.add(fragment);

        fragment = new DraftListFragment();
        bundle = new Bundle();
        bundle.putSerializable("type", Draft.DRAFT_TYPE.ARTICLE);
        fragment.setArguments(bundle);
        mFragments.add(fragment);

        fragment = new DraftListFragment();
        bundle = new Bundle();
        bundle.putSerializable("type", Draft.DRAFT_TYPE.ATTRACTION);
        fragment.setArguments(bundle);
        mFragments.add(fragment);

        fragment = new DraftListFragment();
        bundle = new Bundle();
        bundle.putSerializable("type", Draft.DRAFT_TYPE.TOPIC);
        fragment.setArguments(bundle);
        mFragments.add(fragment);

        mAdapter = new DraftFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    public enum DRAFT_TYPE {
        COMMENT_REPLY, THUMBUP_COLLECT, FOLLOW
    }

    public class DraftFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public DraftFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
