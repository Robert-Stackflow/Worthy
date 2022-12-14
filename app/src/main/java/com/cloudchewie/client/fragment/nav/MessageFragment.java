/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.ChatterListAdapter;
import com.cloudchewie.client.entity.Chatter;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MyDialog;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Chatter> chatters;
    ChatterListAdapter chatterListAdapter;
    RecyclerView messagersRecyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_message, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.message_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.message_clear_unread).setOnClickListener(this);
        initSwipeRefresh();
        initRecyclerView();
        return mainView;
    }

    void initRecyclerView() {
        chatters = new ArrayList<>();
        try {
            chatters.addAll(DomainUtil.getChatterList(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagersRecyclerView = mainView.findViewById(R.id.messagers_recyclerview);
        chatterListAdapter = new ChatterListAdapter(getActivity(), chatters);
        messagersRecyclerView.setAdapter(chatterListAdapter);
        messagersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @SuppressLint("NotifyDataSetChanged")
    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_message_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_message_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(refreshLayout -> {
            chatterListAdapter.notifyDataSetChanged();
            swipeRefreshLayout.finishRefresh();
        });
        header.setEnableLastTime(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.message_clear_unread)) {
            final MyDialog dialog = new MyDialog(getActivity());
            dialog.setMessage("???????????????????????????????").setSingle(false).setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                    IToast.makeTextTop(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                    IToast.makeTextTop(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
                }
            }).show();
        }
    }

}
