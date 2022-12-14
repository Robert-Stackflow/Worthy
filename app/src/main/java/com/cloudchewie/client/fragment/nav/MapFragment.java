/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.fragment.nav;

import static android.content.Context.SENSOR_SERVICE;
import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_DARK;
import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_TEA;
import static com.cloudchewie.client.util.map.MapUtil.DARK_ID;
import static com.cloudchewie.client.util.map.MapUtil.LIGHT_ID;
import static com.cloudchewie.client.util.map.MapUtil.getCustomStyleFilePath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.baidu.mapapi.map.BitmapDescriptor;
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
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.map.SelectCityActivity;
import com.cloudchewie.client.adapter.SpinnerAdapter;
import com.cloudchewie.client.adapter.SuggestListAdapter;
import com.cloudchewie.client.api.cluster.clustering.ClusterItem;
import com.cloudchewie.client.api.cluster.clustering.ClusterManager;
import com.cloudchewie.client.api.overlay.OverlayManager;
import com.cloudchewie.client.api.overlay.PoiOverlay;
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

public class MapFragment extends Fragment implements SensorEventListener, View.OnClickListener, View.OnTouchListener, OnGetGeoCoderResultListener, OnGetPoiSearchResultListener, BaiduMap.OnMapStatusChangeListener, OnGetSuggestionResultListener, BaiduMap.OnMapTouchListener, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    //BaiduMap
    private MapView mapView;
    private BaiduMap baiduMap;
    private GeoCoder geoCoder;
    private BDLocation myLocation;
    private Marker preSelectMarker;
    private PoiSearch poiSearchAround;
    private PoiSearch poiSearchInCity;
    private SensorManager sensorManager;
    private LocationClient locationClient;
    private SuggestionSearch suggestionSearch;
    private LocationClientOption locationOption;
    private ClusterManager<MyClusterItem> clusterManager;
    //RecyclerView
    private RecyclerView sugRecyclerView;
    private RecyclerView poiRecyclerView;
    private RecyclerView filterRecyclerView;
    private SuggestListAdapter suggestListAdapter;
    //????????????
    private View mainView;
    private TextView poiTitle;
    private TextView poiAddress;
    private EditText searchInput;
    private MySpinner citySpinner;
    private MySpinner typeSpinner;
    private MySpinner moreSpinner;
    private SearchLayout searchLayout;
    private ConstraintLayout titleBar;
    private MySpinner distanceSpinner;
    private ImageButton gotoMyLocation;
    private LinearLayout layoutDetailInfo;
    //??????
    private LatLng centroid;
    private double lastX = 0;
    private int loadIndex = 0;
    private boolean itemClicked = false;
    private ActivityResultLauncher launcher;
    private HashMap<Marker, PoiInfo> searchAroundMarkers = new HashMap<>();
    private HashMap<Marker, PoiInfo> searchInCityMarkers = new HashMap<>();
    private searchTextWatcher searchTextWatcher = new searchTextWatcher();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_map, null);
        bindView();
        initView();
        initRecyclerView();
        initSpinner();
        initMap();
        initSearchAround();
        initMapStyle();
        locateMyLocation(OnReceiveLocationOption.JUMPTO_UPDATESPINNER);
        return mainView;
    }

    void bindView() {
        mapView = mainView.findViewById(R.id.map_view);
        titleBar = mainView.findViewById(R.id.map_titlebar);
        layoutDetailInfo = mainView.findViewById(R.id.poiInfo);
        poiTitle = mainView.findViewById(R.id.poiTitle);
        poiAddress = mainView.findViewById(R.id.poiAddress);
        searchLayout = mainView.findViewById(R.id.map_search_layout);
        sugRecyclerView = mainView.findViewById(R.id.map_sug_recyclerview);
        poiRecyclerView = mainView.findViewById(R.id.map_poi_recyclerview);
        filterRecyclerView = mainView.findViewById(R.id.map_search_filter_recyclerview);
        citySpinner = mainView.findViewById(R.id.map_search_filter_city);
        typeSpinner = mainView.findViewById(R.id.map_search_filter_type);
        moreSpinner = mainView.findViewById(R.id.map_search_filter_more);
        distanceSpinner = mainView.findViewById(R.id.map_search_filter_distance);
        gotoMyLocation = mainView.findViewById(R.id.goto_mylocation);
        searchInput = searchLayout.getSearchEdit();
    }

    void initView() {
        StatusBarUtil.setStatusBarMarginTop(titleBar, 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        launcher = registerForActivityResult(new SelectCityContract(), result -> {
            if (result != null) citySpinner.setText(result);
            if (geoCoder == null) {
                geoCoder = GeoCoder.newInstance();
                geoCoder.setOnGetGeoCodeResultListener(this);
            }
            geoCoder.geocode(new GeoCodeOption().city(citySpinner.getText()).address(citySpinner.getText()));
        });
        gotoMyLocation.setOnClickListener(this);
        searchLayout.setOnTextSearchListener(s -> {
            if (TextUtils.isEmpty(s)) hideSuggestLayout();
            return null;
        }, s -> {
            searchInCity();
            return null;
        });
        if (searchInput == null) searchInput = searchLayout.getSearchEdit();
        searchInput.addTextChangedListener(new searchTextWatcher());
        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) hideFilterLayout();
        });
    }

    void initRecyclerView() {
        suggestListAdapter = new SuggestListAdapter();
        suggestListAdapter.setOnItemClickListener((view, position, suggestInfo) -> {
            locateSuggestionInfo(suggestInfo);
            setSearchTextWithSuggestInfo(suggestInfo);
        });
        if (null == sugRecyclerView) return;
        sugRecyclerView.setAdapter(suggestListAdapter);
        sugRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sugRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KeyBoardUtil.hideKeyBoard(getActivity());
            }
        });
        hideSuggestLayout();
    }

    void initSpinner() {
        typeSpinner.setEntry(Arrays.asList(getResources().getStringArray(R.array.poi_filter_type)));
        distanceSpinner.setEntry(Arrays.asList(getResources().getStringArray(R.array.poi_filter_distance)));
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
                if (isChecked) {
                    filterRecyclerView.setVisibility(View.VISIBLE);
                    filterRecyclerView.setAdapter(new SpinnerAdapter(getContext(), spinner.getDataSource()).setOnItemClickListener(position -> {
                        spinner.setCurrentIndex(position);
                        spinner.setChecked(false);
                        filterRecyclerView.setVisibility(View.GONE);
                        clearData();
                        searchAround();
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
                    filterRecyclerView.setAdapter(new SpinnerAdapter(getContext(), spinner.getDataSource()).setOnItemClickListener(position -> {
                        spinner.setCurrentIndex(position);
                        spinner.setChecked(false);
                        filterRecyclerView.setVisibility(View.GONE);
                        clearData();
                        searchAround();
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
        baiduMap.setOnMapStatusChangeListener(this);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        configuration.accuracyCircleFillColor = 0x00000000;
        configuration.accuracyCircleStrokeColor = 0x00000000;
        baiduMap.setMyLocationConfiguration(configuration);
        poiSearchInCity = PoiSearch.newInstance();
        poiSearchInCity.setOnGetPoiSearchResultListener(this);
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        baiduMap.setOnMapLoadedCallback(() -> {
            mapView.showScaleControl(false);
            mapView.showZoomControls(false);
            MapUtil.hideLogo(mapView);
        });
        clusterManager = new ClusterManager<>(getContext(), baiduMap);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    void initSearchAround() {
        poiSearchAround = PoiSearch.newInstance();
        poiSearchAround.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    loadIndex = 0;
                    return;
                }
                setSearchAroundResult(poiResult.getAllPoi());
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
        });
    }

    void initMapStyle() {
        if (mapView == null) return;
        MapCustomStyleOptions mapCustomStyleOptions = new MapCustomStyleOptions();
        mapCustomStyleOptions.customStyleId(DarkModeUtil.isDarkMode(getContext()) ? DARK_ID : LIGHT_ID);
        mapCustomStyleOptions.localCustomStylePath(getCustomStyleFilePath(getActivity(), DarkModeUtil.isDarkMode(getContext()) ? CUSTOM_FILE_NAME_DARK : CUSTOM_FILE_NAME_TEA));
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

    void locateLocation(LatLng latLng) {
        if (latLng != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(14.0f);
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

    void searchAround() {
        String tag = typeSpinner.getCurrentIndex() == 0 ? BaiduRequestBuilder.TAG_ATTRACTION : typeSpinner.getText();
        String city = citySpinner.getText();
        if (distanceSpinner.getCurrentIndex() != 0) {
            if (centroid != null)
                poiSearchAround.searchNearby(new PoiNearbySearchOption().location(centroid).tag(tag).pageCapacity(100).radius(Integer.parseInt(distanceSpinner.getText().replace("km", "")) * 1000).keyword(tag).scope(1));
        } else {
            if (centroid != null)
                poiSearchAround.searchNearby(new PoiNearbySearchOption().location(centroid).tag(tag).pageCapacity(100).radius(1000000).keyword(tag).scope(1));
        }
    }

    private void searchInCity() {
        String city = citySpinner.getText();
        String key = searchInput.getText().toString();
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(key)) return;
        poiSearchInCity.searchInCity((new PoiCitySearchOption()).city(city).keyword(key).pageNum(loadIndex).cityLimit(true).scope(1));
        hidePoiInfoLayout();
        clearData();
    }

    private void searchDetail() {
        String city = citySpinner.getText();
        String key = searchInput.getText().toString();
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(key)) return;
        hidePoiInfoLayout();
        clearData();
        poiSearchInCity.searchPoiDetail((new PoiDetailSearchOption()));
    }

    private void setSearchTextWithSuggestInfo(SuggestionResult.SuggestionInfo suggestionInfo) {
        if (null == suggestionInfo) return;
        searchInput.removeTextChangedListener(searchTextWatcher);
        searchInput.setText(suggestionInfo.getKey());
        searchInput.setSelection(suggestionInfo.getKey().length());
        searchInput.addTextChangedListener(searchTextWatcher);
        itemClicked = true;
    }

    /**
     * ????????????????????????
     *
     * @param suggestInfo
     */
    private void locateSuggestionInfo(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo) return;
        if (null == sugRecyclerView || null == mapView) return;
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideSuggestLayout();
        hideFilterLayout();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(suggestInfo.getPt()).zoom(baiduMap.getMapStatus().zoom).build()));
        clearData();
        if (!showSuggestMarker(suggestInfo)) {
            setSearchTextWithSuggestInfo(suggestInfo);
            searchInCity();
        }
    }

    /**
     * ????????????????????????Marker
     *
     * @param suggestionInfo
     */
    @Contract("null -> false")
    private boolean showSuggestMarker(SuggestionResult.SuggestionInfo suggestionInfo) {
        if (null == suggestionInfo) return false;
        baiduMap.addOverlay(new MarkerOptions().position(suggestionInfo.getPt()).icon(MapUtil.getMarkerIcon(0)));
        return true;
    }

    /**
     * ????????????????????????
     *
     * @param poiInfos
     */
    private void setSearchInCityResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0) return;
        clearData();
        LatLng latLng = poiInfos.get(0).getLocation();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(latLng).zoom(baiduMap.getMapStatus().zoom).build()));
        Iterator<PoiInfo> itr = poiInfos.iterator();
        List<LatLng> latLngs = new ArrayList<>();
        PoiInfo poiInfo;
        int i = 0;
        while (itr.hasNext()) {
            poiInfo = itr.next();
            if (null == poiInfo) continue;
            showSearchInCityMarker(poiInfo, i);
            latLngs.add(poiInfo.getLocation());
            if (0 == i) showPoiInfoLayout(poiInfo);
            i++;
        }
        setBounds(latLngs);
    }

    private void setSearchAroundResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0) return;
        List<LatLng> latLngs = new ArrayList<>();
        int i = 0;
        for (PoiInfo poi : poiInfos) {
            if (null == poi) continue;
            showSearchAroundMarker(poi, i);
            latLngs.add(poi.getLocation());
            i++;
        }
    }

    /**
     * ????????????????????????
     *
     * @param poiInfo
     * @param i
     */
    private void locatePoiInfo(PoiInfo poiInfo, int i) {
        if (null == poiInfo) return;
        KeyBoardUtil.hideKeyBoard(getActivity());
        showSearchInCityMarker(poiInfo, i);
    }

    /**
     * ????????????????????????Marker
     *
     * @param poiInfo
     * @param i
     */
    private void showSearchInCityMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) return;
        Marker existedMarker = isMarkerExisted(poiInfo);
        if (existedMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(poiInfo.getLocation()).icon(MapUtil.getMarkerIcon(0)).perspective(true).isJoinCollision(true);
//            OverlayOptions textOptions = new TextOptions().text(poiInfo.getName()).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(poiInfo.getLocation());
//            baiduMap.addOverlay(textOptions);
            Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
            if (null != marker) {
                searchInCityMarkers.put(marker, poiInfo);
                if (clusterManager != null)
                    clusterManager.addItem(new MyClusterItem(poiInfo.location));
                if (i == 0) preSelectMarker = marker;
            }
        }
    }

    /**
     * ????????????????????????Marker
     *
     * @param poiInfo
     * @param i
     */
    private void showSearchAroundMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) return;
        Marker existedMarker = isMarkerExisted(poiInfo);
        if (existedMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(poiInfo.getLocation()).icon(MapUtil.getMarkerIcon(0)).perspective(true).isJoinCollision(true);
            //            OverlayOptions textOptions = new TextOptions().text(poiInfo.getName()).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(poiInfo.getLocation());
            //            baiduMap.addOverlay(textOptions);
            Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
            OverlayManager manager = new PoiOverlay(baiduMap);
            manager.addToMap();
            if (null != marker) {
                searchAroundMarkers.put(marker, poiInfo);
                if (clusterManager != null)
                    clusterManager.addItem(new MyClusterItem(poiInfo.location));
                if (i == 0) preSelectMarker = marker;
            }
        }
    }

    Marker isMarkerExisted(PoiInfo poiInfo) {
        if (null == poiInfo) return null;
        for (Map.Entry<Marker, PoiInfo> entry : searchInCityMarkers.entrySet())
            if (Objects.equals(entry.getValue().uid, poiInfo.uid)) return entry.getKey();
        for (Map.Entry<Marker, PoiInfo> entry : searchAroundMarkers.entrySet())
            if (Objects.equals(entry.getValue().uid, poiInfo.uid)) return entry.getKey();
        return null;
    }

    /**
     * ??????POI??????Poi?????????
     *
     * @param poiInfo
     * @return
     */
    @NonNull
    @Contract("_ -> new")
    @SuppressLint("UseCompatLoadingForDrawables")
    private InfoWindow getPoiInfoWindow(@NonNull PoiInfo poiInfo) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_infowindow, null);
        ((TextView) view.findViewById(R.id.infowindow_title)).setText(poiInfo.getName());
        ((TextView) view.findViewById(R.id.infowindow_address)).setText(poiInfo.getAddress());
        view.setClickable(true);
        return new InfoWindow(view, poiInfo.getLocation(), -150);
    }

    /**
     * ??????Poi????????????
     *
     * @param poiInfo
     */
    @Contract(pure = true)
    private void showPoiInfoLayout(PoiInfo poiInfo) {
//        if (null == layoutDetailInfo || null == poiInfo) {
//            return;
//        }
//        if (null == poiTitle) {
//            return;
//        }
//        if (null == poiAddress) {
//            return;
//        }
//        layoutDetailInfo.setVisibility(View.VISIBLE);
//        poiTitle.setText(poiInfo.getName());
//        String address = poiInfo.getAddress();
//        if (TextUtils.isEmpty(address)) {
//            poiAddress.setVisibility(View.GONE);
//        } else {
//            poiAddress.setText(poiInfo.getAddress());
//            poiAddress.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * ??????Poi????????????
     */
    private void hidePoiInfoLayout() {
        if (null == layoutDetailInfo) return;
        layoutDetailInfo.setVisibility(View.GONE);
    }

    /**
     * ???????????????????????????????????????
     */
    private void clearData() {
        baiduMap.clear();
        hidePoiInfoLayout();
        hideSuggestLayout();
        searchAroundMarkers.clear();
        preSelectMarker = null;
        clusterManager.clearItems();
    }

    /**
     * ????????????????????????
     *
     * @param latLngs
     */
    private void setBounds(List<LatLng> latLngs) {
        if (null == latLngs || latLngs.size() <= 0) return;
        int horizontalPadding = 80;
        int verticalPaddingBottom = 400;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLngs);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(), horizontalPadding, verticalPaddingBottom, horizontalPadding, verticalPaddingBottom);
        baiduMap.animateMapStatus(mapStatusUpdate);
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
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        if (null != geoCoder) geoCoder.destroy();
        if (locationClient != null) locationClient.stop();
        if (baiduMap != null) baiduMap.setMyLocationEnabled(false);
        if (sensorManager != null) sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    private void updateCitySpinner(@NonNull BDLocation location) {
        citySpinner.setText(location.getDistrict());
    }

    private void updateCitySpinner(@NonNull ReverseGeoCodeResult.AddressComponent addressComponent) {
        if (addressComponent.city != null) citySpinner.setText(addressComponent.city);
        if (addressComponent.district != null) citySpinner.setText(addressComponent.district);
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
            IToast.makeTextBottom(getActivity(), "??????" + citySpinner.getText() + "????????????", Toast.LENGTH_LONG).show();
            return;
        }
        List<PoiInfo> poiInfos = poiResult.getAllPoi();
        if (null == poiInfos) return;
        setSearchInCityResult(poiInfos);
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
                setSearchTextWithSuggestInfo(suggestInfo);
            });
        } else suggestListAdapter.updateData(suggesInfos);
        suggestListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        KeyBoardUtil.hideKeyBoard(getActivity());
        hideSuggestLayout();
        hideFilterLayout();
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        IToast.makeTextBottom(getContext(), mapPoi.getName(), Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        KeyBoardUtil.hideKeyBoard(getActivity());
        if (null == marker || (null == searchInCityMarkers && null == searchAroundMarkers))
            return false;
        PoiInfo poiInfo = null;
        if (searchAroundMarkers != null) {
            for (Map.Entry<Marker, PoiInfo> entry : searchAroundMarkers.entrySet()) {
                Marker temp = entry.getKey();
                if (null == temp) continue;
                if (Objects.equals(temp.getId(), marker.getId())) {
                    poiInfo = entry.getValue();
                    break;
                }
            }
        }
        if (searchInCityMarkers != null) {
            for (Map.Entry<Marker, PoiInfo> entry : searchInCityMarkers.entrySet()) {
                Marker temp = entry.getKey();
                if (null == temp) continue;
                if (Objects.equals(temp.getId(), marker.getId())) {
                    poiInfo = entry.getValue();
                    break;
                }
            }
        }
        if (null == poiInfo) return false;
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(poiInfo.getLocation()).zoom(baiduMap.getMapStatus().zoom).build()));
        baiduMap.showInfoWindow(getPoiInfoWindow(poiInfo));
        showPoiInfoLayout(poiInfo);
        if (null != preSelectMarker) {
            preSelectMarker.setForceDisplay(false);
            preSelectMarker.setIcon(MapUtil.getMarkerIcon(0));
        }
        marker.setIcon(MapUtil.getMarkerIcon(1));
        marker.setForceDisplay(true);
        preSelectMarker = marker;
        hideSuggestLayout();
        hideFilterLayout();
        return true;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (mapStatus == null) return;
        if (centroid == null || Math.abs(mapStatus.target.longitude - centroid.longitude) + Math.abs(mapStatus.target.latitude - centroid.latitude) > 0.002) {
            centroid = mapStatus.target;
            searchAround();
            centroid = mapStatus.target;
            if (geoCoder == null) {
                geoCoder = GeoCoder.newInstance();
                geoCoder.setOnGetGeoCodeResultListener(this);
            }
            if (geoCoder != null && centroid != null)
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(centroid));
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (null != geoCodeResult && null != geoCodeResult.getLocation())
            if (geoCodeResult != null && geoCodeResult.error == SearchResult.ERRORNO.NO_ERROR)
                locateLocation(geoCodeResult.getLocation());
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult != null && reverseGeoCodeResult.getLocation() != null)
            updateCitySpinner(reverseGeoCodeResult.getAddressDetail());
    }

    @Override
    public void onSensorChanged(@NonNull SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (myLocation != null && baiduMap != null) if (Math.abs(x - lastX) > 1.0)
            baiduMap.setMyLocationData(new MyLocationData.Builder().accuracy(myLocation.getRadius()).direction((int) x).latitude(myLocation.getLatitude()).longitude(myLocation.getLongitude()).build());
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                IToast.makeTextBottom(getActivity(), "????????????????????????????????????!", Toast.LENGTH_SHORT).show();
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
                    searchAround();
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

    private class searchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) hideSuggestLayout();
        }

        @Override
        public void afterTextChanged(Editable s) {
            itemClicked = false;
            String city = citySpinner.getText();
            String key = searchInput.getText().toString();
            if (TextUtils.isEmpty(city) || TextUtils.isEmpty(key)) return;
            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(key).citylimit(true));
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

    public class MyClusterItem implements ClusterItem {
        LatLng mPosition;

        public MyClusterItem(LatLng position) {
            mPosition = position;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return MapUtil.getMarkerIcon(0);
        }
    }
}
