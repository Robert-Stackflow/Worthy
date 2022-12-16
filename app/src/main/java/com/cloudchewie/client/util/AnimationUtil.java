package com.cloudchewie.client.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class AnimationUtil {

    //动画持续时间
    public final static int ANIMATION_IN_TIME = 500;
    public final static int ANIMATION_OUT_TIME = 500;

    /**
     * @param isIn          动画类型，进入或消失
     * @param rootView      根布局，主要用来设置半透明背景
     * @param target        要移动的view
     * @param animInterface 动画执行完毕后的回调
     */
    public static void createAnimation(final boolean isIn, final View rootView, final View target,
                                       final AnimInterface animInterface) {
        final int toYDelta = ViewUtils.getViewMeasuredHeight(target);//测量布局高度
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isIn ? -toYDelta : 0, isIn ? 0 : -toYDelta);
        valueAnimator.setDuration(isIn ? ANIMATION_IN_TIME : ANIMATION_OUT_TIME);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                target.setY(currentValue);
                if (!isIn) {//因为在setAnimationStyle(R.style.SelectPopupWindow);设置了进入动画,所以执行进入动画时不再设置
                    rootView.setAlpha(1 - Math.abs(currentValue) / animation.getDuration());
                }
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animInterface != null) {
                    animInterface.animEnd();
                }
            }
        });
        valueAnimator.start();
    }

    public interface AnimInterface {
        void animEnd();
    }


    private static AlphaAnimation mHideAnimation = null;
    private static AlphaAnimation mShowAnimation = null;

    /**
     * View渐现动画效果
     */
    public static void setAlphaAnimation(float start,float end,View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(start,end);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }
}
