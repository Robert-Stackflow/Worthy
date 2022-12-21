package com.cloudchewie.client.adapter;

import android.content.Context;

import com.cloudchewie.client.domin.User;

public class AttractionListAdapterBuilder {
    private String location;
    private String topic;
    private User followUser;
    private User goneUser;
    private User wantUser;
    private Context context;

    public static AttractionListAdapterBuilder locationOf(String location) {
        return new AttractionListAdapterBuilder().location(location);
    }

    public static AttractionListAdapterBuilder topicOf(String topic) {
        return new AttractionListAdapterBuilder().topic(topic);
    }

    public static AttractionListAdapterBuilder followOf(User user) {
        return new AttractionListAdapterBuilder().follow(user);
    }

    public static AttractionListAdapterBuilder goneOf(User user) {
        return new AttractionListAdapterBuilder().gone(user);
    }

    public static AttractionListAdapterBuilder wantOf(User user) {
        return new AttractionListAdapterBuilder().want(user);
    }

    public static AttractionListAdapterBuilder with(Context context) {
        return new AttractionListAdapterBuilder().want(user);
    }

    public static AttractionListAdapter build() {
        AttractionListAdapter adapter=new
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public User getFollowUser() {
        return followUser;
    }

    public void setFollowUser(User followUser) {
        this.followUser = followUser;
    }

    public User getGoneUser() {
        return goneUser;
    }

    public void setGoneUser(User goneUser) {
        this.goneUser = goneUser;
    }

    public User getWantUser() {
        return wantUser;
    }

    public void setWantUser(User wantUser) {
        this.wantUser = wantUser;
    }

    public AttractionListAdapterBuilder location(String location) {
        AttractionListAdapterBuilder attractionListAdapterBuilder = new AttractionListAdapterBuilder();
        attractionListAdapterBuilder.setLocation(location);
        return attractionListAdapterBuilder;
    }

    public AttractionListAdapterBuilder topic(String topic) {
        AttractionListAdapterBuilder attractionListAdapterBuilder = new AttractionListAdapterBuilder();
        attractionListAdapterBuilder.setTopic(topic);
        return attractionListAdapterBuilder;
    }

    public AttractionListAdapterBuilder follow(User user) {
        AttractionListAdapterBuilder attractionListAdapterBuilder = new AttractionListAdapterBuilder();
        attractionListAdapterBuilder.setFollowUser(user);
        return attractionListAdapterBuilder;
    }

    public AttractionListAdapterBuilder gone(User user) {
        AttractionListAdapterBuilder attractionListAdapterBuilder = new AttractionListAdapterBuilder();
        attractionListAdapterBuilder.setGoneUser(user);
        return attractionListAdapterBuilder;
    }

    public AttractionListAdapterBuilder want(User user) {
        AttractionListAdapterBuilder attractionListAdapterBuilder = new AttractionListAdapterBuilder();
        attractionListAdapterBuilder.setWantUser(user);
        return attractionListAdapterBuilder;
    }

}
