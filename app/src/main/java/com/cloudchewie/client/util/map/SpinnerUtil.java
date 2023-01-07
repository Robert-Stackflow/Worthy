package com.cloudchewie.client.util.map;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cloudchewie.client.R;

import java.util.ArrayList;
import java.util.HashSet;

public class SpinnerUtil {
    int province_postion = 0;
    int city_postion = 0;
    int county_poisition = 0;
    String seletced_province;
    String seletced_county;
    String seletced_city;
    ArrayAdapter<String> provinceAdapter;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> countyAdapter;
    ArrayList<String> provinces;
    ArrayList<ArrayList<String>> cities;
    ArrayList<ArrayList<ArrayList<String>>> counties;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner countySpinner;

    void initSpinner(Activity activity) {
        CountyUtil.loadData(activity);
        provinces = CountyUtil.getProvices();
        cities = CountyUtil.getCities();
        counties = CountyUtil.getCounties();
        provinceAdapter = new ArrayAdapter<>(activity, R.layout.widget_spinner, provinces);
        provinceAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
        cityAdapter = new ArrayAdapter<>(activity, R.layout.widget_spinner, cities.get(0));
        cityAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
        countyAdapter = new ArrayAdapter<>(activity, R.layout.widget_spinner, counties.get(0).get(0));
        countyAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                province_postion = i;
                cityAdapter = new ArrayAdapter<>(activity, R.layout.widget_spinner, cities.get(i));
                cityAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
                citySpinner.setAdapter(cityAdapter);
                seletced_province = provinces.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_postion = i;
                countyAdapter = new ArrayAdapter<>(activity, R.layout.widget_spinner, new ArrayList<>(new HashSet<>(counties.get(province_postion).get(city_postion))));
                countyAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
                countySpinner.setAdapter(countyAdapter);
                seletced_city = cities.get(province_postion).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletced_county = counties.get(province_postion).get(city_postion).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        provinceSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        citySpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        countySpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

}
