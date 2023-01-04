/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_DARK;
import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_TEA;
import static com.cloudchewie.client.util.map.MapUtil.DARK_ID;
import static com.cloudchewie.client.util.map.MapUtil.LIGHT_ID;
import static com.cloudchewie.client.util.map.MapUtil.getCustomStyleFilePath;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CustomMapStyleCallBack;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapCustomStyleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.SuggestListAdapter;
import com.cloudchewie.client.util.map.CountyUtil;
import com.cloudchewie.client.util.map.MapUtil;
import com.cloudchewie.client.util.ui.DarkModeUtil;
import com.cloudchewie.client.util.ui.KeyBoardUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.search.SearchLayout;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    boolean itemClicked = false;
    View mainView;
    MapView mapView;
    BaiduMap baiduMap;
    SearchLayout searchLayout;
    EditText searchInput;
    LocationClient locationClient;
    LocationClientOption locationOption;
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
    private PoiSearch poiSearch;
    private SuggestionSearch suggestionSearch;
    private RecyclerView recyclerView;
    private SuggestListAdapter suggestListAdapter;
    private int loadIndex = 0;
    private TextView poiTitle;
    private TextView poiAddress;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner countySpinner;
    private Marker preSelectMarker;
    private LinearLayout layoutDetailInfo;
    private HashMap<Marker, PoiInfo> markerPoiInfo = new HashMap<>();
    private MyTextWatcher myTextWatcher = new MyTextWatcher();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_map, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.map_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        initView();
        initMap();
        initSpinner();
        locateMyLocation();
        return mainView;
    }

    void initView() {
        mapView = mainView.findViewById(R.id.map_view);
        searchLayout = mainView.findViewById(R.id.map_search_layout);
        recyclerView = mainView.findViewById(R.id.map_sug_recyclerview);
        layoutDetailInfo = mainView.findViewById(R.id.poiInfo);
        poiTitle = mainView.findViewById(R.id.poiTitle);
        poiAddress = mainView.findViewById(R.id.poiAddress);
        provinceSpinner = mainView.findViewById(R.id.map_search_province_filter);
        citySpinner = mainView.findViewById(R.id.map_search_city_filter);
        countySpinner = mainView.findViewById(R.id.map_search_county_filter);
        mainView.findViewById(R.id.goto_mylocation).setOnClickListener(this);
        searchInput = searchLayout.getSearchEdit();
        searchInput.addTextChangedListener(new MyTextWatcher());
        searchLayout.setOnTextSearchListener(s -> null, s -> {
            searchInCity();
            return null;
        });
        suggestListAdapter = new SuggestListAdapter();
        suggestListAdapter.setOnItemClickListener((view, position, suggestInfo) -> {
            locateSuggestionInfo(suggestInfo);
            setPoiTextWithLocateSuggestInfo(suggestInfo);
        });
        if (null == recyclerView) return;
        recyclerView.setVisibility(View.GONE);
        recyclerView.setAdapter(suggestListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyBoardUtil.hideKeyBoard(getActivity());
            }
        });
    }

    void initMap() {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        myLocationConfiguration.accuracyCircleFillColor = 0x00000000;
        myLocationConfiguration.accuracyCircleStrokeColor = 0x00000000;
        baiduMap.setMyLocationConfiguration(myLocationConfiguration);
        View child = mapView.getChildAt(1);
        if ((child instanceof ImageView || child instanceof ZoomControls))
            child.setVisibility(View.GONE);
        baiduMap.setOnMapLoadedCallback(() -> {
            mapView.setScaleControlPosition(new Point(100, 750));
            mapView.showScaleControl(false);
            mapView.showZoomControls(false);
        });
        setMapStyle();
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
    }

    void setMapStyle() {
        if (mapView == null) return;
        MapCustomStyleOptions mapCustomStyleOptions = new MapCustomStyleOptions();
        mapCustomStyleOptions.localCustomStylePath(getCustomStyleFilePath(getActivity(), DarkModeUtil.isDarkMode(getContext()) ? CUSTOM_FILE_NAME_DARK : CUSTOM_FILE_NAME_TEA));
        mapCustomStyleOptions.customStyleId(DarkModeUtil.isDarkMode(getContext()) ? DARK_ID : LIGHT_ID);
        mapView.setMapCustomStyle(mapCustomStyleOptions, new CustomMapStyleCallBack() {
            @Override
            public boolean onPreLoadLastCustomMapStyle(String customStylePath) {
                return false;
            }

            @Override
            public boolean onCustomMapStyleLoadSuccess(boolean hasUpdate, String customStylePath) {
                return false;
            }

            @Override
            public boolean onCustomMapStyleLoadFailed(int status, String Message, String customStylePath) {
                return false;
            }
        });
    }

    void initSpinner() {
        CountyUtil.loadData(getActivity());
        provinces = CountyUtil.getProvices();
        cities = CountyUtil.getCities();
        counties = CountyUtil.getCounties();
        provinceAdapter = new ArrayAdapter<>(getActivity(), R.layout.widget_spinner, provinces);
        provinceAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
        cityAdapter = new ArrayAdapter<>(getActivity(), R.layout.widget_spinner, cities.get(0));
        cityAdapter.setDropDownViewResource(R.layout.widget_spinner_item);
        countyAdapter = new ArrayAdapter<>(getActivity(), R.layout.widget_spinner, counties.get(0).get(0));
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
                cityAdapter = new ArrayAdapter<>(getActivity(), R.layout.widget_spinner, cities.get(i));
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
                countyAdapter = new ArrayAdapter<>(getActivity(), R.layout.widget_spinner, new ArrayList<>(new HashSet<>(counties.get(province_postion).get(city_postion))));
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

    void locateMyLocation() {
        try {
            locationClient = new LocationClient(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        locationOption.setScanSpan(0);
        locationOption.setCoorType("bd09ll");
        locationOption.setIsNeedAddress(true);
        locationOption.setNeedDeviceDirect(false);
        locationClient.setLocOption(locationOption);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationClient.registerLocationListener(new MyLocationListener());
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        recyclerView.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            loadIndex = 0;
            IToast.makeTextTop(getActivity(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        List<PoiInfo> poiInfos = poiResult.getAllPoi();
        if (null == poiInfos) return;
        setPoiResult(poiInfos);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            loadIndex = 0;
            return;
        }
        List<SuggestionResult.SuggestionInfo> suggesInfos = suggestionResult.getAllSuggestions();
        if (null == suggesInfos) return;
        hidePoiInfoLayout();
        if (suggesInfos.size() > 0 && !itemClicked) recyclerView.setVisibility(View.VISIBLE);
        else recyclerView.setVisibility(View.GONE);
        if (null == suggestListAdapter) {
            suggestListAdapter = new SuggestListAdapter(suggesInfos);
            suggestListAdapter.setOnItemClickListener((view, position, suggestInfo) -> {
                locateSuggestionInfo(suggestInfo);
                setPoiTextWithLocateSuggestInfo(suggestInfo);
            });
        } else suggestListAdapter.updateData(suggesInfos);
        suggestListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        KeyBoardUtil.hideKeyBoard(getActivity());
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        KeyBoardUtil.hideKeyBoard(getActivity());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null == marker || null == markerPoiInfo || markerPoiInfo.size() <= 0) return false;
        Iterator<Map.Entry<Marker, PoiInfo>> itr = markerPoiInfo.entrySet().iterator();
        Marker tmpMarker;
        PoiInfo poiInfo = null;
        Map.Entry<Marker, PoiInfo> markerPoiInfoEntry;
        while (itr.hasNext()) {
            markerPoiInfoEntry = itr.next();
            tmpMarker = markerPoiInfoEntry.getKey();
            if (null == tmpMarker) continue;
            if (Objects.equals(tmpMarker.getId(), marker.getId())) {
                poiInfo = markerPoiInfoEntry.getValue();
                break;
            }
        }
        if (null == poiInfo) return false;
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(poiInfo.getLocation()).zoom(12.0f).build()));
        baiduMap.showInfoWindow(getPoiInfoWindow(poiInfo));
        showPoiInfoLayout(poiInfo);
        if (null != preSelectMarker) preSelectMarker.setScale(1.0f);
        marker.setScale(2f);
        preSelectMarker = marker;
        return true;
    }

    private void setPoiTextWithLocateSuggestInfo(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo) return;
        searchInput.removeTextChangedListener(myTextWatcher);
        searchInput.setText(suggestInfo.getKey());
        searchInput.setSelection(suggestInfo.getKey().length());
        searchInput.addTextChangedListener(myTextWatcher);
        itemClicked = true;
    }

    private void locateSuggestionInfo(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo) return;
        if (null == recyclerView || null == mapView) return;
        KeyBoardUtil.hideKeyBoard(getActivity());
        recyclerView.setVisibility(View.INVISIBLE);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(suggestInfo.getPt()).zoom(12.0f).build()));
        clearData();
        if (!showSuggestMarker(suggestInfo)) {
            setPoiTextWithLocateSuggestInfo(suggestInfo);
            searchInCity();
        }
    }


    @Contract("null -> false")
    private boolean showSuggestMarker(SuggestionResult.SuggestionInfo suggestionInfo) {
        if (null == suggestionInfo) return false;
        baiduMap.addOverlay(new MarkerOptions().position(suggestionInfo.getPt()).icon(MapUtil.getMarkerIcon()).scaleX(2f).scaleY(2f));
        return true;
    }

    private void searchInCity() {
        String cityStr = citySpinner.getSelectedItem().toString();
        String keyWordStr = searchInput.getText().toString();
        if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) return;
        recyclerView.setVisibility(View.INVISIBLE);
        poiSearch.searchInCity((new PoiCitySearchOption()).city(cityStr).keyword(keyWordStr).pageNum(loadIndex).cityLimit(true).scope(1));
    }

    private void setPoiResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0) return;
        clearData();
        LatLng latLng = poiInfos.get(0).getLocation();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(mapStatusUpdate);
        Iterator<PoiInfo> itr = poiInfos.iterator();
        List<LatLng> latLngs = new ArrayList<>();
        PoiInfo poiInfo;
        int i = 0;
        while (itr.hasNext()) {
            poiInfo = itr.next();
            if (null == poiInfo) continue;
            locatePoiInfo(poiInfo, i);
            latLngs.add(poiInfo.getLocation());
            if (0 == i) showPoiInfoLayout(poiInfo);
            i++;
        }
        setBounds(latLngs);
    }

    private void clearData() {
        baiduMap.clear();
        markerPoiInfo.clear();
        preSelectMarker = null;
    }

    private void locatePoiInfo(PoiInfo poiInfo, int i) {
        if (null == poiInfo) {
            return;
        }
        KeyBoardUtil.hideKeyBoard(getActivity());
        showPoiMarker(poiInfo, i);
    }

    private void showPoiMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) {
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions().position(poiInfo.getLocation()).icon(MapUtil.getMarkerIcon());
        if (0 == i) {
            InfoWindow infoWindow = getPoiInfoWindow(poiInfo);
            markerOptions.scaleX(2f).scaleY(2f).infoWindow(infoWindow);
        }
        Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
        if (null != marker) {
            markerPoiInfo.put(marker, poiInfo);
            if (0 == i) {
                preSelectMarker = marker;
            }
        }
    }

    @NonNull
    @Contract("_ -> new")
    @SuppressLint("UseCompatLoadingForDrawables")
    private InfoWindow getPoiInfoWindow(@NonNull PoiInfo poiInfo) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_infowindow, null);
        ((TextView) view.findViewById(R.id.infowindow_title)).setText(poiInfo.getName());
        ((TextView) view.findViewById(R.id.infowindow_address)).setText(poiInfo.getAddress());
        return new InfoWindow(view, poiInfo.getLocation(), -150);
    }

    private void showPoiInfoLayout(PoiInfo poiInfo) {
        if (null == layoutDetailInfo || null == poiInfo) {
            return;
        }
        if (null == poiTitle) {
            return;
        }
        if (null == poiAddress) {
            return;
        }
        layoutDetailInfo.setVisibility(View.VISIBLE);
        poiTitle.setText(poiInfo.getName());
        String address = poiInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            poiAddress.setVisibility(View.GONE);
        } else {
            poiAddress.setText(poiInfo.getAddress());
            poiAddress.setVisibility(View.VISIBLE);
        }
    }

    private void hidePoiInfoLayout() {
        if (null == layoutDetailInfo) return;
        layoutDetailInfo.setVisibility(View.GONE);
    }

    private void setBounds(List<LatLng> latLngs) {
        if (null == latLngs || latLngs.size() <= 0) {
            return;
        }
        int horizontalPadding = 80;
        int verticalPaddingBottom = 400;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLngs);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(), horizontalPadding, verticalPaddingBottom, horizontalPadding, verticalPaddingBottom);
        baiduMap.setMapStatus(mapStatusUpdate);
        baiduMap.setViewPadding(0, 0, 0, verticalPaddingBottom);
    }

    @Override
    public void onClick(View view) {
        recyclerView.setVisibility(View.GONE);
        if (view == mainView.findViewById(R.id.goto_mylocation)) {
            locateMyLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        if (locationClient != null) locationClient.stop();
        if (baiduMap != null) baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            int errorCode = location.getLocType();
            float radius = location.getRadius();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String coorType = location.getCoorType();
            if (errorCode == BDLocation.TYPE_NO_PERMISSION_LOCATION_FAIL)
                IToast.makeTextBottom(getActivity(), "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                locationOption.setScanSpan(0);
                locationClient.stop();
                locationClient.setLocOption(locationOption);
            }
        }
    }

    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0 && View.VISIBLE == recyclerView.getVisibility()) {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            itemClicked = false;
            String cityStr = citySpinner.getSelectedItem().toString();
            String keyWordStr = searchInput.getText().toString();
            if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr))
                return;
            if (View.VISIBLE == recyclerView.getVisibility())
                recyclerView.setVisibility(View.INVISIBLE);
            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(cityStr).keyword(keyWordStr).citylimit(true));
        }
    }
}
