/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/17 21:42:08
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CheckBoxItem extends ConstraintLayout {
    private CheckBox right_switch;
    private TextView title_view;
    private ConstraintLayout mainLayout;

    public CheckBoxItem(@NonNull Context context) {
        super(context);
    }

    public CheckBoxItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CheckBoxItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public CheckBoxItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_checkbox_item, this, true);
        right_switch = findViewById(R.id.checkbox_item_switch);
        title_view = findViewById(R.id.checkbox_item_title);
        mainLayout = findViewById(R.id.checkbox_item_layout);
        @SuppressLint("CustomViewStyleable")
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxItem);
        if (attr != null) {
            int titleBarBackground = attr.getResourceId(R.styleable.CheckBoxItem_checkbox_item_background, Color.TRANSPARENT);
            setBackgroundResource(titleBarBackground);
            String title = attr.getString(R.styleable.CheckBoxItem_checkbox_item_title);
            int titleColor = attr.getColor(R.styleable.CheckBoxItem_checkbox_item_title_color, getResources().getColor(R.color.color_accent, getResources().newTheme()));
            setTitle(title, titleColor);
            right_switch.setChecked(attr.getBoolean(R.styleable.CheckBoxItem_checkbox_item_checked, true));
            boolean topRadiusEnable = attr.getBoolean(R.styleable.CheckBoxItem_checkbox_item_top_radius_enable, false);
            boolean bottomRadiusEnable = attr.getBoolean(R.styleable.CheckBoxItem_checkbox_item_bottom_radius_enable, false);
            setRadiusEnbale(topRadiusEnable, bottomRadiusEnable);
            attr.recycle();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void setRadiusEnbale(boolean top, boolean bottom) {
        if (!top && !bottom)
            mainLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_rect));
        else if (top && bottom)
            mainLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_round_dp10));
        else if (!top && bottom)
            mainLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_round_bottom_dp10));
        else if (top && !bottom)
            mainLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_round_top_dp10));
    }

    private void setTitle(String title, int titleColor) {
        title_view.setText(title);
        title_view.setTextColor(titleColor);
    }
}
