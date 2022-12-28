package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

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
}
