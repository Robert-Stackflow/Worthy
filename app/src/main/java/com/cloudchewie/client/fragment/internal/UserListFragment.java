/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:44:18
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.internal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.UserListAdapter;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.fragment.BaseFragment;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    List<User> users;
    UserListAdapter userAdapter;
    RecyclerView userlistRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            users.add((User) msg.obj);
            userAdapter.notifyItemInserted(users.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            message.obj = new User(1, "奥斯", "123456", "17837353874", "20140378@qq.com", null, "哈哈哈，你好", 1, simpleDateFormat.parse("2003-7-26"), "12345", simpleDateFormat.parse("2022-03-02"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_user_list, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        users = new ArrayList<>();
        userlistRecyclerView = mainView.findViewById(R.id.fragment_user_list_recyclerview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            User user = new User(1, "奥斯", "123456", "17837353874", "20140378@qq.com", null, "哈哈哈，你好", 1, simpleDateFormat.parse("2003-7-26"), "12345", simpleDateFormat.parse("2022-03-02"));
            users.add(user);
            user = new User(2, "茄诺", "123456", "17837353874", "20140378@qq.com", null, "哈哈哈，你好", 1, simpleDateFormat.parse("2003-7-26"), "12345", simpleDateFormat.parse("2022-03-02"));
            users.add(user);
            user = new User(3, "二蛋", "123456", "17837353874", "20140378@qq.com", null, "哈哈哈，你好", 1, simpleDateFormat.parse("2003-7-26"), "12345", simpleDateFormat.parse("2022-03-02"));
            users.add(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userAdapter = new UserListAdapter(getActivity(), users);
        userlistRecyclerView.setAdapter(userAdapter);
        userlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_user_list_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_user_list_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
    }

    @Override
    public void performRefresh() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }
}