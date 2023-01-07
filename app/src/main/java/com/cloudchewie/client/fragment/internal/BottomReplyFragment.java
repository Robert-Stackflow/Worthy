package com.cloudchewie.client.fragment.internal;

import static com.cloudchewie.client.util.ui.MatricsUtil.getScreenHeight;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.CommentListAdapter;
import com.cloudchewie.client.entity.Comment;
import com.cloudchewie.ui.BottomSheet;
import com.cloudchewie.ui.util.MatricsUtil;
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
        init(context);
    }

    public BottomReplyFragment(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected BottomReplyFragment(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight = getScreenHeight(getContext());
        if (screenHeight == 0)
            screenHeight = 1920;
        Window window = getWindow();
        //设置成沉浸式
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (dialogHeight - reduceHeight));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 3 * 2);
        window.setGravity(Gravity.BOTTOM);
        getBehavior().setPeekHeight(3000);

    }

    void init(Context context) {
        setMainLayout(R.layout.fragment_comments);
//        getBehavior().setDraggable(false);
        getBehavior().setPeekHeight(3000);
        setPeekHeight(MatricsUtil.getDisplayHeight(context));
        initRecyclerView();
        initSwipeRefresh();
        setDragBarVisible(false);
        if (mainView != null)
            mainView.setMinimumHeight(3000);
    }

    void initRecyclerView() {
        setTitle("回复详情");
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
        swipeRefreshLayout.setEnableRefresh(false);
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
