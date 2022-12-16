package com.cloudchewie.client.activity;

import static java.lang.Math.abs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.BottomSheet;
import com.cloudchewie.client.util.AnimationUtil;
import com.cloudchewie.client.widget.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import jp.wasabeef.blurry.Blurry;

public class HomePageActivity extends AppCompatActivity {
    RefreshLayout swipeRefreshLayout;
    Context context;
    boolean isBlurred = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        context = getApplicationContext();
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .transparent()
                .apply();
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
                    findViewById(R.id.home_page_small_avatar).setVisibility(View.GONE);
                    findViewById(R.id.home_page_small_username).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.home_page_background)).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.background));
                    AnimationUtil.setAlphaAnimation(0.95f, 1.0f, findViewById(R.id.home_page_background), 500);
                    isBlurred = false;
                } else if (state == State.COLLAPSED) {
                    isBlurred = true;
                    Blurry.with(context).animate(500).radius(abs(offset) % 10).async().capture(findViewById(R.id.home_page_background)).into(findViewById(R.id.home_page_background));
                    findViewById(R.id.home_page_small_avatar).setVisibility(View.VISIBLE);
                    findViewById(R.id.home_page_small_username).setVisibility(View.VISIBLE);
                } else if (state == State.UPING) {
//                    isBlurred = true;
//                    Blurry.with(context).animate(500).radius(abs(offset) % 20).async().capture(findViewById(R.id.home_page_background)).into(findViewById(R.id.home_page_background));
                } else if (state == State.DOWNING) {
                    findViewById(R.id.home_page_small_avatar).setVisibility(View.GONE);
                    findViewById(R.id.home_page_small_username).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.home_page_background)).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.background));
                    isBlurred = false;
                }
            }
        });
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
//        swipeRefreshLayout = findViewById(R.id.home_page_swipe_refresh);
//        swipeRefreshLayout.setEnableOverScrollDrag(true);
//        swipeRefreshLayout.setEnableOverScrollBounce(true);
//        swipeRefreshLayout.setEnableLoadMore(false);
//        swipeRefreshLayout.setEnablePureScrollMode(true);
    }
}
