package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;

public class PoiLocation {
    double lat;
    double lng;

    @NonNull
    @Override
    public String toString() {
        return "PoiLocation{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }
}
