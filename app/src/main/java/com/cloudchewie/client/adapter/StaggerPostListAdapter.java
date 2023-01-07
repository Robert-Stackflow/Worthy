package com.cloudchewie.client.adapter;

import static com.cloudchewie.client.util.basic.StringUtil.handleLineBreaks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.AttractionDetailActivity;
import com.cloudchewie.client.activity.discover.PostDetailActivity;
import com.cloudchewie.client.activity.user.HomePageActivity;
import com.cloudchewie.client.entity.Post;
import com.cloudchewie.client.util.image.CornerTransformation;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.IconTextItem;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaggerPostListAdapter extends RecyclerView.Adapter<StaggerPostListAdapter.MyViewHolder> {
    private List<Post> posts;
    private Context context;

    public StaggerPostListAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public StaggerPostListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_stagger_post_item, parent, false);
        return new StaggerPostListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
        holder.nameView.setText(post.getUser().getUsername());
        holder.contentView.setText(handleLineBreaks(post.getContent()));
        holder.attraction.setText(post.getAttraction().getName());
        holder.attraction.setOnClickListener(v -> {
            if (!(context instanceof AttractionDetailActivity && Objects.equals(((AttractionDetailActivity) context).getAttraction(), holder.attraction.getText()))) {
                Intent intent = new Intent(context, AttractionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("attraction", post.getAttraction());
                intent.putExtras(bundle);
                context.startActivity(intent);
            } else {
                IToast.makeTextTop(context, "已经在看" + post.getAttraction().getName() + "了喔", Toast.LENGTH_SHORT).show();
            }
        });
        holder.avatarView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", post.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", post.getUser());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        Glide.with(context).load(post.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(context, 5, true, true, false, false))).into(holder.imageView);
        Glide.with(context).load(post.getUser().getAvatarUrl()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mainView;
        public TextView nameView;
        public TextView contentView;
        public IconTextItem attraction;
        public CircleImageView avatarView;
        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
            avatarView = view.findViewById(R.id.widget_stagger_post_item_avatar);
            nameView = view.findViewById(R.id.widget_stagger_post_item_username);
            contentView = view.findViewById(R.id.widget_stagger_post_item_content);
            attraction = view.findViewById(R.id.widget_stagger_post_item_location);
            imageView = view.findViewById(R.id.widget_stagger_post_item_image);
            int width = ((Activity) imageView.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = (int) (200 + Math.random() * 400);
            imageView.setLayoutParams(params);
        }
    }
}

