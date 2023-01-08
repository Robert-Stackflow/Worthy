package com.cloudchewie.client.activity.map;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.CityListAdapter;
import com.cloudchewie.client.bean.CityBean;
import com.cloudchewie.client.bean.CityList;
import com.cloudchewie.client.fragment.nav.MapFragment;
import com.cloudchewie.client.util.decoration.CityListItemDecoration;
import com.cloudchewie.client.util.enumeration.SharedPreferenceCode;
import com.cloudchewie.client.util.listener.AppBarStateChangeListener;
import com.cloudchewie.client.util.system.AssetsUtil;
import com.cloudchewie.client.util.system.SharedPreferenceUtil;
import com.cloudchewie.client.util.ui.SizeUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.FlowTagLayout;
import com.cloudchewie.ui.QuickLocationBar;
import com.cloudchewie.ui.TagItem;
import com.cloudchewie.ui.search.SearchLayout;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectCityActivity extends BaseActivity implements View.OnClickListener {
    RecyclerView cityRecyclerView;
    QuickLocationBar locationBar;
    FlowTagLayout hotCityLayout;
    FlowTagLayout historyLayout;
    TextView historyLabel;
    TextView clearHistory;
    CityListAdapter adapter;
    AppBarLayout appBarLayout;
    CityListItemDecoration headDecoration;
    ConstraintLayout headLayout;
    List<String> letterList;
    List<CityBean> cityBeans;
    LinearLayoutManager layoutManager;
    String currentCity;
    TextView currentCityTextView;
    SearchLayout searchLayout;
    View topDivider;
    boolean isMoveOut;
    int index;
    int maxHistoryCount = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_select_city);
        cityRecyclerView = findViewById(R.id.activity_select_city_recyclerview);
        locationBar = findViewById(R.id.activity_select_city_location_bar);
        hotCityLayout = findViewById(R.id.activity_select_city_hot_city_layout);
        headLayout = findViewById(R.id.activity_select_city_head);
        appBarLayout = findViewById(R.id.activity_select_city_appbar);
        searchLayout = findViewById(R.id.activity_select_city_search_layout);
        historyLayout = findViewById(R.id.activity_select_city_history_layout);
        historyLabel = findViewById(R.id.activity_select_city_history_label);
        clearHistory = findViewById(R.id.activity_select_city_history_clear);
        currentCityTextView = findViewById(R.id.activity_select_city_current_text);
        topDivider = findViewById(R.id.activity_select_city_top_divider);
        findViewById(R.id.activity_select_city_back).setOnClickListener(v -> finish());
        layoutManager = new LinearLayoutManager(this);
        cityRecyclerView.setLayoutManager(layoutManager);
        loadData();
        initSearchLayout();
        initRecyclerView();
        initLocationBar();
    }

    void initSearchLayout() {
        searchLayout.setOnTextSearchListener(s -> {
            if (adapter != null) {
                if (TextUtils.isEmpty(s)) {
                    headLayout.setVisibility(View.VISIBLE);
                    headDecoration.setCityBeans(cityBeans);
                    headDecoration.setHeadEnabled(true);
                    adapter.setCityBeans(cityBeans);
                    locationBar.setVisibility(View.VISIBLE);
                } else {
                    List<CityBean> searchCityBeans = search(s);
                    headLayout.setVisibility(View.GONE);
                    headDecoration.setCityBeans(searchCityBeans);
                    headDecoration.setHeadEnabled(false);
                    adapter.setCityBeans(searchCityBeans);
                    locationBar.setVisibility(View.GONE);
                }
            }
            return null;
        }, s -> {
            if (adapter != null) {
                if (TextUtils.isEmpty(s)) {
                    headLayout.setVisibility(View.VISIBLE);
                    headDecoration.setCityBeans(cityBeans);
                    headDecoration.setHeadEnabled(true);
                    adapter.setCityBeans(cityBeans);
                    locationBar.setVisibility(View.VISIBLE);
                } else {
                    List<CityBean> searchCityBeans = search(s);
                    headLayout.setVisibility(View.GONE);
                    headDecoration.setCityBeans(searchCityBeans);
                    headDecoration.setHeadEnabled(false);
                    adapter.setCityBeans(searchCityBeans);
                    locationBar.setVisibility(View.GONE);
                }
            }
            return null;
        });
    }

    List<CityBean> search(String city) {
        city = city.toLowerCase();
        List<CityBean> result = new ArrayList<>();
        for (CityBean cityBean : cityBeans) {
            if (cityBean.getName().contains(city) || cityBean.getPinyin().contains(city))
                result.add(cityBean);
        }
        return result;
    }

    private void loadData() {
        Intent intent = getIntent();
        currentCity = intent.getStringExtra("current");
        currentCityTextView.setText(currentCity != null ? currentCity : "定位失败");
        currentCityTextView.setOnClickListener(this);
        clearHistory.setOnClickListener(this);
        JSONObject jsonObject = JSON.parseObject(AssetsUtil.getTextFileContent(this, "city.json"));
        setHotCity(jsonObject.getJSONObject("data").getJSONArray("hot_city").toJavaList(String.class));
        setHistory(SharedPreferenceUtil.getStringArray(SharedPreferenceCode.CITY_HISTORY.getKey(), this));
        List<CityList> cityLists = jsonObject.getJSONObject("data").getJSONArray("city").toJavaList(CityList.class);
        cityBeans = new ArrayList<>();
        letterList = new ArrayList<>();
        int tagIndex = 1;
        if (cityLists != null) {
            for (CityList list : cityLists) {
                if (list != null && list.getCitylist().size() != 0) {
                    letterList.add(list.getLetter());
                    for (String city : list.getCitylist())
                        cityBeans.add(new CityBean(tagIndex, city));
                    tagIndex++;
                }
            }
        }
    }

    void initRecyclerView() {
        headDecoration = new CityListItemDecoration(this, letterList);
        headDecoration.setCityBeans(cityBeans);
        headDecoration.setOnTagChangedListener(name -> locationBar.setSelectCharacter(name));
        cityRecyclerView.addItemDecoration(headDecoration);
        locationBar.setCharacters(letterList, true);
        adapter = new CityListAdapter(this, cityBeans);
        adapter.setListener(position -> {
            CityListAdapter.MyViewHolder viewHolder = (CityListAdapter.MyViewHolder) cityRecyclerView.findViewHolderForAdapterPosition(position);
            Intent intent1 = new Intent(SelectCityActivity.this, MapFragment.class);
            intent1.putExtra("selection", viewHolder.getText());
            addHistory(viewHolder.getText());
            setResult(RESULT_OK, intent1);
            finish();
        });
        cityRecyclerView.setAdapter(adapter);
        cityRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMoveOut) {
                    isMoveOut = false;
                    int n = index - layoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < cityRecyclerView.getChildCount()) {
                        cityRecyclerView.scrollBy(0, cityRecyclerView.getChildAt(n).getTop() - SizeUtil.dp2px(SelectCityActivity.this, 36));
                    }
                }
            }
        });
    }

    void setHistory(List<String> historyList) {
        if (historyList == null || historyList.size() == 0) {
            historyLabel.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            clearHistory.setVisibility(View.GONE);
        } else {
            historyLabel.setVisibility(View.VISIBLE);
            historyLayout.setVisibility(View.VISIBLE);
            clearHistory.setVisibility(View.VISIBLE);
            historyLayout.removeAllViews();
            for (String tag : historyList) {
                TagItem tagItem = new TagItem(this);
                tagItem.setText(tag);
                tagItem.setTextSize(14);
                tagItem.setClickable(true);
                tagItem.setFocusable(true);
                tagItem.setMinEms(5);
                tagItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tagItem.setTextColor(getColor(R.color.text_color_entry));
                tagItem.setPadding(5, 5, 5, 5);
                tagItem.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.tag_background)));
                tagItem.setBackground(AppCompatResources.getDrawable(this, R.drawable.shape_round_dp5));
                tagItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tagItem.setOnClickListener(this);
                historyLayout.addView(tagItem);
            }
        }
    }

    void setHotCity(List<String> hotCityList) {
        for (String tag : hotCityList) {
            TagItem tagItem = new TagItem(this);
            tagItem.setText(tag);
            tagItem.setTextSize(14);
            tagItem.setClickable(true);
            tagItem.setFocusable(true);
            tagItem.setMinEms(5);
            tagItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tagItem.setTextColor(getColor(R.color.text_color_entry));
            tagItem.setPadding(5, 5, 5, 5);
            tagItem.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.tag_background)));
            tagItem.setBackground(AppCompatResources.getDrawable(this, R.drawable.shape_round_dp5));
            tagItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tagItem.setOnClickListener(this);
            hotCityLayout.addView(tagItem);
        }
    }

    void addHistory(String history) {
        List<String> histories = SharedPreferenceUtil.getStringArray(SharedPreferenceCode.CITY_HISTORY.getKey(), this);
        if (histories == null) histories = new ArrayList<>();
        if (!histories.contains(history)) histories.add(0, history);
        else {
            histories.remove(history);
            histories.add(0, history);
        }
        List<String> newHistories = new ArrayList<>();
        for (int i = 0; i < maxHistoryCount && i < histories.size(); i++)
            newHistories.add(histories.get(i));
        SharedPreferenceUtil.putStringArray(SharedPreferenceCode.CITY_HISTORY.getKey(), newHistories, this);
        setHistory(newHistories);
    }

    public void initLocationBar() {
        locationBar.setOnTouchLitterChangedListener(s -> {
            if (Objects.equals(s, QuickLocationBar.HOT_LABEL)) {
                appBarLayout.setExpanded(true);
                moveToPosition(0);
                if (headDecoration != null) headDecoration.setLastTag(QuickLocationBar.HOT_LABEL);
            } else {
                appBarLayout.setExpanded(false);
                int toPosition = 0;
                for (String str : letterList) {
                    if (Objects.equals(str, s)) {
                        toPosition = letterList.indexOf(str) + 1;
                        break;
                    }
                }
                for (CityBean cityBean : adapter.getCityListBeans()) {
                    if (cityBean.getTag() == toPosition) {
                        toPosition = adapter.getCityListBeans().indexOf(cityBean);
                        break;
                    }
                }
                moveToPosition(toPosition);
                if (headDecoration != null) headDecoration.setLastTag(s);
            }
        });
        locationBar.setTextDialog(findViewById(R.id.activity_select_city_dialog));
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    if (locationBar != null)
                        locationBar.setSelectCharacter(QuickLocationBar.HOT_LABEL);
                    if (headDecoration != null)
                        headDecoration.setLastTag(QuickLocationBar.HOT_LABEL);
                    topDivider.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    topDivider.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void moveToPosition(int position) {
        index = position;
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (position <= firstVisibleItemPosition) {
            cityRecyclerView.scrollToPosition(position);
        } else if (position <= lastVisibleItemPosition) {
            cityRecyclerView.scrollBy(0, cityRecyclerView.getChildAt(position - firstVisibleItemPosition).getTop() - SizeUtil.dp2px(this, 36));
        } else {
            cityRecyclerView.scrollToPosition(position);
            isMoveOut = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == currentCityTextView) {
            Intent intent = new Intent(this, MapFragment.class);
            addHistory(currentCity);
            intent.putExtra("selection", currentCity);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v instanceof TagItem) {
            Intent intent = new Intent(this, MapFragment.class);
            intent.putExtra("selection", ((TagItem) v).getText());
            setResult(RESULT_OK, intent);
            addHistory(((TagItem) v).getText());
            finish();
        } else if (v == clearHistory) {
            SharedPreferenceUtil.remove(SharedPreferenceCode.CITY_HISTORY.getKey(), this);
            setHistory(null);
        }
    }
}
