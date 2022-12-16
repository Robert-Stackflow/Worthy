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
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.ui.BottomSheet;
import com.cloudchewie.client.ui.IconTextItem;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity {
    Post post;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
    PostDetailActivity.PostDetailFragmentAdapter adapter;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        post = (Post) intent.getSerializableExtra("post");
        setContentView(R.layout.activity_post_detail);
        findViewById(R.id.post_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.post_detail_more).setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        tabLayout = findViewById(R.id.post_detail_comment_tab_layout);
        viewPager = findViewById(R.id.post_detail_content_viewpager);
        ((TextView) findViewById(R.id.post_detail_username)).setText(post.getUsername());
        ((TextView) findViewById(R.id.post_detail_time)).setText(dateToString(post.getDate()));
        ((TextView) findViewById(R.id.post_detail_content)).setText(dealNewLine(post.getContent()));
        ((IconTextItem) findViewById(R.id.post_detail_attraction)).setText(post.getLocation());
        ((IconTextItem) findViewById(R.id.post_detail_topic)).setText(post.getTag());
        ((IconTextItem) findViewById(R.id.post_detail_collection)).setText(String.valueOf(post.getCollectionCount()));
        ((IconTextItem) findViewById(R.id.post_detail_comment)).setText(String.valueOf(post.getCommentCount()));
        ((IconTextItem) findViewById(R.id.post_detail_thumbup)).setText(String.valueOf(post.getThumbupCount()));
        findViewById(R.id.post_detail_topic).setOnClickListener(v -> {
            Intent topicDetailIntent = new Intent(PostDetailActivity.this, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            Topic topic = new Topic(((IconTextItem) findViewById(R.id.post_detail_topic)).getText(), "这里大家可以尽情分享我们的生活", (int) (Math.random() * 100000), (int) (Math.random() * 1000));
            bundle.putSerializable("topic", topic);
            topicDetailIntent.putExtras(bundle);
            startActivity(topicDetailIntent);
        });
        findViewById(R.id.post_detail_attraction).setOnClickListener(v -> {
            Attraction attraction = new Attraction(((IconTextItem) findViewById(R.id.post_detail_attraction)).getText(), "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
            Intent attractionDetailIntent = new Intent(PostDetailActivity.this, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", attraction);
            attractionDetailIntent.putExtras(bundle);
            startActivity(attractionDetailIntent);
        });
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("热门评论");
        titles.add("全部评论");
        for (String str : titles) {
            BaseFragment fragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter = new PostDetailActivity.PostDetailFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PostDetailFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList;
        private final List<String> titleList;

        public PostDetailFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
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
