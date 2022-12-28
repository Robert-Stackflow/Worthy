package com.cloudchewie.client.domin;

import androidx.annotation.NonNull;

public class PoiDetail {
    String tag;
    String type;
    String detail_url;
    String overall_rating;
    String shop_hours;
    PoiLocation navi_location;

    @NonNull
    @Override
    public String toString() {
        return "PoiDetail{" +
                "tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", detail_url='" + detail_url + '\'' +
                ", overall_rating='" + overall_rating + '\'' +
                ", shop_hours='" + shop_hours + '\'' +
                ", navi_location=" + navi_location +
                '}';
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(String overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getShop_hours() {
        return shop_hours;
    }

    public void setShop_hours(String shop_hours) {
        this.shop_hours = shop_hours;
    }

    public PoiLocation getNavi_location() {
        return navi_location;
    }

    public void setNavi_location(PoiLocation navi_location) {
        this.navi_location = navi_location;
    }
}
