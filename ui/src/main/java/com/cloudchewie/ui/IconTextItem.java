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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

public class IconTextItem extends ConstraintLayout {
    private ConstraintLayout mainLayout;
    private ImageView icon;
    private TextView textView;

    public IconTextItem(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public IconTextItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconTextItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public IconTextItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_icon_text_item, this, true);
        mainLayout = findViewById(R.id.icon_text_item_layout);
        icon = findViewById(R.id.icon_text_item_icon);
        textView = findViewById(R.id.icon_text_item_text);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.IconTextItem);
        if (attr != null) {
            int iconId = attr.getResourceId(R.styleable.IconTextItem_icon_text_item_icon, R.drawable.ic_light_map);
            int iconColor = attr.getColor(R.styleable.IconTextItem_icon_text_item_icon_color, getResources().getColor(R.color.color_prominent, getResources().newTheme()));
            int iconSize = (int) attr.getDimension(R.styleable.IconTextItem_icon_text_item_icon_size, 10);
            String text = attr.getString(R.styleable.IconTextItem_icon_text_item_text);
            int textColor = attr.getColor(R.styleable.IconTextItem_icon_text_item_text_color, getResources().getColor(R.color.color_prominent, getResources().newTheme()));
            int textSize = (int) attr.getDimension(R.styleable.IconTextItem_icon_text_item_text_size, 11);
            int spacing = (int) attr.getDimension(R.styleable.IconTextItem_icon_text_item_spacing, 3);
            setIcon(iconId);
            setIconColor(iconColor);
            setIconSize(iconSize);
            setText(text);
            setTextColor(textColor);
            setTextSize(textSize);
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
        ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(size, size);
        layoutParams.topToTop = LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID;
        layoutParams.startToStart = LayoutParams.PARENT_ID;
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
        textView.setPadding(spacing, 0, 0, 0);
    }
}
