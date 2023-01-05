/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/17 22:05:40
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EntryItem extends ConstraintLayout {
    private ConstraintLayout mainLayout;
    private LinearLayout topLayout;
    private ImageButton icon;
    private TextView textView;
    private TextView bigTextView;
    private boolean isChecked;
    private int mode;
    private int iconId;
    private int iconColor;
    private int checkedIconId;
    private int checkedIconColor;

    public EntryItem(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public EntryItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EntryItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EntryItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == 0) {
            bigTextView.setVisibility(GONE);
            icon.setVisibility(VISIBLE);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(topLayout.getLayoutParams());
            layoutParams.startToStart = LayoutParams.PARENT_ID;
            layoutParams.endToEnd = LayoutParams.PARENT_ID;
            layoutParams.topToTop = LayoutParams.PARENT_ID;
            topLayout.setLayoutParams(layoutParams);
        } else {
            bigTextView.setVisibility(VISIBLE);
            icon.setVisibility(GONE);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(topLayout.getLayoutParams());
            layoutParams.startToStart = LayoutParams.PARENT_ID;
            layoutParams.endToEnd = LayoutParams.PARENT_ID;
            layoutParams.topToTop = LayoutParams.PARENT_ID;
            topLayout.setLayoutParams(layoutParams);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_entry_item, this, true);
        mainLayout = findViewById(R.id.entry_item_layout);
        icon = findViewById(R.id.entry_item_icon);
        textView = findViewById(R.id.entry_item_text);
        bigTextView = findViewById(R.id.entry_item_text_big);
        topLayout = findViewById(R.id.entry_item_top_layout);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.EntryItem);
        icon.setOnClickListener(v -> this.performClick());
        if (attr != null) {
            mode = attr.getInt(R.styleable.EntryItem_entry_item_mode, 0);
            iconId = attr.getResourceId(R.styleable.EntryItem_entry_item_icon, R.drawable.ic_light_map);
            iconColor = attr.getColor(R.styleable.EntryItem_entry_item_icon_color, getResources().getColor(R.color.color_icon, getResources().newTheme()));
            checkedIconId = attr.getResourceId(R.styleable.EntryItem_entry_item_checked_icon, R.drawable.ic_light_map_fill);
            checkedIconColor = attr.getColor(R.styleable.EntryItem_entry_item_checked_icon_color, getResources().getColor(R.color.color_prominent, getResources().newTheme()));
            int iconSize = (int) attr.getDimension(R.styleable.EntryItem_entry_item_icon_size, getResources().getDimension(R.dimen.entry_item_default_icon_size));
            String text = attr.getString(R.styleable.EntryItem_entry_item_text);
            int textColor = attr.getColor(R.styleable.EntryItem_entry_item_text_color, getResources().getColor(R.color.color_gray, getResources().newTheme()));
            int textSize = (int) attr.getDimension(R.styleable.EntryItem_entry_item_text_size, getResources().getDimension(R.dimen.entry_item_default_text_size));
            String bigText = attr.getString(R.styleable.EntryItem_entry_item_big_text);
            int bigTextColor = attr.getColor(R.styleable.EntryItem_entry_item_big_text_color, getResources().getColor(R.color.color_accent, getResources().newTheme()));
            int bigTextSize = (int) attr.getDimension(R.styleable.EntryItem_entry_item_big_text_size, getResources().getDimension(R.dimen.entry_item_default_big_text_size));
            int spacing = (int) attr.getDimension(R.styleable.EntryItem_entry_item_spacing, 3);
            int backgroundId = attr.getResourceId(R.styleable.EntryItem_entry_item_icon_background, R.drawable.shape_round_dp10);
            boolean backgroundEnable = attr.getBoolean(R.styleable.EntryItem_entry_item_icon_background_enable, false);
            int iconScaleType = attr.getInt(R.styleable.EntryItem_entry_item_icon_scale_type, 0);
            setScaleType(iconScaleType);
            setMode(mode);
            setIcon(iconId);
            setIconColor(iconColor);
            setIconSize(iconSize);
            setText(text);
            setTextColor(textColor);
            setTextSize(textSize);
            setBigText(bigText);
            setBigTextColor(bigTextColor);
            setBigTextSize(bigTextSize);
            setSpacing(spacing);
            if (backgroundEnable) setIconBackground(backgroundId);
            attr.recycle();
        }
    }

    public void toggle() {
        isChecked = !isChecked;
        if (isChecked) {
            setIcon(checkedIconId);
            setIconColor(checkedIconColor);
        } else {
            setIcon(iconId);
            setIconColor(iconColor);
        }
    }

    public void setScaleType(int type) {
        switch (type) {
            case 0:
                icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case 1:
                icon.setPadding(10, 10, 10, 10);
                icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case 2:
                icon.setPadding(10, 10, 10, 10);
                icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIcon(int iconId) {
        icon.setImageResource(iconId);
    }

    public void setIcon(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setIconColor(int color) {
        icon.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setIconSize(int size) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(icon.getLayoutParams());
        layoutParams.width = size;
        layoutParams.height = size;
        icon.setLayoutParams(layoutParams);
    }

    public String getText() {
        return (String) textView.getText();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextColor(int textColor) {
        textView.setTextColor(textColor);
    }

    public void setTextSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public String getBigText() {
        return (String) bigTextView.getText();
    }

    public void setBigText(String text) {
        bigTextView.setText(text);
    }

    public void setBigTextColor(int textColor) {
        bigTextView.setTextColor(textColor);
    }

    public void setBigTextSize(int size) {
        bigTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setSpacing(int spacing) {
        textView.setPadding(0, spacing, 0, 0);
    }

    public void setIconBackground(int backgroundId) {
        icon.setBackground(AppCompatResources.getDrawable(getContext(), backgroundId));
    }
}
