/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 22:04:26
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment;

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
import com.cloudchewie.client.adapter.NoticeAdapter;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.domin.Notice;
import com.cloudchewie.client.domin.Post;
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
    NoticeAdapter noticeAdapter;
    RecyclerView noticelistRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TODO 加载数据
            notices.add((Notice) msg.obj);
            noticeAdapter.notifyItemInserted(notices.size());
        }
    };
    Runnable getRefreshDatas = () -> {
        //TODO 获取下拉数据并停止刷新
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
        mainView = View.inflate(getContext(), R.layout.fragment_noticelist, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        notices = new ArrayList<>();
        noticelistRecyclerView = mainView.findViewById(R.id.fragment_noticelist_recyclerview);
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
        noticeAdapter = new NoticeAdapter(getActivity(), notices, type);
        noticelistRecyclerView.setAdapter(noticeAdapter);
        noticelistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_noticelist_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_noticelist_swipe_refresh_header);
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

    Comment getComment() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
    }

    Post getPost() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
    }
}