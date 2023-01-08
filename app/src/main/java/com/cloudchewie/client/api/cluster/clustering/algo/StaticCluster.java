/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.cloudchewie.client.api.cluster.clustering.algo;

import androidx.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.cloudchewie.client.api.cluster.clustering.Cluster;
import com.cloudchewie.client.api.cluster.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A cluster whose center is determined upon creation.
 */
public class StaticCluster<T extends ClusterItem> implements Cluster<T> {
    private final LatLng mCenter;
    private final List<T> mItems = new ArrayList<>();

    public StaticCluster(LatLng center) {
        mCenter = center;
    }


    public boolean add(T t) {
        return mItems.add(t);
    }

    @Override
    public LatLng getPosition() {
        return mCenter;
    }

    public boolean remove(T t) {
        return mItems.remove(t);
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public int getSize() {
        return mItems.size();
    }

    @NonNull
    @Override
    public String toString() {
        return "StaticCluster{"
                + "mCenter=" + mCenter
                + ", mItems.size=" + mItems.size()
                + '}';
    }
}