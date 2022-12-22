/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:22:59
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import static com.cloudchewie.client.util.basic.DateUtil.beautifyTime;
import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.adapter.MyNineGridImageViewAdapter;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.fragment.internal.CommentListFragment;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.system.OnKeyboardChangeListener;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.EntryItem;
import com.cloudchewie.ui.IconTextItem;
import com.cloudchewie.ui.InputItem;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends BaseActivity implements OnKeyboardChangeListener.OnChangeListener {
    private Post mPost;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private String inputString;
    //基础控件
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mUserNameView;
    private TextView mTimeView;
    private TextView mContentView;
    private IconTextItem mTopicView;
    private IconTextItem mLocationView;
    private EntryItem mCommentsCountView;
    private EntryItem mThumbupCountView;
    private EntryItem mCollectionCountView;
    private CircleImageView mAvatarView;
    private InputItem mInputItem;
    private EditText mEditText;
    private TextView mSendView;
    private LinearLayout mBottomInputLayout;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private PostDetailFragmentAdapter mAdapter;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NineGridImageView<UserViewInfo> mNineGridImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mBackButton = findViewById(R.id.activity_post_detail_back);
        mMoreButton = findViewById(R.id.activity_post_detail_more);
        mAvatarView = findViewById(R.id.activity_post_detail_avatar);
        mViewPager = findViewById(R.id.activity_post_detail_content_viewpager);
        mTabLayout = findViewById(R.id.activity_post_detail_comment_tab_layout);
        mUserNameView = findViewById(R.id.activity_post_detail_username);
        mTimeView = findViewById(R.id.activity_post_detail_time);
        mContentView = findViewById(R.id.activity_post_detail_content);
        mTopicView = findViewById(R.id.activity_post_detail_topic);
        mLocationView = findViewById(R.id.activity_post_detail_location);
        mCommentsCountView = findViewById(R.id.activity_post_detail_comment_count);
        mThumbupCountView = findViewById(R.id.activity_post_detail_thumbup_count);
        mCollectionCountView = findViewById(R.id.activity_post_detail_collection_count);
        mTabLayout = findViewById(R.id.activity_post_detail_comment_tab_layout);
        mViewPager = findViewById(R.id.activity_post_detail_content_viewpager);
        mNineGridImageView = findViewById(R.id.activity_post_detail_nine_grid);
        mCollapsingToolbarLayout = findViewById(R.id.activity_post_detail_collapsing_toolbar_layout);
        mAppBarLayout = findViewById(R.id.activity_post_detail_appbar);
        mInputItem = findViewById(R.id.activity_post_detail_input_comment);
        mEditText = mInputItem.getEditText();
        mSendView = findViewById(R.id.activity_post_detail_send);
        mBottomInputLayout = findViewById(R.id.activity_post_detail_statistics_layout);
        initView();
        initViewPager();
    }

    private void initView() {
        Intent intent = this.getIntent();
        mPost = (Post) intent.getSerializableExtra("post");
        mAppBarLayout.setExpanded(intent.getBooleanExtra("jumptocomment", true));
        mUserNameView.setText(mPost.getUser().getUsername());
        mTimeView.setText(beautifyTime(mPost.getDate()));
        mContentView.setText(handleLineBreaks(mPost.getContent()));
        mLocationView.setText(mPost.getAttraction().getName());
        mTopicView.setText(mPost.getTopics().get(0).getName());
        mCollectionCountView.setText(String.valueOf(mPost.getCollectionCount()));
        mCommentsCountView.setText(String.valueOf(mPost.getCommentCount()));
        mThumbupCountView.setText(String.valueOf(mPost.getThumbupCount()));
        mBackButton.setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet(this);
            bottomSheet.setMainLayout(R.layout.layout_detail_more);
            bottomSheet.show();
            bottomSheet.findViewById(R.id.detail_more_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
        });
        mTopicView.setOnClickListener(v -> {
            Intent topicDetailIntent = new Intent(PostDetailActivity.this, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("topic", mPost.getTopics().get(0));
            topicDetailIntent.putExtras(bundle);
            startActivity(topicDetailIntent);
        });
        mLocationView.setOnClickListener(v -> {
            Intent attractionDetailIntent = new Intent(PostDetailActivity.this, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", mPost.getAttraction());
            attractionDetailIntent.putExtras(bundle);
            startActivity(attractionDetailIntent);
        });
        mThumbupCountView.setOnClickListener(v -> {
            mThumbupCountView.toggle();
            mPost.setThumbupCount(mPost.getThumbupCount() + (mThumbupCountView.isChecked() ? 1 : -1));
            mThumbupCountView.setText(String.valueOf(mPost.getThumbupCount()));
        });
        mCommentsCountView.setOnClickListener(v -> mAppBarLayout.setExpanded(false));
        mCollectionCountView.setOnClickListener(v -> {
            mCollectionCountView.toggle();
            mPost.setCollectionCount(mPost.getCollectionCount() + (mCollectionCountView.isChecked() ? 1 : -1));
            mCollectionCountView.setText(String.valueOf(mPost.getCollectionCount()));
        });
        mAvatarView.setOnClickListener(v -> {
            Intent avatarIntent = new Intent(PostDetailActivity.this, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mPost.getUser());
            avatarIntent.putExtras(bundle);
            startActivity(avatarIntent);
        });
        mUserNameView.setOnClickListener(v -> {
            Intent userNameIntent = new Intent(PostDetailActivity.this, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mPost.getUser());
            userNameIntent.putExtras(bundle);
            startActivity(userNameIntent);
        });
        ((View) mEditText.getParent()).getViewTreeObserver().addOnGlobalLayoutListener(new OnKeyboardChangeListener(((View) mEditText.getParent()), this));
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!mEditText.getText().toString().equals("")) {
                    mSendView.setEnabled(true);
                    mSendView.setTextColor(getColor(R.color.color_prominent));
                } else {
                    mSendView.setEnabled(false);
                    mSendView.setTextColor(getColor(R.color.text_color_light_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mNineGridImageView.setAdapter(new MyNineGridImageViewAdapter());
        NineGridUtil.setDataSource(mNineGridImageView, ImageUrlUtil.getViewInfos(mPost.getImageUrls()));
        Glide.with(this).load(mPost.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.post_detail_comment_tab_titles));
        mTitles.set(mTitles.size() - 1, mTitles.get(mTitles.size() - 1) + "(" + mPost.getCommentCount() + ")");
        mFragments.add(new CommentListFragment().setOnCommentClickListener((v, comment) -> {
            if (mEditText != null)
                mEditText.performClick();
            inputString = "回复 " + comment.getUser().getUsername();
        }));
        mFragments.add(new CommentListFragment());
        mAdapter = new PostDetailFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
    }

    @Override
    public void onKeyboardShow() {
        mSendView.setVisibility(View.VISIBLE);
        mThumbupCountView.setVisibility(View.GONE);
        mCollectionCountView.setVisibility(View.GONE);
        mCommentsCountView.setVisibility(View.GONE);
        if (!mEditText.getText().toString().equals("")) {
            mSendView.setEnabled(true);
            mSendView.setTextColor(getColor(R.color.color_prominent));
        } else {
            mSendView.setEnabled(false);
            mSendView.setTextColor(getColor(R.color.text_color_light_gray));
        }
        if (!Objects.equals(inputString, "") && mEditText.getText().toString().equals(""))
            mEditText.setText(inputString);
        mEditText.setCursorVisible(true);
        mEditText.setSelection(mEditText.getText().toString().length());
    }

    @Override
    public void onKeyboardHidden() {
        mSendView.setVisibility(View.GONE);
        mThumbupCountView.setVisibility(View.VISIBLE);
        mCollectionCountView.setVisibility(View.VISIBLE);
        mCommentsCountView.setVisibility(View.VISIBLE);
        if (!mEditText.getText().toString().equals(""))
            inputString = mEditText.getText().toString();
        mEditText.setText("");
        mEditText.setCursorVisible(false);
    }

    public class PostDetailFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public PostDetailFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
