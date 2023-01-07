package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

import java.util.List;

public class CityList {
    String letter;
    List<String> citylist;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<String> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<String> citylist) {
        this.citylist = citylist;
    }

    @NonNull
    @Override
    public String toString() {
        return "CityList{" +
                "letter='" + letter + '\'' +
                ", citynamelist=" + citylist +
                '}';
    }
}
