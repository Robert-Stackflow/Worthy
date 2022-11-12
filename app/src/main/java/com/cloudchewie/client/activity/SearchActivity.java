package com.cloudchewie.client.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.client.R;
import com.cloudchewie.client.util.StatusBarUtil;
import com.cloudchewie.search.SearchLayout;
import com.cloudchewie.search.SearchList;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    SearchLayout searchLayout;
    SearchList searchList;
    TextView cancelView;
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setMargin(this);
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