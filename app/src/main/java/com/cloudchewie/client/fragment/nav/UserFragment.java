/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import static com.cloudchewie.client.util.basic.DomainUtil.getCity;
import static com.cloudchewie.client.util.basic.DomainUtil.getDate;
import static com.cloudchewie.client.util.basic.DomainUtil.getPhone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.auth.LoginActivity;
import com.cloudchewie.client.activity.create.DraftActivity;
import com.cloudchewie.client.activity.message.NoticeActivity;
import com.cloudchewie.client.activity.settings.FeedbackActivity;
import com.cloudchewie.client.activity.settings.SettingsActivity;
import com.cloudchewie.client.activity.user.FavoritesListActivity;
import com.cloudchewie.client.activity.user.FollowListActivity;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.entity.User;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.EntryItem;
import com.cloudchewie.ui.IToast;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class UserFragment extends Fragment implements View.OnClickListener {
    View mainView;
    RefreshLayout swipeRefreshLayout;
    //Profile
    EntryItem fansEntry;
    EntryItem followingEntry;
    EntryItem collectionEntry;
    EntryItem footprintEntry;
    //Creation
    EntryItem centerEntry;
    EntryItem managerEntry;
    EntryItem draftEntry;
    EntryItem hostEntry;
    //Notice
    EntryItem commentEntry;
    EntryItem thumbupEntry;
    EntryItem newFansEntry;
    EntryItem systemEntry;
    //Application
    EntryItem feedbackEntry;
    EntryItem helpEntry;
    EntryItem activityEntry;
    EntryItem giftEntry;
    EntryItem opensourceEntry;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_user, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.user_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.user_settings).setOnClickListener(this);
        mainView.findViewById(R.id.switch_daynight).setOnClickListener(this);
        mainView.findViewById(R.id.entry_home_page).setOnClickListener(this);
        fansEntry = mainView.findViewById(R.id.fragment_user_entry_profile_fans);
        followingEntry = mainView.findViewById(R.id.fragment_user_entry_profile_following);
        collectionEntry = mainView.findViewById(R.id.fragment_user_entry_profile_collection);
        footprintEntry = mainView.findViewById(R.id.fragment_user_entry_profile_footprint);
        centerEntry = mainView.findViewById(R.id.fragment_user_entry_creation_center);
        draftEntry = mainView.findViewById(R.id.fragment_user_entry_creation_draft);
        hostEntry = mainView.findViewById(R.id.fragment_user_entry_creation_host);
        managerEntry = mainView.findViewById(R.id.fragment_user_entry_creation_manager);
        commentEntry = mainView.findViewById(R.id.fragment_user_entry_message_comment);
        thumbupEntry = mainView.findViewById(R.id.fragment_user_entry_message_thumbup);
        newFansEntry = mainView.findViewById(R.id.fragment_user_entry_message_new_fans);
        systemEntry = mainView.findViewById(R.id.fragment_user_entry_message_system);
        activityEntry = mainView.findViewById(R.id.fragment_user_entry_application_activity_center);
        helpEntry = mainView.findViewById(R.id.fragment_user_entry_application_help_center);
        feedbackEntry = mainView.findViewById(R.id.fragment_user_entry_application_feedback);
        giftEntry = mainView.findViewById(R.id.fragment_user_entry_application_gift);
        opensourceEntry = mainView.findViewById(R.id.fragment_user_entry_application_opensource);
        footprintEntry.setOnClickListener(this);
        followingEntry.setOnClickListener(this);
        fansEntry.setOnClickListener(this);
        collectionEntry.setOnClickListener(this);
        centerEntry.setOnClickListener(this);
        draftEntry.setOnClickListener(this);
        hostEntry.setOnClickListener(this);
        managerEntry.setOnClickListener(this);
        commentEntry.setOnClickListener(this);
        thumbupEntry.setOnClickListener(this);
        newFansEntry.setOnClickListener(this);
        systemEntry.setOnClickListener(this);
        activityEntry.setOnClickListener(this);
        helpEntry.setOnClickListener(this);
        feedbackEntry.setOnClickListener(this);
        giftEntry.setOnClickListener(this);
        opensourceEntry.setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_username).setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_avatar).setOnClickListener(this);
        initSwipeRefresh();
        return mainView;
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_user_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.user_settings)) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.switch_daynight)) {
//            ImageButton switchDaynight = (ImageButton) view;
//            if (switchDaynight != null) {
//                DarkModeUtil.toggle(getActivity());
//            }
            IToast.makeTextBottom(getContext(), "???????????????,??????????????????????????????", Toast.LENGTH_SHORT).show();
        } else if (view == collectionEntry) {
            Intent intent = new Intent(getActivity(), FavoritesListActivity.class);
            startActivity(intent);
        } else if (view == draftEntry) {
            Intent intent = new Intent(getActivity(), DraftActivity.class);
            startActivity(intent);
        } else if (view == centerEntry) {
        } else if (view == followingEntry) {
            Intent intent = new Intent(getActivity(), FollowListActivity.class);
            startActivity(intent);
        } else if (view == footprintEntry) {
        } else if (view == mainView.findViewById(R.id.entry_home_page)) {
            Intent intent = new Intent(getActivity(), HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", new User(((int) (Math.random() * 1000)), "Ruida", getPhone(), "", User.GENDER.values()[((int) (Math.random() * 1000)) % User.GENDER.values().length], getDate(), getCity()));
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.fragment_user_username) || view == mainView.findViewById(R.id.fragment_user_avatar)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (view == commentEntry) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.COMMENT_REPLY);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        } else if (view == thumbupEntry) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.THUMBUP_COLLECT);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        } else if (view == newFansEntry) {
            Intent intent = new Intent(getContext(), NoticeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", NoticeActivity.NOTICE_TYPE.FOLLOW);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        } else if (view == feedbackEntry) {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            startActivity(intent);
        }
    }
}
