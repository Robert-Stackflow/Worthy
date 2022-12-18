/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 14:12:50
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
import com.cloudchewie.client.adapter.CommentAdapter;
import com.cloudchewie.client.domin.Comment;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Comment> comments;
    CommentAdapter commentAdapter;
    RecyclerView commentsRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TODO 加载数据
            comments.add((Comment) msg.obj);
            commentAdapter.notifyItemInserted(comments.size());
        }
    };
    Runnable getDatas = () -> {
        //TODO 获取数据并停止刷新
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            message.obj = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getRefreshDatas = () -> {
        //TODO 获取下拉数据并停止刷新
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            message.obj = new Comment(1, "东方不败", simpleDateFormat.parse("2022-12-13 20:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
        swipeRefreshLayout.finishRefresh();
    };
    Runnable getMorehDatas = () -> {
        //TODO 获取上拉数据并停止刷新
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        message.obj = new Comment(1, "镜", new Date(System.currentTimeMillis()), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_comments, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        comments = new ArrayList<>();
        commentsRecyclerView = mainView.findViewById(R.id.fragment_comments_recyclerview);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            Comment comment = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
            comments.add(comment);
            comment = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
            comments.add(comment);
            comment = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
            comments.add(comment);
            comment = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
            comments.add(comment);
            comment = new Comment(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100));
            comments.add(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        commentAdapter = new CommentAdapter(getActivity(), comments);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_comments_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_comments_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_comments_swipe_footer);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setRefreshFooter(footer);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
        swipeRefreshLayout.setOnLoadMoreListener(v -> handler.post(getMorehDatas));
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
        footer.setTextSizeTitle(14);
    }

    public void performRefresh() {
        swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }
}