package com.cloudchewie.client.util.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.bean.CityBean;
import com.cloudchewie.client.util.ui.SizeUtil;

import java.util.List;

public class CityListItemDecoration extends RecyclerView.ItemDecoration {
    private List<CityBean> cityBeans;
    private Context context;
    private int headHeight;
    private int lineHeight;
    private Paint paint;
    private Rect rectOver;
    private List<String> letterList;
    private OnTagChangedListener onTagChangedListener;
    private String lastTag = "";
    private boolean isHeadEnabled = true;

    public CityListItemDecoration(Context context, List<String> letterList) {
        this.context = context;
        headHeight = SizeUtil.dp2px(context, 36);
        lineHeight = SizeUtil.dp2px(context, 1);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(SizeUtil.dp2px(context, 15));
            rectOver = new Rect();
            this.letterList = letterList;
        }
    }

    /**
     * 设置Item的布局四周的间隙.
     *
     * @param outRect 确定间隙Left Top Right Bottom的数值的矩形.
     * @param view    RecyclerView的ChildView也就是每个Item的的布局.
     * @param parent  RecyclerView本身.
     * @param state   RecyclerView的各种状态.
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (cityBeans == null || cityBeans.size() == 0) return;
        if (isHeadEnabled) {
            int adapterPosition = parent.getChildAdapterPosition(view);
            CityBean cityBean = getBeanAt(adapterPosition);
            if (cityBean == null)
                return;
            int preTage = -1;
            int tage = cityBean.getTag();
            if (adapterPosition - 1 >= 0) {
                CityBean nextBean = getBeanAt(adapterPosition - 1);
                if (nextBean == null) {
                    return;
                }
                preTage = nextBean.getTag();
            }
            if (preTage != tage) {
                outRect.top = headHeight;
            } else {
                outRect.top = lineHeight;
            }
        }
    }


    /**
     * 绘制除Item内容以外的东西,这个方法是在Item的内容绘制之前执行的,
     * 所以如果两个绘制区域重叠,Item的绘制区域会覆盖掉该方法绘制的区域.
     * 一般配合getItemOffsets来绘制分割线等.
     *
     * @param c      Canvas 画布
     * @param parent RecyclerView
     * @param state  RecyclerView的状态
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    /**
     * 绘制除Item内容以外的东西,这个方法是在Item的内容绘制之后才执行的,
     * 所以该方法绘制的东西会将Item的内容覆盖住,既显示在Item之上.
     * 一般配合getItemOffsets来绘制分组的头部等.
     *
     * @param c      Canvas 画布
     * @param parent RecyclerView
     * @param state  RecyclerView的状态
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (cityBeans == null || cityBeans.size() == 0)
            return;
        if (isHeadEnabled) {
            int parentLeft = parent.getPaddingLeft();
            int parentRight = parent.getWidth() - parent.getPaddingRight();
            int childCount = parent.getChildCount();
            int tag = -1;
            int preTag;
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                if (childView == null)
                    continue;
                int adapterPosition = parent.getChildAdapterPosition(childView);
                int top = childView.getTop();
                int bottom = childView.getBottom();
                preTag = tag;
                if (adapterPosition >= cityBeans.size()) {
                    break;
                }
                tag = cityBeans.get(adapterPosition).getTag();
                if (preTag == tag)
                    continue;
                String name = letterList.get(Math.max((tag - 1), 0));
                int height = Math.max(top, headHeight);
                if (adapterPosition + 1 < cityBeans.size()) {
                    int nextTag = cityBeans.get(adapterPosition + 1).getTag();
                    if (tag != nextTag) {
                        height = bottom;
                    }
                }
                paint.setColor(context.getResources().getColor(R.color.content_background));
                c.drawRect(parentLeft, height - headHeight, parentRight, height, paint);
                paint.setColor(context.getResources().getColor(R.color.color_accent));
                paint.getTextBounds(name, 0, name.length(), rectOver);
                paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                c.drawText(name, SizeUtil.dp2px(context, 10), height - (headHeight - rectOver.height()) / 2.0F, paint);
                if (!lastTag.equals(name) && onTagChangedListener != null && top < headHeight) {
                    onTagChangedListener.onTagChanged(name);
                    lastTag = name;
                }
            }
        }
    }

    public void setOnTagChangedListener(OnTagChangedListener onTagChangedListener) {
        this.onTagChangedListener = onTagChangedListener;
    }

    @Nullable
    private CityBean getBeanAt(int position) {
        if (position < cityBeans.size())
            return cityBeans.get(position);
        return null;
    }

    public void setCityBeans(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    public void setLastTag(String lastTag) {
        this.lastTag = lastTag;
    }

    public void setHeadEnabled(boolean headEnabled) {
        isHeadEnabled = headEnabled;
    }

    public interface OnTagChangedListener {
        void onTagChanged(String name);
    }
}

