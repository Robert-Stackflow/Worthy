package com.cloudchewie.client.fragment.global;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.create.CreateArticleActivity;
import com.cloudchewie.client.activity.create.CreatePostActivity;

public class CreateDialogFragment extends DialogFragment {
    View mainView;
    ImageView close;
    RelativeLayout post;
    RelativeLayout article;
    RelativeLayout draft;
    TextView attraction;
    TextView topic;
    Animation shadeIn;
    Animation shadeOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        shadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shade_in);
        shadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shade_out);
        mainView = inflater.inflate(R.layout.layout_create, null);
        close = mainView.findViewById(R.id.layout_create_close);
        post = mainView.findViewById(R.id.layout_create_post_layout);
        article = mainView.findViewById(R.id.layout_create_article_layout);
        draft = mainView.findViewById(R.id.layout_create_draft_layout);
        attraction = mainView.findViewById(R.id.layout_create_attraction);
        topic = mainView.findViewById(R.id.layout_create_topic);
        post.startAnimation(shadeIn);
        article.startAnimation(shadeIn);
        draft.startAnimation(shadeIn);
        attraction.startAnimation(shadeIn);
        topic.startAnimation(shadeIn);
        post.setOnClickListener(view -> {
            dismiss();
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            startActivity(intent);
        });
        article.setOnClickListener(view -> {
            dismiss();
            Intent intent = new Intent(getContext(), CreateArticleActivity.class);
            startActivity(intent);
        });
        close.setOnClickListener(v -> dismiss());
        mainView.setOnClickListener(v -> {
//            post.startAnimation(shadeOut);
//            article.startAnimation(shadeOut);
//            draft.startAnimation(shadeOut);
//            attraction.startAnimation(shadeOut);
//            Animation shadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_shade_out);
//            shadeOut.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    dismiss();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//            topic.startAnimation(shadeOut);
            dismiss();
        });
        return mainView;
    }
}
