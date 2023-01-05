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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class SettingItem extends ConstraintLayout {
    private ImageView left_icon;
    private ImageView right_icon;
    private TextView title_view;
    private TextView tip_view;
    private ConstraintLayout mainLayout;

    public SettingItem(@NonNull Context context) {
        super(context);
    }

    public SettingItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SettingItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public SettingItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_setting_item, this, true);
        mainLayout = findViewById(R.id.setting_item_layout);
        left_icon = findViewById(R.id.setting_item_left_icon);
        right_icon = findViewById(R.id.setting_item_right_icon);
        title_view = findViewById(R.id.setting_item_title);
        tip_view = findViewById(R.id.setting_item_tip);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);
        if (attr != null) {
            int titleBarBackground = attr.getResourceId(R.styleable.SettingItem_setting_item_background, Color.TRANSPARENT);
            setBackgroundResource(titleBarBackground);
            boolean leftButtonVisible = attr.getBoolean(R.styleable.SettingItem_setting_item_left_icon_visibility, true);
            int leftButtonIconId = attr.getResourceId(R.styleable.SettingItem_setting_item_left_icon, R.drawable.ic_light_settings);
            int leftButtonBackgroundColor = attr.getColor(R.styleable.SettingItem_setting_item_left_icon_background, Color.TRANSPARENT);
            setLeftButton(leftButtonVisible, leftButtonIconId, leftButtonBackgroundColor);
            boolean rightButtonVisible = attr.getBoolean(R.styleable.SettingItem_setting_item_right_icon_visibility, true);
            int rightButtonIconId = attr.getResourceId(R.styleable.SettingItem_setting_item_right_icon, R.drawable.ic_light_arrow_right);
            int rightButtonBackgroundColor = attr.getColor(R.styleable.SettingItem_setting_item_right_icon_background, Color.TRANSPARENT);
            setRightButton(rightButtonVisible, rightButtonIconId, rightButtonBackgroundColor);
            String title = attr.getString(R.styleable.SettingItem_setting_item_title);
            int titleColor = attr.getColor(R.styleable.SettingItem_setting_item_title_color, getResources().getColor(R.color.color_accent, getResources().newTheme()));
            setTitle(title, titleColor);
            String tip = attr.getString(R.styleable.SettingItem_setting_item_tip);
            int tipColor = attr.getColor(R.styleable.SettingItem_setting_item_tip_color, getResources().getColor(R.color.color_accent, getResources().newTheme()));
            setTip(tip, tipColor);
            boolean topRadiusEnable = attr.getBoolean(R.styleable.SettingItem_setting_item_top_radius_enable, false);
            boolean bottomRadiusEnable = attr.getBoolean(R.styleable.SettingItem_setting_item_bottom_radius_enable, false);
            setRadiusEnbale(topRadiusEnable, bottomRadiusEnable);
            boolean isSimpleMode = attr.getBoolean(R.styleable.SettingItem_setting_item_simple_mode, false);
            setSimpleMode(isSimpleMode);
            attr.recycle();
        }
    }

    void setSimpleMode(boolean simpleMode) {
        if (simpleMode) {
            left_icon.setVisibility(GONE);
            right_icon.setVisibility(GONE);
            tip_view.setVisibility(GONE);
            ConstraintSet set = new ConstraintSet();
            set.clone(mainLayout);
            set.constrainWidth(title_view.getId(), LayoutParams.MATCH_PARENT);
            set.centerHorizontally(title_view.getId(), ConstraintSet.PARENT_ID);
            set.applyTo(mainLayout);
            title_view.setTextSize(17);
        }
    }

    private void setLeftButton(boolean visibility, int iconId, int backgroundColor) {
        if (visibility)
            left_icon.setVisibility(View.VISIBLE);
        else
            left_icon.setVisibility(View.GONE);
        left_icon.setImageResource(iconId);
        left_icon.setBackgroundColor(backgroundColor);
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

    private void setRightButton(boolean visibility, int iconId, int backgroundColor) {
        if (visibility)
            right_icon.setVisibility(View.VISIBLE);
        else
            right_icon.setVisibility(View.GONE);
        right_icon.setImageResource(iconId);
        right_icon.setBackgroundColor(backgroundColor);
    }

    private void setTitle(String title, int titleColor) {
        title_view.setText(title);
        title_view.setTextColor(titleColor);
    }

    private void setTip(String tip, int tipColor) {
        tip_view.setText(tip);
        tip_view.setTextColor(tipColor);
    }
}
