/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.AttractionDetailActivity;
import com.cloudchewie.client.activity.discover.PostDetailActivity;
import com.cloudchewie.client.activity.discover.TopicDetailActivity;
import com.cloudchewie.client.domin.Attraction;
import com.cloudchewie.client.domin.Post;
import com.cloudchewie.client.domin.Topic;
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.util.basic.DateUtil;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.IconTextItem;
import com.previewlibrary.GPreviewBuilder;

import java.util.List;
import java.util.Objects;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {
    private List<Post> posts;
    private Context context;

    public PostListAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_post_item, parent, false);
        return new PostListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.MyViewHolder holder, int position) {
        if (null == posts) {
            return;
        }
        if (position < 0 || position >= posts.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Post post = posts.get(position);
        if (null == post) {
            return;
        }
        holder.mainView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", post);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.nameView.setText(post.getUsername());
        holder.timeView.setText(DateUtil.beautifyTime(post.getDate()));
        holder.contentView.setText(handleLineBreaks(post.getContent()));
        holder.location.setText(post.getLocation());
        holder.topic.setText(post.getTag());
        holder.thumbup.setText(String.valueOf(post.getThumbupCount()));
        holder.comment.setText(String.valueOf(post.getCommentCount()));
        holder.topic.setOnClickListener(v -> {
            if (!(context instanceof TopicDetailActivity && Objects.equals(((TopicDetailActivity) context).getTopic(), holder.topic.getText()))) {
                Intent intent = new Intent(context, TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                Topic topic = new Topic(holder.topic.getText(), "这里大家可以尽情分享我们的生活", (int) (Math.random() * 100000), (int) (Math.random() * 1000));
                bundle.putSerializable("topic", topic);
                intent.putExtras(bundle);
                context.startActivity(intent);
            } else {

            }
        });
        holder.location.setOnClickListener(v -> {
            Attraction attraction = new Attraction(holder.location.getText(), "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, Math.random() % 180, Math.random() % 180, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
            Intent intent = new Intent(context, AttractionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("attraction", attraction);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.thumbup.setOnClickListener(v -> {
            holder.thumbup.toggle();
            post.setThumbupCount(post.getThumbupCount() + (holder.thumbup.isChecked() ? 1 : -1));
            holder.thumbup.setText(String.valueOf(post.getThumbupCount()));
        });
        NineGridUtil.setDataSource(holder.nineGridImageViewer, ImageUrlUtil.getViewInfos(post.getImageUrls()));
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mainView;
        public TextView nameView;
        public TextView timeView;
        public TextView contentView;
        public IconTextItem location;
        public IconTextItem topic;
        public IconTextItem comment;
        public IconTextItem thumbup;
        public NineGridImageView<UserViewInfo> nineGridImageViewer;
        private MyNineGridImageViewAdapter mAdapter;
        private GPreviewBuilder.IndicatorType mIndicatorType;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
            nameView = view.findViewById(R.id.post_item_username);
            timeView = view.findViewById(R.id.post_item_time);
            contentView = view.findViewById(R.id.post_item_content);
            location = view.findViewById(R.id.post_item_attraction);
            topic = view.findViewById(R.id.post_item_topic);
            comment = view.findViewById(R.id.post_item_comment);
            thumbup = view.findViewById(R.id.post_item_thumbup);
            nineGridImageViewer = view.findViewById(R.id.post_item_nine_grid);
            mAdapter = new MyNineGridImageViewAdapter();
            nineGridImageViewer.setAdapter(mAdapter);
        }
    }
}
