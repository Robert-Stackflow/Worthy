package com.cloudchewie.client.activity;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.widget.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

public class HomePageActivity extends AppCompatActivity {
    RefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .transparent()
                .apply();
        UltimateBarX.addStatusBarTopPadding(findViewById(R.id.home_page_toolbar));
        findViewById(R.id.home_page_back).setOnClickListener(v -> finish());
        ((Toolbar) findViewById(R.id.home_page_toolbar)).setContentInsetsRelative(0, 0);
//        ((AppBarLayout) (findViewById(R.id.home_page_appbar))).addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                if (state == State.EXPANDED) {
//                    //展开状态
//                    NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
//                    CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(nestedScrollView.getLayoutParams());
//                    layoutParams.setMargins(0, -10, 0, 0);
//                    nestedScrollView.setLayoutParams(layoutParams);
//                } else if (state == State.COLLAPSED) {
//                    //折叠状态
//                    NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
//                    CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(nestedScrollView.getLayoutParams());
//                    layoutParams.setMargins(0, 0, 0, 0);
//                    nestedScrollView.setLayoutParams(layoutParams);
//                } else {
//                    NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
//                    CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(nestedScrollView.getLayoutParams());
//                    layoutParams.setMargins(0, 0, 0, 0);
//                    nestedScrollView.setLayoutParams(layoutParams);
//                }
//            }
//        });
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
