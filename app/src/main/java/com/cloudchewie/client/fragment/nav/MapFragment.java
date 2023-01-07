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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.cloudchewie.client.activity.discover.SelectCityActivity;
import com.cloudchewie.client.adapter.SpinnerAdapter;
import com.cloudchewie.client.adapter.SuggestListAdapter;
import com.cloudchewie.client.bean.GeoCoding;
import com.cloudchewie.client.request.MapRequest;
import com.cloudchewie.client.util.map.BaiduRequestBuilder;
import com.cloudchewie.client.util.map.MapUtil;
import com.cloudchewie.client.util.ui.DarkModeUtil;
import com.cloudchewie.client.util.ui.KeyBoardUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MySpinner;
import com.cloudchewie.ui.search.SearchLayout;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener, BaiduMap.OnMapTouchListener, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    boolean itemClicked = false;
    View mainView;
    MapView mapView;
    BaiduMap baiduMap;
    SearchLayout searchLayout;
    EditText searchInput;
    LocationClient locationClient;
    LocationClientOption locationOption;
    ActivityResultLauncher launcher;
    BDLocation myLocation;
    ImageButton gotoMyLocation;
    ConstraintLayout titleBar;
    private PoiSearch poiSearch;
    private SuggestionSearch suggestionSearch;
    private RecyclerView sugRecyclerView;
    private RecyclerView poiRecyclerView;
    private SuggestListAdapter suggestListAdapter;
    private int loadIndex = 0;
    private TextView poiTitle;
    private TextView poiAddress;
    private MySpinner citySpinner;
    private MySpinner typeSpinner;
    private MySpinner moreSpinner;
    private MySpinner distanceSpinner;
    private RecyclerView filterRecyclerView;
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
        mapView = mainView.findViewById(R.id.map_view);
        titleBar = mainView.findViewById(R.id.map_titlebar);
        layoutDetailInfo = mainView.findViewById(R.id.poiInfo);
        poiTitle = mainView.findViewById(R.id.poiTitle);
        poiAddress = mainView.findViewById(R.id.poiAddress);
        searchLayout = mainView.findViewById(R.id.map_search_layout);
        searchInput = searchLayout.getSearchEdit();
        sugRecyclerView = mainView.findViewById(R.id.map_sug_recyclerview);
        poiRecyclerView = mainView.findViewById(R.id.map_poi_recyclerview);
        filterRecyclerView = mainView.findViewById(R.id.map_search_filter_recyclerview);
        citySpinner = mainView.findViewById(R.id.map_search_filter_city);
        typeSpinner = mainView.findViewById(R.id.map_search_filter_type);
        moreSpinner = mainView.findViewById(R.id.map_search_filter_more);
        distanceSpinner = mainView.findViewById(R.id.map_search_filter_distance);
        gotoMyLocation = mainView.findViewById(R.id.goto_mylocation);
        initView();
        initRecyclerView();
        initSpinner();
        initMap();
        locateMyLocation(OnReceiveLocationOption.JUMPTO_UPDATESPINNER);
        return mainView;
    }

    void initView() {
        StatusBarUtil.setStatusBarMarginTop(titleBar, 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        launcher = registerForActivityResult(new SelectCityContract(), result -> {
            if (result != null) citySpinner.setText(result);
            locateLocation(MapRequest.getGeoCoding(citySpinner.getText(), citySpinner.getText()));
        });
        gotoMyLocation.setOnClickListener(this);
        searchInput.addTextChangedListener(new MyTextWatcher());
        searchLayout.setOnTextSearchListener(s -> {
            if (TextUtils.isEmpty(s)) hideSuggestLayout();
            return null;
        }, s -> {
            searchInCity();
            return null;
        });
        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) hideFilterLayout();
        });
    }

    void initRecyclerView() {
        suggestListAdapter = new SuggestListAdapter();
        suggestListAdapter.setOnItemClickListener((view, position, suggestInfo) -> {
            locateSuggestionInfo(suggestInfo);
            setPoiTextWithLocateSuggestInfo(suggestInfo);
        });
        if (null == sugRecyclerView) return;
        sugRecyclerView.setVisibility(View.GONE);
        sugRecyclerView.setAdapter(suggestListAdapter);
        sugRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sugRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyBoardUtil.hideKeyBoard(getActivity());
            }
        });
    }

    void initSpinner() {
        typeSpinner.setDataSource(Arrays.asList(getResources().getStringArray(R.array.poi_filter_type)));
        distanceSpinner.setDataSource(Arrays.asList(getResources().getStringArray(R.array.poi_filter_distance)));
        citySpinner.setOnCheckStateChangedListener(new MySpinner.onCheckStateChangedListener() {
            @Override
            public void onCheckStateChanged(MySpinner spinner, boolean isChecked) {
                filterRecyclerView.setVisibility(View.GONE);
                filterRecyclerView.setAdapter(null);
            }

            @Override
            public void onClicked(MySpinner spinner) {
                spinner.toggle();
                spinner.toggle();
                hideSuggestLayout();
                moreSpinner.setChecked(false);
                typeSpinner.setChecked(false);
                distanceSpinner.setChecked(false);
                if (myLocation == null) locateMyLocation(OnReceiveLocationOption.SELECT_CITY);
                else {
                    launcher.launch(myLocation.getDistrict());
                    clearData();
                }
            }
        });
        moreSpinner.setOnCheckStateChangedListener(new MySpinner.onCheckStateChangedListener() {
            @Override
            public void onCheckStateChanged(MySpinner spinner, boolean isChecked) {

            }

            @Override
            public void onClicked(MySpinner spinner) {
                hideSuggestLayout();
                citySpinner.setChecked(false);
                typeSpinner.setChecked(false);
                distanceSpinner.setChecked(false);
                spinner.toggle();
            }
        });
        typeSpinner.setOnCheckStateChangedListener(new MySpinner.onCheckStateChangedListener() {
            @Override
            public void onCheckStateChanged(MySpinner spinner, boolean isChecked) {
                Log.d("xuruida", String.valueOf(isChecked));
                if (isChecked) {
                    filterRecyclerView.setVisibility(View.VISIBLE);
                    filterRecyclerView.setAdapter(new SpinnerAdapter(getContext(), spinner.getDataSource()).setListener(position -> {
                        spinner.setCurrentIndex(position);
                        spinner.setChecked(false);
                        filterRecyclerView.setVisibility(View.GONE);
                        searchWithFilter();
                    }).setSelection(spinner.getCurrentIndex()));
                    filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    filterRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClicked(MySpinner spinner) {
                hideSuggestLayout();
                moreSpinner.setChecked(false);
                citySpinner.setChecked(false);
                distanceSpinner.setChecked(false);
                spinner.toggle();
            }
        });
        distanceSpinner.setOnCheckStateChangedListener(new MySpinner.onCheckStateChangedListener() {
            @Override
            public void onCheckStateChanged(MySpinner spinner, boolean isChecked) {
                if (isChecked) {
                    filterRecyclerView.setVisibility(View.VISIBLE);
                    filterRecyclerView.setAdapter(new SpinnerAdapter(getContext(), spinner.getDataSource()).setListener(position -> {
                        spinner.setCurrentIndex(position);
                        spinner.setChecked(false);
                        filterRecyclerView.setVisibility(View.GONE);
                        searchWithFilter();
                    }).setSelection(spinner.getCurrentIndex()));
                    filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    filterRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClicked(MySpinner spinner) {
                hideSuggestLayout();
                moreSpinner.setChecked(false);
                citySpinner.setChecked(false);
                typeSpinner.setChecked(false);
                spinner.toggle();
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
        if (child instanceof ImageView) child.setVisibility(View.GONE);
        baiduMap.setOnMapLoadedCallback(() -> {
            mapView.showScaleControl(false);
            mapView.showZoomControls(false);
        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        initMapStyle();
    }

    void initMapStyle() {
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

    void locateLocation(GeoCoding geoCoding) {
        if (geoCoding != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            LatLng latLng = geoCoding.getLocation().toLatLng();
            builder.target(latLng).zoom(12.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    void locateMyLocation(OnReceiveLocationOption option) {
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
        locationClient.registerLocationListener(new MyLocationListener(option));
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.start();
    }

    void searchWithFilter() {
        BaiduRequestBuilder.SEARCH_TYPE searchType = BaiduRequestBuilder.SEARCH_TYPE.DEFAULT;
        if (distanceSpinner.getCurrentIndex() != 0)
            searchType = BaiduRequestBuilder.SEARCH_TYPE.CIRCLE;
        String tag = typeSpinner.getCurrentIndex() == 0 ? BaiduRequestBuilder.TAG_ATTRACTION : typeSpinner.getText();
        String city = citySpinner.getText();
        BaiduRequestBuilder builder = new BaiduRequestBuilder();
        String url;
        if (searchType == BaiduRequestBuilder.SEARCH_TYPE.DEFAULT)
            url = builder.type(searchType).tag(tag).region(city).output(BaiduRequestBuilder.OUTPUT_TYPE.JSON).pageSize(10).scope(BaiduRequestBuilder.SCOPE_TYPE.DETAIL).pageNum(1).build();
        else
            url = builder.type(searchType).tag(tag).output(BaiduRequestBuilder.OUTPUT_TYPE.JSON).radius(Integer.parseInt(distanceSpinner.getText().replace("km", ""))).pageSize(10).scope(BaiduRequestBuilder.SCOPE_TYPE.DETAIL).pageNum(1).build();
        Log.d("xuruida", url);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        hideSuggestLayout();
        hideFilterLayout();
        return false;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            loadIndex = 0;
            IToast.makeTextBottom(getActivity(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        List<PoiInfo> poiInfos = poiResult.getAllPoi();
        if (null == poiInfos) return;
        setPoiResult(poiInfos);
        hideSuggestLayout();
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
        if (suggesInfos.size() > 0 && !itemClicked && !TextUtils.isEmpty(searchInput.getText().toString()))
            sugRecyclerView.setVisibility(View.VISIBLE);
        else hideSuggestLayout();
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
        Log.d("xuruida", "Click Map");
        Log.d("xuruida", MapRequest.getReverseGeoCoding(latLng).toString());
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideSuggestLayout();
        hideFilterLayout();
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideSuggestLayout();
        hideFilterLayout();
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
        hideSuggestLayout();
        hideFilterLayout();
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
        if (null == sugRecyclerView || null == mapView) return;
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideSuggestLayout();
        hideFilterLayout();
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
        String cityStr = citySpinner.getText();
        String keyWordStr = searchInput.getText().toString();
        if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) return;
        hidePoiInfoLayout();
        clearData();
        poiSearch.searchInCity((new PoiCitySearchOption()).city(cityStr).keyword(keyWordStr).pageNum(loadIndex).cityLimit(true).scope(1));
    }

    private void setPoiResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0) return;
        clearData();
        LatLng latLng = poiInfos.get(0).getLocation();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(latLng).zoom(12.0f).build()));
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
        hidePoiInfoLayout();
        hideSuggestLayout();
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
        hideSuggestLayout();
        if (view == mainView.findViewById(R.id.goto_mylocation)) {
            locateMyLocation(OnReceiveLocationOption.JUMPTO);
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

    private void updateCitySpinner(@NonNull BDLocation location) {
        String coorType = location.getCoorType();
        String addr = location.getAddrStr();
        String country = location.getCountry();
        String province = location.getProvince();
        String city = location.getCity();
        String district = location.getDistrict();
        String street = location.getStreet();
        String adcode = location.getAdCode();
        String town = location.getTown();
        citySpinner.setText(district);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideFilterLayout();
        hideSuggestLayout();
    }

    void hideFilterLayout() {
        citySpinner.setChecked(false);
        typeSpinner.setChecked(false);
        moreSpinner.setChecked(false);
        distanceSpinner.setChecked(false);
    }

    void hideSuggestLayout() {
        sugRecyclerView.setVisibility(View.GONE);
    }

    private enum OnReceiveLocationOption {
        NONE, JUMPTO, UPDATESPINNER, JUMPTO_UPDATESPINNER, SELECT_CITY
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        OnReceiveLocationOption option;

        MyLocationListener(OnReceiveLocationOption option) {
            this.option = option;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) return;
            int errorCode = location.getLocType();
            float radius = location.getRadius();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (errorCode == BDLocation.TYPE_NO_PERMISSION_LOCATION_FAIL)
                IToast.makeTextBottom(getActivity(), "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                myLocation = location;
                if (option == OnReceiveLocationOption.JUMPTO) {
                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                    clearData();
                    hideSuggestLayout();
                } else if (option == OnReceiveLocationOption.UPDATESPINNER) {
                    updateCitySpinner(location);
                } else if (option == OnReceiveLocationOption.JUMPTO_UPDATESPINNER) {
                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                    updateCitySpinner(location);
                    clearData();
                    hideSuggestLayout();
                    searchWithFilter();
                } else if (option == OnReceiveLocationOption.SELECT_CITY) {
                    launcher.launch(location.getDistrict());
                    clearData();
                    hideSuggestLayout();
                }
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
            if (s.length() <= 0 && View.VISIBLE == sugRecyclerView.getVisibility()) {
                sugRecyclerView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            itemClicked = false;
            String cityStr = citySpinner.getText();
            String keyWordStr = searchInput.getText().toString();
            if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) return;
            if (View.VISIBLE == sugRecyclerView.getVisibility())
                sugRecyclerView.setVisibility(View.INVISIBLE);
            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(cityStr).keyword(keyWordStr).citylimit(true));
        }
    }

    class SelectCityContract extends ActivityResultContract<String, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent intent = new Intent(context, SelectCityActivity.class);
            intent.putExtra("current", input);
            return intent;
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            return intent == null ? null : intent.getStringExtra("selection");
        }
    }
}
