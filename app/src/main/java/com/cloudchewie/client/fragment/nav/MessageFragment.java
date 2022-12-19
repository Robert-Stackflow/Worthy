/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.cloudchewie.client.activity.message.NoticeActivity;
import com.cloudchewie.client.adapter.ChatterListAdapter;
import com.cloudchewie.client.domin.Message;
import com.cloudchewie.client.domin.Messager;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.CustomDialog;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Messager> messagers;
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
        StatusBarUtil.setMargin(mainView.findViewById(R.id.message_titlebar), 0, StatusBarUtil.getHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.message_clear_unread).setOnClickListener(this);
        mainView.findViewById(R.id.message_entry_comment_layout).setOnClickListener(this);
        mainView.findViewById(R.id.message_entry_thumbup_layout).setOnClickListener(this);
        mainView.findViewById(R.id.message_entry_fans_layout).setOnClickListener(this);
        mainView.findViewById(R.id.message_entry_system_layout).setOnClickListener(this);
        initSwipeRefresh();
        initRecyclerView();
        return mainView;
    }

    void initRecyclerView() {
        messagers = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-10 20:12:33"), "你好，在吗"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-12 20:12:33"), "嘀嘀嘀"));
                Messager messager = new Messager("官方通知", 0, false, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2020-12-10 20:12:33"), "我是恐龙"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2020-12-12 20:12:33"), "你是谁"));
                Messager messager = new Messager("远古巨兽", 0, false, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, new Date(System.currentTimeMillis()), "你好，我是来自山东的汉子一枚"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-13 20:12:33"), "哈哈哈"));
                Messager messager = new Messager("灿烂未来", 0, true, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-11 20:12:33"), "你好，我是山风"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-10 20:12:33"), "你去过这里吗"));
                Messager messager = new Messager("山风", 0, false, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-10 20:12:33"), "你好，在吗"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-12 20:12:33"), "嘀嘀嘀"));
                Messager messager = new Messager("官方通知", 0, false, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2020-12-10 20:12:33"), "我是恐龙"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2020-12-12 20:12:33"), "你是谁"));
                Messager messager = new Messager("远古巨兽", 0, false, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, new Date(System.currentTimeMillis()), "你好，我是来自山东的汉子一枚"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-13 20:12:33"), "哈哈哈"));
                Messager messager = new Messager("灿烂未来", 0, true, messages);
                messagers.add(messager);
            }
            {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-11 20:12:33"), "你好，我是山风"));
                messages.add(new Message(1, 1, 0, 0, simpleDateFormat.parse("2022-12-10 20:12:33"), "你去过这里吗"));
                Messager messager = new Messager("山风", 0, false, messages);
                messagers.add(messager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagersRecyclerView = mainView.findViewById(R.id.messagers_recyclerview);
        chatterListAdapter = new ChatterListAdapter(getActivity(), messagers);
        messagersRecyclerView.setAdapter(chatterListAdapter);
        messagersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @SuppressLint("NotifyDataSetChanged")
    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_message_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_message_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(header);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setOnRefreshListener(refreshLayout -> {
            chatterListAdapter.notifyDataSetChanged();
            swipeRefreshLayout.finishRefresh();
        });
        swipeRefreshLayout.autoRefresh();
        header.setEnableLastTime(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.message_clear_unread)) {
            final CustomDialog dialog = new CustomDialog(getActivity());
            dialog.setMessage("是否清除所有未读消息?").setSingle(false).setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "清除所有未读消息", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "取消清除未读消息", Toast.LENGTH_SHORT).show();
                }
            }).show();
        } else if (view == mainView.findViewById(R.id.message_entry_comment_layout)) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.COMMENT_REPLY);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        } else if (view == mainView.findViewById(R.id.message_entry_thumbup_layout)) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.THUMBUP_COLLECT);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        } else if (view == mainView.findViewById(R.id.message_entry_fans_layout)) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.FOLLOW);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        }
    }

}
