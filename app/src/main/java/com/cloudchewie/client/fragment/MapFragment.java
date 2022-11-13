package com.cloudchewie.client.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
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
import com.cloudchewie.client.adapter.PoiItemAdapter;
import com.cloudchewie.client.maputil.Utils;
import com.cloudchewie.client.util.StatusBarUtil;
import com.cloudchewie.search.SearchLayout;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    View mainView;
    MapView mapView;
    BaiduMap baiduMap;
    SearchLayout searchLayout;
    EditText searchInput;
    private PoiSearch poiSearch;
    LocationClient locationClient;
    private SuggestionSearch suggestionSearch;
    private RecyclerView recyclerView;
    LocationClientOption locationOption;
    private PoiItemAdapter poiItemAdaper;
    private BitmapDescriptor bitmapdescwaterdrop = BitmapDescriptorFactory.fromResource(R.drawable.water_drop);
    private int loadIndex = 0;
    private TextView poiTitle;
    private TextView poiAddress;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner countySpinner;
    private Marker preSelectMarker;
    private LinearLayout layoutDetailInfo;
    int province_postion = 0;
    int city_postion = 0;
    int county_poisition = 0;
    String seletced_province;
    String seletced_county;
    String seletced_city;
    ArrayAdapter<String> adapter_province;
    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_county;
    private HashMap<Marker, PoiInfo> markerPoiInfo = new HashMap<>();
    private MyTextWatcher myTextWatcher = new MyTextWatcher();
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_blacknight.sty";
    private static final String CUSTOM_FILE_NAME_WHITE = "custom_trip.sty";
    String[] provinces = {"北京市", "天津市", "上海市", "广东省", "河南省", "重庆市", "河北省", "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省",
            "江西省", "山东省", "湖北省", "湖南省", "海南省", "四川省", "贵州省", "云南省", "陕西省", "甘肃省", "青海省", "台湾省"};
    String[][] cities = new String[][]{
            {"北京市"},
            {"天津市"},
            {"上海市"},
            {"广州", "深圳", "韶关", "珠海", "汕头", "佛山", "湛江", "肇庆", "江门", "茂名", "惠州", "梅州", "汕尾", "河源", "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮"},
            {"郑州市", "开封市", "洛阳市", "平顶山市", "许昌市"},
    };
    String[][][] counties = new String[][][]{
            {
                    {"东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区", "房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县", "延庆县"}
            },
            {
                    {"和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区", "东丽区"}
            },
            {
                    {"长宁区", "静安区", "普陀区", "闸北区", "虹口区"}
            },
            {
                    {"海珠区", "荔湾区", "越秀区", "白云区", "萝岗区", "天河区", "黄埔区", "花都区", "从化市", "增城市", "番禺区", "南沙区"},
                    {"宝安区", "福田区", "龙岗区", "罗湖区", "南山区", "盐田区"},
                    {"武江区", "浈江区", "曲江区", "乐昌市", "南雄市", "始兴县", "仁化县", "翁源县", "新丰县", "乳源县"},
                    {"无"}
            },
            {
                    {"中原区", "二七区", "管城区", "金水区", "上街区", "惠济区", "巩义市", "荥阳市", "新密市", "新郑市", "登封市", "中牟县"},
                    {"鼓楼区", "龙亭区", "顺河区", "禹王台", "金明区", "杞县", "通许县", "尉氏县", "开封县", "兰考县"},
                    {"西工区", "老城区", "瀍河区", "涧西区", "吉利区", "洛龙区", "偃师市", "孟津县", "新安县", "栾川县", "嵩县", "汝阳县", "宜阳县", "洛宁县", "伊川县"},
                    {"新华区", "卫东区", "湛河区", "石龙区", "舞钢市", "汝州市", "宝丰县", "叶 县", "鲁山县", "郏县"},
                    {"魏都区", "禹州市", "长葛市", "许昌县", "鄢陵县", "襄城县"}
            },
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_map, null);
        mainView.findViewById(R.id.goto_mylocation).setOnClickListener(this);
        StatusBarUtil.setMargin(mainView.findViewById(R.id.map_titlebar), 0, StatusBarUtil.getHeight(getActivity()), 0, 0);
        initMap();
        locateMyLocation();
        initSpinner();
        return mainView;
    }

    void initMap() {
        mapView = mainView.findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);
        searchLayout = mainView.findViewById(R.id.map_search_layout);
        searchInput = searchLayout.getSearchEdit();
        searchInput.addTextChangedListener(new MyTextWatcher());
        recyclerView = mainView.findViewById(R.id.map_sug_recyclerview);
        layoutDetailInfo = mainView.findViewById(R.id.poiInfo);
        poiTitle = mainView.findViewById(R.id.poiTitle);
        poiAddress = mainView.findViewById(R.id.poiAddress);
        poiItemAdaper = new PoiItemAdapter();
        provinceSpinner = mainView.findViewById(R.id.map_search_province_filter);
        citySpinner = mainView.findViewById(R.id.map_search_city_filter);
        countySpinner = mainView.findViewById(R.id.map_search_county_filter);
        searchLayout.setOnTextSearchListener(s -> null, s -> {
            searchPoiInCity();
            return null;
        });
        poiItemAdaper.setOnItemClickListener((parent, view, position, id) -> {
            SuggestionResult.SuggestionInfo suggestInfo =
                    poiItemAdaper.getItemSuggestInfo(position);
            locateSuggestPoi(suggestInfo);
            setPoiTextWithLocateSuggestInfo(suggestInfo);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        if (null == recyclerView)
            return;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(poiItemAdaper);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Utils.hideKeyBoard(getActivity());
            }
        });
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
        String customStyleFilePath = getCustomStyleFilePath(getActivity(), CUSTOM_FILE_NAME_GRAY);
        mapView.setMapCustomStylePath(customStyleFilePath);
        mapView.setMapCustomStyleEnable(false);
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
    }

    void initSpinner() {
        adapter_province = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, provinces);
        adapter_city = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities[0]);
        adapter_county = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, counties[0][0]);
        provinceSpinner.setAdapter(adapter_province);
        provinceSpinner.setSelection(0);
        citySpinner.setAdapter(adapter_city);
        citySpinner.setSelection(0);
        countySpinner.setAdapter(adapter_county);
        countySpinner.setSelection(0);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                province_postion = i;
                adapter_city = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities[i]);
                citySpinner.setAdapter(adapter_city);
                seletced_province = provinces[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_postion = i;
                adapter_county = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, counties[province_postion][city_postion]);
                countySpinner.setAdapter(adapter_county);
                seletced_city = cities[province_postion][i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletced_county = counties[province_postion][city_postion][i];
                Log.d("xuruida", seletced_province + "-" + seletced_city + "-" + seletced_county);
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

    @NonNull
    private String getCustomStyleFilePath(@NonNull Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;
        try {
            inputStream = context.getAssets().open(customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists())
                customStyleFile.delete();
            customStyleFile.createNewFile();
            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parentPath + "/" + customStyleFileName;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        recyclerView.setVisibility(View.GONE);
        return false;
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            int errorCode = location.getLocType();
            float radius = location.getRadius();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String coorType = location.getCoorType();
            if (errorCode == BDLocation.TYPE_NO_PERMISSION_LOCATION_FAIL)
                Toast.makeText(getActivity(), "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                locationOption.setScanSpan(0);
                locationClient.stop();
                locationClient.setLocOption(locationOption);
            }
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            loadIndex = 0;
            Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        List<PoiInfo> poiInfos = poiResult.getAllPoi();
        if (null == poiInfos)
            return;
        recyclerView.setVisibility(View.GONE);
        setPoiResult(poiInfos);
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
            Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        List<SuggestionResult.SuggestionInfo> suggesInfos = suggestionResult.getAllSuggestions();
        if (null == suggesInfos)
            return;
        hideInfoLayout();
        recyclerView.setVisibility(View.VISIBLE);
        if (null == poiItemAdaper)
            poiItemAdaper = new PoiItemAdapter(suggesInfos);
        else
            poiItemAdaper.updateData(suggesInfos);
        poiItemAdaper.notifyDataSetChanged();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Utils.hideKeyBoard(getActivity());
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null == marker || null == markerPoiInfo || markerPoiInfo.size() <= 0)
            return false;
        Iterator<Map.Entry<Marker, PoiInfo>> itr = markerPoiInfo.entrySet().iterator();
        Marker tmpMarker;
        PoiInfo poiInfo = null;
        Map.Entry<Marker, PoiInfo> markerPoiInfoEntry;
        while (itr.hasNext()) {
            markerPoiInfoEntry = itr.next();
            tmpMarker = markerPoiInfoEntry.getKey();
            if (null == tmpMarker) {
                continue;
            }
            if (Objects.equals(tmpMarker.getId(), marker.getId())) {
                poiInfo = markerPoiInfoEntry.getValue();
                break;
            }
        }
        if (null == poiInfo)
            return false;
        InfoWindow infoWindow = getPoiInfoWindow(poiInfo);
        LatLng center = poiInfo.getLocation();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 12);
        baiduMap.setMapStatus(mapStatusUpdate);
        baiduMap.showInfoWindow(infoWindow);
        showPoiInfoLayout(poiInfo);
        if (null != preSelectMarker) {
            preSelectMarker.setScale(1.0f);
        }
        marker.setScale(2f);
        preSelectMarker = marker;
        return true;
    }

    private void setPoiTextWithLocateSuggestInfo(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo)
            return;
        searchInput.removeTextChangedListener(myTextWatcher);
        searchInput.setText(suggestInfo.getKey());
        searchInput.setSelection(suggestInfo.getKey().length());
        searchInput.addTextChangedListener(myTextWatcher);
    }

    private void locateSuggestPoi(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo)
            return;
        if (null == recyclerView || null == mapView)
            return;
        recyclerView.setVisibility(View.INVISIBLE);
        LatLng latLng = suggestInfo.getPt();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(mapStatusUpdate);
        Utils.hideKeyBoard(getActivity());
        clearData();
        if (showSuggestMarker(latLng)) {
            showPoiInfoLayout(suggestInfo);
        } else {
            setPoiTextWithLocateSuggestInfo(suggestInfo);
            searchPoiInCity();
        }
    }

    private void searchPoiInCity() {
        String cityStr = citySpinner.getSelectedItem().toString();
        String keyWordStr = searchInput.getText().toString();
        if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr))
            return;
        if (View.VISIBLE == recyclerView.getVisibility())
            recyclerView.setVisibility(View.INVISIBLE);
        poiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityStr)
                .keyword(keyWordStr)
                .pageNum(loadIndex)
                .cityLimit(true)
                .scope(1));
    }

    private void setPoiResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0)
            return;
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
            if (null == poiInfo)
                continue;
            locatePoiInfo(poiInfo, i);
            latLngs.add(poiInfo.getLocation());
            if (0 == i)
                showPoiInfoLayout(poiInfo);
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
        Utils.hideKeyBoard(getActivity());
        showPoiMarker(poiInfo, i);
    }

    private void showPoiMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) {
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(poiInfo.getLocation())
                .icon(bitmapdescwaterdrop);
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
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_infowindow, null);
        ((TextView) view.findViewById(R.id.infowindow_title)).setText(poiInfo.getName());
        return new InfoWindow(view, poiInfo.getLocation(), -150);
    }

    private boolean showSuggestMarker(LatLng latLng) {
        if (null == latLng)
            return false;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(bitmapdescwaterdrop)
                .scaleX(2f)
                .scaleY(2f);
        baiduMap.addOverlay(markerOptions);
        return true;
    }

    private void showPoiInfoLayout(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == layoutDetailInfo || null == suggestInfo) {
            return;
        }
        if (null == poiTitle) {
            return;
        }
        if (null == poiAddress) {
            return;
        }
        layoutDetailInfo.setVisibility(View.VISIBLE);
        poiTitle.setText(suggestInfo.getKey());
        String address = suggestInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            poiAddress.setVisibility(View.GONE);
        } else {
            poiAddress.setText(suggestInfo.getAddress());
            poiAddress.setVisibility(View.VISIBLE);
        }
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

    private void hideInfoLayout() {
        if (null == layoutDetailInfo)
            return;
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
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(),
                horizontalPadding,
                verticalPaddingBottom,
                horizontalPadding,
                verticalPaddingBottom);
        baiduMap.setMapStatus(mapStatusUpdate);
        baiduMap.setViewPadding(0,
                0,
                0,
                verticalPaddingBottom);
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
            String cityStr = citySpinner.getSelectedItem().toString();
            String keyWordStr = searchInput.getText().toString();
            if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) {
                return;
            }
            if (View.VISIBLE == recyclerView.getVisibility())
                recyclerView.setVisibility(View.INVISIBLE);
            suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city(cityStr)
                    .keyword(keyWordStr)
                    .citylimit(true));
        }
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
        mapView.onDestroy();
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
//      mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
}
