/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.cloudchewie.client.api.cluster.projection;

import androidx.annotation.NonNull;

public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    @Override
    public String toString() {
        return "Point{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }
}
