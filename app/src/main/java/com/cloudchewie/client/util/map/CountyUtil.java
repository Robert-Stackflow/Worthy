/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.util.map;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cloudchewie.client.util.system.AssetsUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CountyUtil {
    private static String LOCATION_FILE_NAME = "location.json";
    private static List<LocationBean> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    public static void loadData(Context context) {
        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
        options3Items = new ArrayList<>();
        String JsonData = AssetsUtil.getTextFileContent(context, LOCATION_FILE_NAME);
        ArrayList<LocationBean> locationBean = parseData(JsonData);
        options1Items = locationBean;
        for (int i = 0; i < locationBean.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
            for (int c = 0; c < locationBean.get(i).getCityList().size(); c++) {
                String cityName = locationBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);
                ArrayList<String> city_AreaList = new ArrayList<>();
                if (locationBean.get(i).getCityList().get(c).getArea() == null || locationBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(locationBean.get(i).getCityList().get(c).getArea());
                }
                city_AreaList.addAll(locationBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }
    }

    @NonNull
    public static ArrayList<String> getProvices() {
        ArrayList<String> provinces = new ArrayList<>();
        for (LocationBean locationBean : options1Items)
            provinces.add(locationBean.getName());
        return provinces;
    }

    @Contract(pure = true)
    public static ArrayList<ArrayList<String>> getCities() {
        return options2Items;
    }

    @Contract(pure = true)
    public static ArrayList<ArrayList<ArrayList<String>>> getCounties() {
        return options3Items;
    }

    @NonNull
    public static ArrayList<LocationBean> parseData(String result) {
        ArrayList<LocationBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                LocationBean entity = gson.fromJson(data.optJSONObject(i).toString(), LocationBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
