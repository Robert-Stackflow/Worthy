/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:44:18
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.internal;

import static com.cloudchewie.client.domin.Notice.NOTICE_TYPE.COLLECT;
import static com.cloudchewie.client.domin.Notice.NOTICE_TYPE.COMMENT;
import static com.cloudchewie.client.domin.Notice.NOTICE_TYPE.FOLLOW;
import static com.cloudchewie.client.domin.Notice.NOTICE_TYPE.REPLY;
import static com.cloudchewie.client.domin.Notice.NOTICE_TYPE.THUMBUP;

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
import com.cloudchewie.client.adapter.NoticeListAdapter;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.domin.Notice;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.fragment.BaseFragment;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoticeListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    Notice.NOTICE_TYPE type;
    List<Notice> notices;
    NoticeListAdapter noticeListAdapter;
    RecyclerView noticelistRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notices.add((Notice) msg.obj);
            noticeListAdapter.notifyItemInserted(notices.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            Notice notice = null;
            switch (type) {
                case COMMENT:
                    notice = new Notice(1, "奥莫斯", simpleDateFormat.parse("2022-10-22 20:00:06"), COMMENT, getPost(), getComment());
                    break;
                case REPLY:
                    notice = new Notice(1, "奥斯", simpleDateFormat.parse("2022-12-17 20:00:06"), REPLY, getPost(), getComment());
                    break;
                case THUMBUP:
                    notice = new Notice(1, "莫斯", new Date(System.currentTimeMillis()), THUMBUP, getPost(), null);
                    break;
                case COLLECT:
                    notice = new Notice(1, "奥斯莫", simpleDateFormat.parse("2022-12-18 20:00:06"), COLLECT, getPost(), null);
                    break;
                case FOLLOW:
                    notice = new Notice(1, "莫斯奥", new Date(System.currentTimeMillis() - 5000), FOLLOW, null, null);
                    break;
            }
            message.obj = notice;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (Notice.NOTICE_TYPE) getArguments().getSerializable("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_notice_list, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        notices = new ArrayList<>();
        noticelistRecyclerView = mainView.findViewById(R.id.fragment_notice_list_recyclerview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            Notice notice = null;
            switch (type) {
                case COMMENT:
                    notice = new Notice(1, "奥莫斯", simpleDateFormat.parse("2022-10-22 20:00:06"), COMMENT, getPost(), getComment());
                    break;
                case REPLY:
                    notice = new Notice(1, "奥斯", simpleDateFormat.parse("2022-12-17 20:00:06"), REPLY, getPost(), getComment());
                    break;
                case THUMBUP:
                    notice = new Notice(1, "莫斯", new Date(System.currentTimeMillis()), THUMBUP, getPost(), null);
                    break;
                case COLLECT:
                    notice = new Notice(1, "奥斯莫", simpleDateFormat.parse("2022-12-18 20:00:06"), COLLECT, getPost(), null);
                    break;
                case FOLLOW:
                    notice = new Notice(1, "莫斯奥", new Date(System.currentTimeMillis() - 5000), FOLLOW, null, null);
                    break;
            }
            notices.add(notice);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        noticeListAdapter = new NoticeListAdapter(getActivity(), notices, type);
        noticelistRecyclerView.setAdapter(noticeListAdapter);
        noticelistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_notice_list_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_notice_list_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableLoadMore(false);
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

    Comment getComment() throws ParseException {
        return DomainUtil.getComment(getContext(), 3);
    }

    Post getPost() throws ParseException {
        return DomainUtil.getPost(getContext());
    }
}