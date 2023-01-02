package com.cloudchewie.client.activity.create;

import static com.cloudchewie.client.util.basic.DateUtil.beautifyTime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
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
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.fragment.global.StateFragment;
import com.cloudchewie.client.util.listener.AppBarStateChangeListener;
import com.cloudchewie.client.util.ui.DarkModeUtil;
import com.cloudchewie.client.util.ui.RichEditorUtil;
import com.cloudchewie.client.util.webview.ImageJsInterface;
import com.cloudchewie.client.util.webview.MyWebViewClient;
import com.cloudchewie.ui.EntryItem;
import com.cloudchewie.ui.IconTextItem;
import com.cloudchewie.ui.InputItem;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticlePreviewActivity extends BaseActivity {
    private Post mPost;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private String inputString;
    //基础控件
    private TextView mTitleView;
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private TextView mUserNameView;
    private TextView mTimeView;
    private WebView mContentView;
    private IconTextItem mTopicView;
    private IconTextItem mLocationView;
    private EntryItem mCommentsCountView;
    private EntryItem mThumbupCountView;
    private EntryItem mCollectionCountView;
    private CircleImageView mAvatarView;
    private InputItem mInputItem;
    private EditText mEditText;
    private TextView mSendView;
    private TextView mPageTitleView;
    private TextView mSmallTitleView;
    private LinearLayout mBottomInputLayout;
    private Button mSortButton;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ArticlePreviewFragmentAdapter mAdapter;
    private AppBarLayout mAppBarLayout;
    private RefreshLayout swipeRefreshLayout;
    private ClassicsHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_preview);
        mBackButton = findViewById(R.id.activity_article_preview_back);
        mMoreButton = findViewById(R.id.activity_article_preview_more);
        mAvatarView = findViewById(R.id.activity_article_preview_avatar);
        mViewPager = findViewById(R.id.activity_article_preview_content_viewpager);
        mTabLayout = findViewById(R.id.activity_article_preview_comment_tab_layout);
        mUserNameView = findViewById(R.id.activity_article_preview_username);
        mTimeView = findViewById(R.id.activity_article_preview_time);
        mPageTitleView = findViewById(R.id.activity_article_preview_page_title);
        mSmallTitleView = findViewById(R.id.activity_article_preview_small_title);
        mTitleView = findViewById(R.id.activity_article_preview_title);
        mContentView = findViewById(R.id.activity_article_preview_content);
        mTopicView = findViewById(R.id.activity_article_preview_topic);
        mLocationView = findViewById(R.id.activity_article_preview_location);
        mCommentsCountView = findViewById(R.id.activity_article_preview_comment_count);
        mThumbupCountView = findViewById(R.id.activity_article_preview_thumbup_count);
        mCollectionCountView = findViewById(R.id.activity_article_preview_collection_count);
        mTabLayout = findViewById(R.id.activity_article_preview_comment_tab_layout);
        mViewPager = findViewById(R.id.activity_article_preview_content_viewpager);
        mAppBarLayout = findViewById(R.id.activity_article_preview_appbar);
        mInputItem = findViewById(R.id.activity_article_preview_input_comment);
        mEditText = mInputItem.getEditText();
        mSendView = findViewById(R.id.activity_article_preview_send);
        mBottomInputLayout = findViewById(R.id.activity_article_preview_statistics_layout);
        swipeRefreshLayout = findViewById(R.id.activity_article_preview_swipe_refresh);
        header = findViewById(R.id.activity_article_preview_swipe_refresh_header);
        mSortButton = findViewById(R.id.activity_article_preview_sort);
        initSwipeRefresh();
        initView();
        initViewPager();
        initWebView();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        header.setEnableLastTime(false);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        Intent intent = this.getIntent();
        mPost = (Post) intent.getSerializableExtra("post");
        mAppBarLayout.setExpanded(intent.getBooleanExtra("jumptocomment", true));
        mUserNameView.setText(mPost.getUser().getUsername());
        mTimeView.setText("文章发表于" + beautifyTime(mPost.getDate()));
        mLocationView.setText(mPost.getAttraction().getName());
        mTopicView.setText(mPost.getTopics().get(0).getName());
        mTitleView.setText(mPost.getTitle());
        mSmallTitleView.setText(mPost.getTitle());
        mCollectionCountView.setText(String.valueOf(mPost.getCollectionCount()));
        mCommentsCountView.setText(String.valueOf(mPost.getCommentCount()));
        mThumbupCountView.setText(String.valueOf(mPost.getThumbupCount()));
        mBackButton.setOnClickListener(v -> finish());
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
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.COLLAPSED) {
                    mSmallTitleView.setVisibility(View.VISIBLE);
                    mPageTitleView.setVisibility(View.GONE);
                } else {
                    mSmallTitleView.setVisibility(View.GONE);
                    mPageTitleView.setVisibility(View.VISIBLE);
                }
            }
        });
        Glide.with(this).load(mPost.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mContentView.setWebChromeClient(new WebChromeClient());
        WebSettings settings = mContentView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        mContentView.setVerticalScrollBarEnabled(false);
        mContentView.setHorizontalScrollBarEnabled(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mContentView.setWebViewClient(new MyWebViewClient());
        mContentView.setWebChromeClient(new WebChromeClient());
        String data;
        if (DarkModeUtil.isDarkMode(this))
            data = "</Div><head><style>body{font-size:16px;color:#FFFFFF;background-color:#000000;}</style>" +
                    "<style>img{margin-top:0.4em;margin-bottom:0.4em}</style>" +
                    "<style>ul{ padding-left: 1em;margin-top:0em}</style>" +
                    "<style>ol{ padding-left: 1.2em;margin-top:0em}</style>" +
                    "</head>" + mPost.getContent();
        else
            data = "</Div><head><style>body{font-size:16px;color:#000000;background-color:#FFFFFF;}</style>" +
                    "<style>img{margin-top:0.4em;margin-bottom:0.4em}</style>" +
                    "<style>ul{ padding-left: 1em;margin-top:0em}</style>" +
                    "<style>ol{ padding-left: 1.2em;margin-top:0em}</style>" +
                    "</head>" + mPost.getContent();
        ArrayList<String> arrayList = RichEditorUtil.getImageUrls(data);
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (!arrayList.get(i).contains("http")) {
                    data = data.replace(arrayList.get(i), "file://" + arrayList.get(i));
                }
            }
        }
        mContentView.addJavascriptInterface(new ImageJsInterface(mContentView, this, arrayList), "imagelistener");
        mContentView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.post_detail_comment_tab_titles));
        mTitles.set(mTitles.size() - 1, mTitles.get(mTitles.size() - 1) + "(" + mPost.getCommentCount() + ")");
        mFragments.add(new StateFragment());
        mAdapter = new ArticlePreviewActivity.ArticlePreviewFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
    }

    public class ArticlePreviewFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public ArticlePreviewFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
