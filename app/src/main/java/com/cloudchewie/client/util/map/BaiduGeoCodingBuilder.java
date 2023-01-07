package com.cloudchewie.client.util.map;

import androidx.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.cloudchewie.client.bean.PoiLocation;
import com.cloudchewie.client.util.basic.Constant;

import org.jetbrains.annotations.Contract;

public class BaiduGeoCodingBuilder {

    @NonNull
    @Contract(pure = true)
    public static String buildGeo(String address, String city) {
        return Constant.BAIDU_GEO_BASE_URL + "?" + "address=" + address +
                "&" + "city=" + city +
                "&" + "ak=" + Constant.BAIDU_SECRET_KEY_WEB +
                "&" + "output=" + "json";
    }

    @NonNull
    @Contract(pure = true)
    public static String buildGeo(String address) {
        return Constant.BAIDU_GEO_BASE_URL + "?" + "address=" + address +
                "&" + "ak=" + Constant.BAIDU_SECRET_KEY_WEB +
                "&" + "output=" + "json";
    }

    @NonNull
    @Contract(pure = true)
    public static String buildReverseGeo(@NonNull PoiLocation poiLocation) {
        return Constant.BAIDU_REVERSE_GEO_BASE_URL + "?" + "location=" + poiLocation.getLat() + "," + poiLocation.getLng() +
                "&" + "ak=" + Constant.BAIDU_SECRET_KEY_WEB +
                "&" + "output=" + "json";
    }

    @NonNull
    @Contract(pure = true)
    public static String buildReverseGeo(LatLng latLng) {
        return Constant.BAIDU_REVERSE_GEO_BASE_URL + "?" + "location=" + latLng.latitude + "," + latLng.longitude +
                "&" + "ak=" + Constant.BAIDU_SECRET_KEY_WEB +
                "&" + "output=" + "json";
    }
}
