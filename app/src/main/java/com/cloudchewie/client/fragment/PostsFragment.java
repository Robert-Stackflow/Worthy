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
import com.cloudchewie.client.adapter.PostAdapter;
import com.cloudchewie.client.domin.Post;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Post> posts;
    PostAdapter postAdapter;
    RecyclerView followingRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TODO 加载数据
            posts.add((Post) msg.obj);
            postAdapter.notifyItemInserted(posts.size());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_posts, null);
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    void initRecyclerView() {
        posts = new ArrayList<>();
        followingRecyclerView = mainView.findViewById(R.id.fragment_following_recyclerview);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            Post post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
            post = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
            posts.add(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        postAdapter = new PostAdapter(getActivity(), posts);
        followingRecyclerView.setAdapter(postAdapter);
        followingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_following_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_following_swipe_refresh_header);
        footer = mainView.findViewById(R.id.fragment_following_swipe_footer);
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
    }

    public void performRefresh() {
        swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }

    Runnable getDatas = () -> {
        //TODO 获取数据并停止刷新
        Message message = handler.obtainMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            message.obj = new Post(1, "灿烂未来", simpleDateFormat.parse("2022-12-15 06:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "武汉", "生活圈");
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
            message.obj = new Post(1, "东方不败", simpleDateFormat.parse("2022-12-13 20:00:00"), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "北京", "生活圈");
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
        message.obj = new Post(1, "镜", new Date(System.currentTimeMillis()), "有时相信在某个平行的宇宙\\n你的爱还一如既往陪在我左右", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), "乌鲁木齐", "你好!未来");
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };
}
