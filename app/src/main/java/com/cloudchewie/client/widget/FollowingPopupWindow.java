/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.appcompat.content.res.AppCompatResources;

import com.cloudchewie.client.R;
import com.cloudchewie.ui.FlowTagLayout;
import com.cloudchewie.ui.TagItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class FollowingPopupWindow extends BasePopupWindow implements View.OnClickListener {
    FlowTagLayout flowTagLayout;
    private int oldOption = -1;
    private int currentOption = -1;
    private List<String> strings;
    private List<TagItem> tagItemList = new ArrayList<>();

    public FollowingPopupWindow(Context context, int option) {
        super(context);
        setContentView(R.layout.layout_follow_choices);
        flowTagLayout = findViewById(R.id.follow_choices_flowtaglayout);
        strings = Arrays.asList(getContext().getResources().getStringArray(R.array.following_tags));
        for (String tag : strings) {
            TagItem tagItem = new TagItem(getContext());
            tagItem.setText(tag);
            tagItem.setTextSize(14);
            tagItem.setClickable(true);
            tagItem.setFocusable(true);
            tagItem.setOnClickListener(this);
            tagItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tagItem.setTextColor(getContext().getColor(R.color.text_color_entry));
            tagItem.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_tag_round));
            tagItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tagItemList.add(tagItem);
            flowTagLayout.addView(tagItem);
        }
        setCurrentOption(option);
        oldOption = currentOption;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_TOP)
                .toShow();
    }

    public int getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(int option) {
        oldOption = currentOption;
        if (option > tagItemList.size())
            return;
        if (currentOption == option)
            return;
        currentOption = option;
        for (TagItem tagItem : tagItemList) {
            tagItem.setTextColor(getContext().getColor(R.color.text_color_entry));
            tagItem.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_tag_round));
        }
        tagItemList.get(option - 1).setTextColor(getContext().getColor(R.color.color_prominent));
        tagItemList.get(option - 1).setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape_tag_round_checked));
    }

    public boolean isOptionChanged() {
        return oldOption != currentOption;
    }

    @Override
    public void onClick(View view) {
        for (TagItem tagItem : tagItemList)
            if (view == tagItem)
                setCurrentOption(tagItemList.indexOf(tagItem) + 1);
        dismiss();
    }
}
