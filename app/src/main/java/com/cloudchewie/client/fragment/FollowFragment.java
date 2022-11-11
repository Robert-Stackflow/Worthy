package com.cloudchewie.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FollowFragment extends Fragment implements View.OnClickListener {
    View mainView;
    FollowFragmentAdapter adapter;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_follow, null);
        tabLayout = mainView.findViewById(R.id.follow_tab_layout);
        viewPager = mainView.findViewById(R.id.follow_viewpager);
        fragments = new ArrayList<>(3);
        titles = new ArrayList<>(3);
        BaseFragment attractionFragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "关注的人");
        attractionFragment.setArguments(bundle);

        BaseFragment postFragment = new BaseFragment();
        bundle = new Bundle();
        bundle.putString("title", "关注的地点");
        postFragment.setArguments(bundle);

        BaseFragment articleFragment = new BaseFragment();
        bundle = new Bundle();
        bundle.putString("title", "关注的话题");
        articleFragment.setArguments(bundle);
        fragments.add(attractionFragment);
        fragments.add(postFragment);
        fragments.add(articleFragment);
        titles.add("关注的人");
        titles.add("地点");
        titles.add("话题");
        adapter = new FollowFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return mainView;
    }

    @Override
    public void onClick(View view) {
    }

    public class FollowFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList;
        private final List<String> titleList;

        public FollowFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
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
