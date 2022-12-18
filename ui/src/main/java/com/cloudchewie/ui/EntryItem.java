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
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EntryItem extends ConstraintLayout {
    private ConstraintLayout mainLayout;
    private ImageView icon;
    private TextView textView;

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

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_entry_item, this, true);
        mainLayout = findViewById(R.id.entry_item_layout);
        icon = findViewById(R.id.entry_item_icon);
        textView = findViewById(R.id.entry_item_text);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.EntryItem);
        if (attr != null) {
            int iconId = attr.getResourceId(R.styleable.EntryItem_entry_item_icon, R.drawable.ic_light_map);
            int iconColor = attr.getColor(R.styleable.EntryItem_entry_item_icon_color, getResources().getColor(R.color.icon_color, getResources().newTheme()));
            int iconSize = (int) attr.getDimension(R.styleable.EntryItem_entry_item_icon_size, 10);
            String text = attr.getString(R.styleable.EntryItem_entry_item_text);
            int textColor = attr.getColor(R.styleable.EntryItem_entry_item_text_color, getResources().getColor(R.color.text_color_fast_entry, getResources().newTheme()));
            int textSize = (int) attr.getDimension(R.styleable.EntryItem_entry_item_text_size, 11);
            int spacing = (int) attr.getDimension(R.styleable.EntryItem_entry_item_spacing, 3);
            int backgroundId = attr.getResourceId(R.styleable.EntryItem_entry_item_icon_background, R.drawable.shape_tag_round);
            boolean backgroundEnable = attr.getBoolean(R.styleable.EntryItem_entry_item_icon_background_enable, false);
            setIcon(iconId);
            setIconColor(iconColor);
            setIconSize(iconSize);
            setText(text);
            setTextColor(textColor);
            setTextSize(textSize);
            setSpacing(spacing);
            if (backgroundEnable)
                setIconBackground(backgroundId);
            attr.recycle();
        }
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
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(icon.getLayoutParams());
        layoutParams.width = size;
        layoutParams.height = size;
        layoutParams.startToStart = LayoutParams.PARENT_ID;
        layoutParams.endToEnd = LayoutParams.PARENT_ID;
        layoutParams.topToTop = LayoutParams.PARENT_ID;
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
        textView.setTextSize(size);
    }

    public void setSpacing(int spacing) {
        textView.setPadding(0, spacing, 0, 0);
    }

    public void setIconBackground(int backgroundId) {
        icon.setBackground(AppCompatResources.getDrawable(getContext(), backgroundId));
    }
}
