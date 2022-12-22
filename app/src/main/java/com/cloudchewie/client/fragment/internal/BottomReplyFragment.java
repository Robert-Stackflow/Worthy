package com.cloudchewie.client.fragment.internal;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.CommentListAdapter;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.ui.BottomSheet;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomReplyFragment extends BottomSheet implements View.OnClickListener {
    List<Comment> comments;
    CommentListAdapter commentListAdapter;
    RecyclerView commentsRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;
    ClassicsFooter footer;

    public BottomReplyFragment(@NonNull Context context, List<Comment> comments) {
        super(context);
        this.comments = comments;
        setMainLayout(R.layout.fragment_comments);
        initRecyclerView();
        initSwipeRefresh();
    }

    public BottomReplyFragment(@NonNull Context context, int theme) {
        super(context, theme);
        setMainLayout(R.layout.fragment_comments);
        initRecyclerView();
        initSwipeRefresh();
    }

    protected BottomReplyFragment(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setMainLayout(R.layout.fragment_comments);
        initRecyclerView();
        initSwipeRefresh();
    }

    void initRecyclerView() {
        setTitle("回复详情");
//        setBottomSheetCallback((View) mainView.getParent());
        getBehavior().setDraggable(false);
        if (comments == null)
            comments = new ArrayList<>();
        commentsRecyclerView = mainView.findViewById(R.id.fragment_comments_recyclerview);
        commentListAdapter = new CommentListAdapter(getContext(), comments);
        commentsRecyclerView.setAdapter(commentListAdapter);
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
        swipeRefreshLayout.setNoMoreData(true);
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
