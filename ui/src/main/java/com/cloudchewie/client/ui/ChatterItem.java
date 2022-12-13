package com.cloudchewie.client.ui;

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
import androidx.constraintlayout.widget.ConstraintLayout;

public class ChatterItem extends ConstraintLayout {
    private ImageView avatar;
    private TextView name_view;
    private TextView time_view;
    private TextView tag_view;
    private TextView content_view;
    private View splitter;
    private ConstraintLayout mainLayout;

    public ChatterItem(@NonNull Context context) {
        super(context);
    }

    public ChatterItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ChatterItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public ChatterItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_chatter_item, this, true);
        mainLayout = findViewById(R.id.chatter_item_layout);
        avatar = findViewById(R.id.chatter_item_avatar);
        name_view = findViewById(R.id.chatter_item_name);
        time_view = findViewById(R.id.chatter_item_time);
        content_view = findViewById(R.id.chatter_item_content);
        tag_view = findViewById(R.id.chatter_item_tag);
        splitter = findViewById(R.id.chatter_item_splitter);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ChatterItem);
        if (attr != null) {
            String name = attr.getString(R.styleable.ChatterItem_chatter_item_name);
            int nameColor = attr.getColor(R.styleable.ChatterItem_chatter_item_name_color, getResources().getColor(R.color.color_accent));
            String tag = attr.getString(R.styleable.ChatterItem_chatter_item_tag);
            int tagColor = attr.getColor(R.styleable.ChatterItem_chatter_item_tag_color, getResources().getColor(R.color.color_accent));
            int tagBackgroundColor = attr.getColor(R.styleable.ChatterItem_chatter_item_tag_color, Color.RED);
            boolean tagVisibility = attr.getBoolean(R.styleable.ChatterItem_chatter_item_tag_visibility, false);
            String time = attr.getString(R.styleable.ChatterItem_chatter_item_time);
            int timeColor = attr.getColor(R.styleable.ChatterItem_chatter_item_time_color, getResources().getColor(R.color.text_color_light_gray));
            String content = attr.getString(R.styleable.ChatterItem_chatter_item_content);
            int contentColor = attr.getColor(R.styleable.ChatterItem_chatter_item_content_color, getResources().getColor(R.color.text_color_gray));
            boolean splitterEnable = attr.getBoolean(R.styleable.ChatterItem_chatter_item_splitter_enable, true);
            boolean topRadiusEnable = attr.getBoolean(R.styleable.ChatterItem_chatter_item_top_radius_enable, false);
            boolean bottomRadiusEnable = attr.getBoolean(R.styleable.ChatterItem_chatter_item_bottom_radius_enable, false);
            setRadiusEnbale(topRadiusEnable, bottomRadiusEnable);
            setContent(content, contentColor);
            setName(name, nameColor);
            setTag(tag, tagColor, tagBackgroundColor, tagVisibility);
            setTime(time, timeColor);
            setSplitterEnable(splitterEnable);
            attr.recycle();
        }
    }

    void setName(String value, int color) {
        name_view.setText(value);
        name_view.setTextColor(color);
    }

    void setSplitterEnable(boolean enable) {
        if (enable)
            splitter.setVisibility(VISIBLE);
        else
            splitter.setVisibility(GONE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setRadiusEnbale(boolean top, boolean bottom) {
        if (!top && !bottom)
            mainLayout.setBackground(getResources().getDrawable(R.drawable.shape_card));
        else if (top && bottom)
            mainLayout.setBackground(getResources().getDrawable(R.drawable.shape_card_round));
        else if (!top && bottom)
            mainLayout.setBackground(getResources().getDrawable(R.drawable.shape_card_bottom_radius));
        else if (top && !bottom)
            mainLayout.setBackground(getResources().getDrawable(R.drawable.shape_card_top_radius));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void setTag(String value, int color, int backgroundColor, boolean visibility) {
        tag_view.setText(value);
        tag_view.setTextColor(color);
        tag_view.setBackground(getResources().getDrawable(R.drawable.shape_chatter_tag));
        if (visibility)
            tag_view.setVisibility(VISIBLE);
        else
            tag_view.setVisibility(GONE);
    }

    void setTime(String value, int color) {
        time_view.setText(value);
        time_view.setTextColor(color);
    }

    void setContent(String value, int color) {
        content_view.setText(value);
        content_view.setTextColor(color);
    }
}
