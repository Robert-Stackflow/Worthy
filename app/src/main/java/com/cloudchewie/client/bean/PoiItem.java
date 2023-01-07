package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

public class PoiItem {
    String name;
    String address;
    String province;
    String city;
    String area;
    String telephone;
    String detail;
    String uid;
    String street_id;
    PoiLocation location;
    PoiDetail detail_info;

    @NonNull
    @Override
    public String toString() {
        return "PoiItem{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", telephone='" + telephone + '\'' +
                ", detail='" + detail + '\'' +
                ", uid='" + uid + '\'' +
                ", street_id='" + street_id + '\'' +
                ", location=" + location +
                ", detail_info=" + detail_info +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public PoiLocation getLocation() {
        return location;
    }

    public void setLocation(PoiLocation location) {
        this.location = location;
    }

    public PoiDetail getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(PoiDetail detail_info) {
        this.detail_info = detail_info;
    }
}
