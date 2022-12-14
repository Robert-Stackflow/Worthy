package com.cloudchewie.client.entity;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Article implements Serializable {
    //TODO 无效,引用为userId
    User user;
    Date date;
    String content;
    List<String> imageUrls;
    //TODO 无效,引用为attractionId
    Attraction attraction;
    //TODO 无效,引用为topicId
    List<Topic> topics;
    int commentCount;
    int thumbupCount;
    String title;
    int collectionCount;
    Post.POST_TYPE type;

    public Article() {
    }

    public Article(User user, Date date, String title, String content, int commentCount, int thumbupCount, int collectionCount, Attraction attraction, Topic topic) {
        this.user = user;
        this.date = date;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.thumbupCount = thumbupCount;
        this.collectionCount = collectionCount;
        this.attraction = attraction;
        this.topics = new ArrayList<>();
        topics.add(topic);
        this.imageUrls = ImageUrlUtil.getUrls(15);
    }

    public Article(User user, Date date, String content, int commentCount, int thumbupCount, int collectionCount, Attraction attraction, List<Topic> topics) {
        this.date = date;
        this.content = content;
        this.commentCount = commentCount;
        this.thumbupCount = thumbupCount;
        this.collectionCount = collectionCount;
        this.attraction = attraction;
        this.topics = topics;
        this.imageUrls = ImageUrlUtil.getUrls(15);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Post.POST_TYPE getType() {
        return type;
    }

    public void setType(Post.POST_TYPE type) {
        this.type = type;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setImages(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @NonNull
    @Override
    public String toString() {
        return "Article{" + "user=" + user + ", date=" + date + ", content='" + content + '\'' + ", imageUrls=" + imageUrls + ", attraction=" + attraction + ", topics=" + topics + ", commentCount=" + commentCount + ", thumbupCount=" + thumbupCount + ", collectionCount=" + collectionCount + ", type=" + type + '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
