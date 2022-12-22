/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.SearchLayout;
import com.cloudchewie.ui.SearchList;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    SearchLayout searchLayout;
    SearchList searchList;
    TextView cancelView;
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_search);
        searchLayout = findViewById(R.id.search_layout);
        searchList = findViewById(R.id.search_list);
        cancelView = findViewById(R.id.search_cancel);
        searchInput = searchLayout.getSearchEdit();
        cancelView.setOnClickListener(this);
        searchLayout.setOnTextSearchListener(s -> null, s -> {
            searchList.doSearchContent(s);
            performSearch();
            return null;
        });
        searchList.setOnHistoryItemClickListener((s, integer) -> {
            searchInput.setText(s);
            performSearch();
            return null;
        });
    }

    @Override
    public void onClick(View view) {
        if (view == cancelView)
            finish();
    }

    public void performSearch() {
        Toast.makeText(this, "执行搜索" + searchInput.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}