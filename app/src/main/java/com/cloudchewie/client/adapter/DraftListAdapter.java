package com.cloudchewie.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.create.CreateArticleActivity;
import com.cloudchewie.client.activity.create.CreateAttractionActivity;
import com.cloudchewie.client.activity.create.CreatePostActivity;
import com.cloudchewie.client.activity.create.CreateTopicActivity;
import com.cloudchewie.client.entity.Article;
import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.entity.Draft;
import com.cloudchewie.client.entity.Post;
import com.cloudchewie.client.entity.RequestAttraction;
import com.cloudchewie.client.entity.RequestTopic;
import com.cloudchewie.client.entity.Topic;
import com.cloudchewie.client.util.basic.DateUtil;
import com.cloudchewie.client.util.image.CornerTransformation;
import com.cloudchewie.client.util.ui.RichEditorUtil;
import com.cloudchewie.ui.MyDialog;

import java.util.List;

public class DraftListAdapter extends RecyclerView.Adapter<DraftListAdapter.MyViewHolder> {
    private List<Draft> drafts;
    private Context context;
    private Draft.DRAFT_TYPE type;

    public DraftListAdapter(Context context, List<Draft> drafts, Draft.DRAFT_TYPE type) {
        this.drafts = drafts;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public DraftListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_draft_item, parent, false);
        return new DraftListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftListAdapter.MyViewHolder holder, int position) {
        if (null == drafts) {
            return;
        }
        if (position < 0 || position >= drafts.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Draft draft = drafts.get(position);
        if (null == draft) {
            return;
        }
        switch (type) {
            case POST:
                if (draft.getObject() != null && draft.getObject() instanceof Post) {
                    Post post = (Post) draft.getObject();
                    String content = post.getContent();
                    holder.contentView.setText(TextUtils.isEmpty(content) ? "" : content);
                    holder.titleView.setText("帖子草稿");
                    holder.tagView.setText("帖子");
                    holder.timeView.setText(DateUtil.beautifyTime(draft.getLastSaveTime()));
                    holder.mItemView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CreatePostActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("post", post);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    });
                    if (post.getImageUrls() == null || post.getImageUrls().size() == 0)
                        holder.coverView.setVisibility(View.GONE);
                    else
                        Glide.with(context).load(post.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(new CornerTransformation(context, 5))).into(holder.coverView);
                }
                break;
            case ARTICLE:
                if (draft.getObject() != null && draft.getObject() instanceof Article) {
                    Article article = (Article) draft.getObject();
                    String content = RichEditorUtil.getPlainText(article.getContent());
                    holder.tagView.setText("文章");
                    holder.contentView.setText(TextUtils.isEmpty(content) ? "" : content);
                    holder.titleView.setText(TextUtils.isEmpty(article.getTitle()) ? "文章草稿" : article.getTitle());
                    holder.timeView.setText(DateUtil.beautifyTime(draft.getLastSaveTime()));
                    holder.mItemView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CreateArticleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("article", article);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    });
                    if (article.getImageUrls() == null || article.getImageUrls().size() == 0)
                        holder.coverView.setVisibility(View.GONE);
                    else
                        Glide.with(context).load(article.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(new CornerTransformation(context, 5))).into(holder.coverView);
                }
                break;
            case ATTRACTION:
                if (draft.getObject() != null && draft.getObject() instanceof RequestAttraction) {
                    RequestAttraction requestAttraction = (RequestAttraction) draft.getObject();
                    Attraction attraction = requestAttraction.getAttraction();
                    String title;
                    String content;
                    if (attraction == null) {
                        title = "景点创建申请草稿";
                    } else {
                        if (!TextUtils.isEmpty(attraction.getName())) title = attraction.getName();
                        else if (!TextUtils.isEmpty(attraction.getLocation()))
                            title = attraction.getLocation();
                        else title = "景点创建申请草稿";
                    }
                    if (attraction == null) {
                        if (!TextUtils.isEmpty(requestAttraction.getReason()))
                            content = requestAttraction.getReason();
                        else content = "";
                    } else {
                        if (!TextUtils.isEmpty(attraction.getDescribe()))
                            content = attraction.getDescribe();
                        else if (!TextUtils.isEmpty(requestAttraction.getReason()))
                            content = requestAttraction.getReason();
                        else content = "";
                    }
                    holder.titleView.setText(title);
                    holder.contentView.setText(content);
                    holder.tagView.setText("景点申请");
                    holder.timeView.setText(DateUtil.beautifyTime(draft.getLastSaveTime()));
                    holder.mItemView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CreateAttractionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("request_attraction", requestAttraction);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    });
                    if (requestAttraction.getImageUrls() == null || requestAttraction.getImageUrls().size() == 0)
                        holder.coverView.setVisibility(View.GONE);
                    else
                        Glide.with(context).load(requestAttraction.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(new CornerTransformation(context, 5))).into(holder.coverView);
                }
                break;
            case TOPIC:
                if (draft.getObject() != null && draft.getObject() instanceof RequestTopic) {
                    RequestTopic requestTopic = (RequestTopic) draft.getObject();
                    Topic topic = requestTopic.getTopic();
                    String title;
                    String content;
                    if (topic == null) {
                        title = "话题创建申请草稿";
                    } else {
                        if (!TextUtils.isEmpty(topic.getName())) title = topic.getName();
                        else title = "话题创建申请草稿";
                    }
                    if (topic == null) {
                        if (!TextUtils.isEmpty(requestTopic.getReason()))
                            content = requestTopic.getReason();
                        else content = "";
                    } else {
                        if (!TextUtils.isEmpty(topic.getDescribe()))
                            content = topic.getDescribe();
                        else if (!TextUtils.isEmpty(requestTopic.getReason()))
                            content = requestTopic.getReason();
                        else content = "";
                    }
                    holder.titleView.setText(title);
                    holder.contentView.setText(content);
                    holder.tagView.setText("话题申请");
                    holder.timeView.setText(DateUtil.beautifyTime(draft.getLastSaveTime()));
                    holder.mItemView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CreateTopicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("request_topic", requestTopic);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    });
                    if (requestTopic.getImageUrls() == null || requestTopic.getImageUrls().size() == 0)
                        holder.coverView.setVisibility(View.GONE);
                    else
                        Glide.with(context).load(requestTopic.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(new CornerTransformation(context, 5))).into(holder.coverView);
                }
                break;
        }
        holder.deleteButton.setOnClickListener(v -> {
            MyDialog dialog = new MyDialog(context);
            dialog.setTitle("确认删除");
            dialog.setMessage("删除草稿后无法恢复");
            dialog.setNegtive("取消");
            dialog.setPositive("删除");
            dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    int i = holder.getAdapterPosition();
                    notifyItemRemoved(i);
                    drafts.remove(i);
                    dialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return drafts == null ? 0 : drafts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView titleView;
        public TextView tagView;
        public TextView timeView;
        public TextView contentView;
        public ImageView deleteButton;
        public ImageView coverView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            tagView = view.findViewById(R.id.widget_draft_item_type);
            coverView = view.findViewById(R.id.widget_draft_item_cover);
            titleView = view.findViewById(R.id.widget_draft_item_title);
            timeView = view.findViewById(R.id.widget_draft_item_last_save);
            contentView = view.findViewById(R.id.widget_draft_item_content);
            deleteButton = view.findViewById(R.id.widget_draft_item_delete);
        }
    }
}
