/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.AttractionsAdapter;
import com.cloudchewie.client.domin.Attraction;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AttractionsFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Attraction> attractions;
    AttractionsAdapter attractionsAdapter;
    RecyclerView attractionsRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TODO 加载数据
            attractions.add((Attraction) msg.obj);
            attractionsAdapter.notifyItemInserted(attractions.size());
        }
    };
    Runnable getDatas = () -> {
        //TODO 获取数据并停止刷新
        Message message = handler.obtainMessage();
        message.obj = new Attraction("九寨沟", "四川省九寨沟市黄果山瀑布", "风景宜人的千古名胜", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getRefreshDatas = () -> {
        //TODO 获取下拉数据并停止刷新
        Message message = handler.obtainMessage();
        message.obj = new Attraction("东湖", "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMorehDatas = () -> {
        //TODO 获取上拉数据并停止刷新
        Message message = handler.obtainMessage();
        message.obj = new Attraction("珞珈山", "湖北省武汉市武汉大学内", "赏樱之人络绎不绝，且看珞珈山下热舞", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新成功";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_attractions, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        attractions = new ArrayList<>();
        attractionsRecyclerView = mainView.findViewById(R.id.fragment_attractions_recyclerview);
        attractionsAdapter = new AttractionsAdapter(getActivity(), attractions);
        attractionsRecyclerView.setAdapter(attractionsAdapter);
        attractionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_attractions_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_attractions_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_attractions_swipe_footer);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setRefreshFooter(footer);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(refreshlayout -> handler.post(getRefreshDatas));
        swipeRefreshLayout.setOnLoadMoreListener(refreshlayout -> handler.post(getMorehDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
    }

    @Override
    public void onClick(View view) {
    }
}
