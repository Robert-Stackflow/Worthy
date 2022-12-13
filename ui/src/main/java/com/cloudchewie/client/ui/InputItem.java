package com.cloudchewie.client.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class InputItem extends RelativeLayout {
    View mainView;
    private ImageView leftIcon;
    private EditText editText;
    private ImageButton rightIcon;
    private RelativeLayout mainLayout;

    public InputItem(Context context) {
        super(context);
    }

    public InputItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public InputItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public InputItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_input_item, this, true);
        mainLayout = findViewById(R.id.input_item_layout);
        leftIcon = findViewById(R.id.input_item_left_icon);
        rightIcon = findViewById(R.id.input_item_right_icon);
        editText = findViewById(R.id.input_item_edit);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.InputItem);
        if (attr != null) {
            String hint = attr.getString(R.styleable.InputItem_input_item_hint);
            String text = attr.getString(R.styleable.InputItem_input_item_text);
            int mode = attr.getInt(R.styleable.InputItem_input_item_mode, 0);
            boolean editable = attr.getBoolean(R.styleable.InputItem_input_item_editable, true);
            boolean leftIconVisibility = attr.getBoolean(R.styleable.InputItem_input_item_left_icon_visibility, false);
            int leftIconId = attr.getResourceId(R.styleable.InputItem_input_item_left_icon, R.drawable.ic_light_search);
            boolean rightIconVisibility = attr.getBoolean(R.styleable.InputItem_input_item_right_icon_visibility, false);
            int rightIconId = attr.getResourceId(R.styleable.InputItem_input_item_right_icon, R.drawable.ic_light_delete);
            setLeftButton(leftIconVisibility, leftIconId);
            setRightButton(rightIconVisibility, rightIconId);
            setEditText(hint, text, editable);
            setMode(mode);
            attr.recycle();
        }
    }

    private void setMode(int mode) {
        if (mode == 0) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (mode == 1) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (mode == 2) {
            rightIcon.setFocusable(true);
            rightIcon.setClickable(true);
            rightIcon.setSelected(false);
            setRightButton(true, R.drawable.ic_light_eyeoff);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            rightIcon.setOnClickListener(v -> {
                if (rightIcon.isSelected()) {
                    rightIcon.setSelected(false);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setRightButton(true, R.drawable.ic_light_eyeoff);
                } else {
                    rightIcon.setSelected(true);
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setRightButton(true, R.drawable.ic_light_eye);
                }
            });
        } else if (mode == 3) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (mode == 4) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
    }

    private void setEditText(String hint, String text, boolean editable) {
        editText.setText(text);
        editText.setHint(hint);
        if (!editable) {
            if (editText instanceof android.widget.EditText) {
                editText.setCursorVisible(false);
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
        }
    }

    private void setLeftButton(boolean visibility, int iconId) {
        if (visibility)
            leftIcon.setVisibility(View.VISIBLE);
        else
            leftIcon.setVisibility(View.GONE);
        leftIcon.setImageResource(iconId);
    }

    private void setRightButton(boolean visibility, int iconId) {
        if (visibility)
            rightIcon.setVisibility(View.VISIBLE);
        else
            rightIcon.setVisibility(View.GONE);
        rightIcon.setImageResource(iconId);
    }
}
