package com.cloudchewie.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheet extends BottomSheetDialog {
    private Context context;

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

    void initView(Context context) {
        this.context = context;
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_bottom_sheet, null);
        setContentView(view);
    }

    public void setMainLayout(int mainLayoutId) {
        ConstraintLayout main_layout = findViewById(R.id.bottom_sheet_content);
        assert main_layout != null;
        main_layout.removeAllViews();
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        main_layout.addView(inflater.inflate(mainLayoutId, null), layoutParams);
    }
}
