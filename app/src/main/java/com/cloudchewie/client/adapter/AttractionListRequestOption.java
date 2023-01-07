package com.cloudchewie.client.adapter;

import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.entity.User;

import java.util.List;

public class AttractionListRequestOption {
    private String location;
    private String topic;
    private User followUser;
    private User goneUser;
    private User wantUser;
    private List<Attraction> attractionList;

    public List<Attraction> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
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

    public AttractionListRequestOption location(String location) {
        setLocation(location);
        return this;
    }

    public AttractionListRequestOption topic(String topic) {
        setTopic(topic);
        return this;
    }

    public AttractionListRequestOption follow(User user) {
        setFollowUser(user);
        return this;
    }

    public AttractionListRequestOption gone(User user) {
        setGoneUser(user);
        return this;
    }

    public AttractionListRequestOption want(User user) {
        setWantUser(user);
        return this;
    }

    public AttractionListRequestOption data(List<Attraction> attractionList) {
        setAttractionList(attractionList);
        return this;
    }
}
