/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/17 21:42:08
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheet extends BottomSheetDialog {
    protected View mainView;
    BottomSheetBehavior bottomSheetBehavior;
    private TextView titleView;
    private ImageButton leftButton;
    private ConstraintLayout titleBarLayout;
    private View dragBar;
    private Context context;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (newState == BottomSheetBehavior.STATE_SETTLING) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public BottomSheet(@NonNull Context context) {
        super(context, R.style.BottomSheetDialog);
        initView(context);
    }

    public BottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    protected BottomSheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public void setBottomSheetCallback(View sheetView) {
        if (bottomSheetBehavior == null) {
            bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
        }
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
    }

    void initView(Context context) {
        this.context = context;
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_bottom_sheet, null);
        setContentView(view);
        titleView = view.findViewById(R.id.bottom_sheet_titlebar_title);
        leftButton = view.findViewById(R.id.bottom_sheet_titlebar_left_button);
        titleBarLayout = view.findViewById(R.id.bottom_sheet_titlebar_layout);
        dragBar = view.findViewById(R.id.bottom_sheet_dragbar);
        leftButton.setOnClickListener(v -> dismiss());
        setTitleBarVisible(false);
    }

    public void setMainLayout(int mainLayoutId) {
        ConstraintLayout main_layout = findViewById(R.id.bottom_sheet_content);
        main_layout.removeAllViews();
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        main_layout.addView((mainView = inflater.inflate(mainLayoutId, null)), layoutParams);
    }

    public void setTitleBarVisible(boolean visible) {
        if (visible)
            titleBarLayout.setVisibility(View.VISIBLE);
        else
            titleBarLayout.setVisibility(View.GONE);
        setDragBarVisible(visible);
    }

    public void setDragBarVisible(boolean visible) {
        if (visible)
            dragBar.setVisibility(View.VISIBLE);
        else
            dragBar.setVisibility(View.INVISIBLE);
    }

    public void setTitle(String str) {
        setTitleBarVisible(true);
        titleView.setText(str);
    }
}
