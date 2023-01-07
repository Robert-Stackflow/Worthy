package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

public class AddressComponent {
    String country;
    int country_code;
    String country_code_iso;
    String country_code_iso2;
    String province;
    String city;
    String district;
    String town;
    String town_code;
    String street;
    String sreet_number;
    int adcode;
    String direction;
    String distance;

    @NonNull
    @Override
    public String toString() {
        return "AddressComponent{" +
                "country='" + country + '\'' +
                ", country_code=" + country_code +
                ", country_code_iso='" + country_code_iso + '\'' +
                ", country_code_iso2='" + country_code_iso2 + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", town='" + town + '\'' +
                ", town_code='" + town_code + '\'' +
                ", street='" + street + '\'' +
                ", sreet_number='" + sreet_number + '\'' +
                ", adcode=" + adcode +
                ", direction='" + direction + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code_iso() {
        return country_code_iso;
    }

    public void setCountry_code_iso(String country_code_iso) {
        this.country_code_iso = country_code_iso;
    }

    public String getCountry_code_iso2() {
        return country_code_iso2;
    }

    public void setCountry_code_iso2(String country_code_iso2) {
        this.country_code_iso2 = country_code_iso2;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown_code() {
        return town_code;
    }

    public void setTown_code(String town_code) {
        this.town_code = town_code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSreet_number() {
        return sreet_number;
    }

    public void setSreet_number(String sreet_number) {
        this.sreet_number = sreet_number;
    }

    public int getAdcode() {
        return adcode;
    }

    public void setAdcode(int adcode) {
        this.adcode = adcode;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
