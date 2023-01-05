/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.domin.Comment;
import com.cloudchewie.client.fragment.internal.BottomReplyFragment;
import com.cloudchewie.client.util.basic.DateUtil;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.system.ClipBoardUtil;
import com.cloudchewie.client.util.widget.ReplyItem;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.IconTextItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> implements View.OnClickListener {
    private final List<Comment> comments;
    private final Context context;
    private OnCommentItemClickListener listener;

    public CommentListAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    public void setListener(OnCommentItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, parent, false);
        return new CommentListAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.MyViewHolder holder, int position) {
        if (null == comments) {
            return;
        }
        if (position < 0 || position >= comments.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Comment comment = comments.get(position);
        if (null == comment) {
            return;
        }
        holder.contentView.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(holder.mItemView, comment);
        });
        holder.reply.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(holder.mItemView, comment);
        });
        holder.contentView.setOnLongClickListener(view -> {
            ClipBoardUtil.copy(context, comment.getContent());
            IToast.makeTextBottom(context, "已复制" + comment.getUser().getUsername() + "的评论", Toast.LENGTH_SHORT).show();
            return false;
        });
        holder.nameView.setText(comment.getUser().getUsername());
        holder.timeView.setText(DateUtil.beautifyTime(comment.getDate()));
        holder.contentView.setText(handleLineBreaks(comment.getContent()));
        holder.thumbup.setText(comment.getThumbupCount() != 0 ? String.valueOf(comment.getThumbupCount()) : "");
        if (comment.getReplyCount() > 0) {
            for (int i = 0; i < comment.getReplyCount() && i < 3; i++)
                holder.repliesLayout.addView(new ReplyItem(context, comment.getReplies().get(i)), holder.repliesLayout.getChildCount() - 1);
        } else {
            holder.repliesLayout.setVisibility(View.GONE);
        }
        holder.avatarView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", comment.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", comment.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.thumbup.setOnClickListener(v -> {
            holder.thumbup.toggle();
            if (holder.thumbdown.isChecked())
                holder.thumbdown.toggle();
            comment.setThumbupCount(comment.getThumbupCount() + (holder.thumbup.isChecked() ? 1 : -1));
            holder.thumbup.setText(comment.getThumbupCount() != 0 ? String.valueOf(comment.getThumbupCount()) : "");
        });
        holder.thumbdown.setOnClickListener(v -> {
            holder.thumbdown.toggle();
            if (holder.thumbup.isChecked()) {
                holder.thumbup.toggle();
                comment.setThumbupCount(comment.getThumbupCount() + (holder.thumbup.isChecked() ? 1 : -1));
                holder.thumbup.setText(comment.getThumbupCount() != 0 ? String.valueOf(comment.getThumbupCount()) : "");
            }
            if (holder.thumbdown.isChecked())
                IToast.makeTextBottom(context, "感谢您的反馈", Toast.LENGTH_SHORT).show();
        });
        holder.share.setOnClickListener(v -> IToast.makeTextBottom(context, "功能维护中,暂时无法分享评论", Toast.LENGTH_SHORT).show());
        holder.more.setOnClickListener(v -> IToast.makeTextBottom(context, "功能维护中,暂时无法进行操作", Toast.LENGTH_SHORT).show());
        holder.expandView.setText("共" + comment.getReplyCount() + "条回复 > ");
        holder.expandView.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_round_dp2));
        if (comment.getReplyCount() > 0) {
            holder.reply.setOnClickListener(v -> {
                BottomReplyFragment bottomReplyFragment = new BottomReplyFragment(context, comment.getReplies());
                bottomReplyFragment.show();
            });
            holder.expandView.setOnClickListener(v -> {
                BottomReplyFragment bottomReplyFragment = new BottomReplyFragment(context, comment.getReplies());
                bottomReplyFragment.show();
            });
        }
        NineGridUtil.setDataSource(holder.nineGridImageViewer, ImageUrlUtil.urlToImageViewInfo(comment.getImageUrls()));
        Glide.with(context).load(comment.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    @Override
    public void onClick(View view) {

    }

    public interface OnCommentItemClickListener {
        void onClick(View v, Comment comment);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView timeView;
        public TextView contentView;
        public IconTextItem reply;
        public IconTextItem thumbup;
        public IconTextItem thumbdown;
        public IconTextItem share;
        public ImageView more;
        public LinearLayout repliesLayout;
        public CircleImageView avatarView;
        public NineGridImageView<ImageViewInfo> nineGridImageViewer;
        private MyNineGridImageViewAdapter mAdapter;
        private TextView expandView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.comment_item_username);
            timeView = view.findViewById(R.id.comment_item_time);
            contentView = view.findViewById(R.id.comment_item_content);
            reply = view.findViewById(R.id.comment_item_reply);
            thumbup = view.findViewById(R.id.comment_item_thumbup);
            thumbdown = view.findViewById(R.id.comment_item_thumbdown);
            share = view.findViewById(R.id.comment_item_share);
            more = view.findViewById(R.id.comment_item_more);
            repliesLayout = view.findViewById(R.id.comment_item_replies_layout);
            avatarView = view.findViewById(R.id.comment_item_avatar);
            nineGridImageViewer = view.findViewById(R.id.comment_item_nine_grid);
            expandView = view.findViewById(R.id.comment_item_expand);
            mAdapter = new MyNineGridImageViewAdapter();
            nineGridImageViewer.setAdapter(mAdapter);
        }
    }
}
