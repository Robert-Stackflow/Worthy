/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 14:05:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import static com.cloudchewie.client.util.StringUtil.dealNewLine;
import static com.cloudchewie.client.util.TimeUtil.dateToString;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.fragment.CommentsFragment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.IconTextItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailActivity extends BaseActivity {
    Comment comment;
    CommentDetailActivity.CommentDetailFragmentAdapter adapter;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        comment = (Comment) intent.getSerializableExtra("comment");
        setContentView(R.layout.activity_comment_detail);
        findViewById(R.id.comment_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.comment_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        tabLayout = findViewById(R.id.comment_detail_comment_tab_layout);
        viewPager = findViewById(R.id.comment_detail_content_viewpager);
        ((TextView) findViewById(R.id.comment_detail_username)).setText(comment.getUsername());
        ((TextView) findViewById(R.id.comment_detail_time)).setText(dateToString(comment.getDate()));
        ((TextView) findViewById(R.id.comment_detail_content)).setText(dealNewLine(comment.getContent()));
        ((IconTextItem) findViewById(R.id.comment_detail_comment)).setText(String.valueOf(comment.getCommentCount()));
        ((IconTextItem) findViewById(R.id.comment_detail_thumbup)).setText(String.valueOf(comment.getThumbupCount()));
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("热门回复");
        titles.add("全部回复");
        fragments.add(new CommentsFragment());
        fragments.add(new CommentsFragment());
        adapter = new CommentDetailActivity.CommentDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
    }

    public class CommentDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public CommentDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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