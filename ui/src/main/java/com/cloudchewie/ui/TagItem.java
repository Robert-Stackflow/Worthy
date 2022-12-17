package com.cloudchewie.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

public class TagItem extends RelativeLayout {
    private TextView textView;
    private RelativeLayout mainLayout;

    public TagItem(Context context) {
        super(context);
        initView(context, null);
    }

    public TagItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TagItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public TagItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_tag_item, this, true);
        mainLayout = findViewById(R.id.tag_item_layout);
        textView = findViewById(R.id.tag_item_text);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.TagItem);
        String text;
        if (attr != null) {
            int backgroundId = attr.getResourceId(R.styleable.TagItem_tag_item_background, R.drawable.shape_tag_round);
            text = attr.getString(R.styleable.TagItem_tag_item_text);
            int textColor = attr.getColor(R.styleable.TagItem_tag_item_text_color, getResources().getColor(R.color.text_color_fast_entry, getResources().newTheme()));
            int textSize = (int) attr.getDimension(R.styleable.TagItem_tag_item_text_size, 14);
            setText(text);
            setTextSize(textSize);
            setTextColor(textColor);
            setBackground(backgroundId);
            attr.recycle();
        }
    }

    public void setTextSize(int size) {
        textView.setTextSize(size);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setBackground(int resId) {
        textView.setBackground(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setBackground(Drawable drawable) {
        textView.setBackground(drawable);
    }

    public String getText() {
        return (String) textView.getText();
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
