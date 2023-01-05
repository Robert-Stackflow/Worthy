package com.cloudchewie.ninegrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

public class GridImageView extends NiceImageView {

    private int moreNum = 0;
    private int maskColor = 0x88000000;
    private float textSize = 35;
    private int textColor = 0xFFFFFFFF;
    private TextPaint textPaint;
    private String msg = "";


    public GridImageView(Context context) {
        this(context, null);
    }

    public GridImageView(Context context, int radius) {
        this(context, null);
        setCornerRadius(radius);
    }

    public GridImageView(Context context, int radius, boolean leftBottom, boolean leftTop, boolean rightBottom, boolean rightTop) {
        this(context, null);
        if (leftBottom) setCornerBottomLeftRadius(radius);
        if (leftTop) setCornerTopLeftRadius(radius);
        if (rightBottom) setCornerBottomRightRadius(radius);
        if (rightTop) setCornerTopRightRadius(radius);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //转化单位
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getContext().getResources().getDisplayMetrics());
        textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (moreNum > 0) {
            canvas.drawColor(maskColor);
            float baseY = getHeight() / 2.0F - (textPaint.ascent() + textPaint.descent()) / 2;
            canvas.drawText(msg, getWidth() / 2.0F, baseY, textPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
//                setMaskColor(Color.argb(120, 0, 0, 0));
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
//                setMaskColor(0);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }

    public int getMoreNum() {
        return moreNum;
    }

    public void setMoreNum(int moreNum) {
        this.moreNum = moreNum;
        msg = "+" + moreNum;
        invalidate();
    }

//    public int getMaskColor() {
//        return maskColor;
//    }
//
//    public void setMaskColor(int maskColor) {
//        this.maskColor = maskColor;
//        invalidate();
//    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }
}