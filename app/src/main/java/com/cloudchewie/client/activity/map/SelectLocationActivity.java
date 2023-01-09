package com.cloudchewie.client.activity.map;

import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_DARK;
import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_TEA;
import static com.cloudchewie.client.util.map.MapUtil.DARK_ID;
import static com.cloudchewie.client.util.map.MapUtil.LIGHT_ID;
import static com.cloudchewie.client.util.map.MapUtil.getCustomStyleFilePath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CustomMapStyleCallBack;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapCustomStyleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.api.overlay.OverlayManager;
import com.cloudchewie.client.api.overlay.PoiOverlay;
import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.util.map.MapUtil;
import com.cloudchewie.client.util.ui.DarkModeUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MyDialog;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectLocationActivity extends BaseActivity implements SensorEventListener, View.OnClickListener, BaiduMap.OnMarkerClickListener, OnGetPoiSearchResultListener, OnGetGeoCoderResultListener, BaiduMap.OnMarkerDragListener {
    //Map
    private MapView mapView;
    private BaiduMap baiduMap;
    private GeoCoder geoCoder;
    private SensorManager sensorManager;
    private LocationClient locationClient;
    private LocationClientOption locationOption;
    private PoiSearch poiSearchAround;
    private Marker selectedMarker;
    private Text selectedTextMarker;
    //控件
    private ImageView mBackButton;
    private ImageView mConfirmButton;
    //变量
    private LatLng centroid;
    private int loadIndex = 0;
    private boolean itemClicked = false;
    private String currentCity;
    private double lastX = 0.0;
    private BDLocation myLocation;
    private Marker preSelectMarker;
    private ReverseGeoCodeResult reverseGeoCodeResult;
    private HashMap<Marker, PoiInfo> searchAroundMarkers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        mBackButton = findViewById(R.id.activity_select_location_back);
        mConfirmButton = findViewById(R.id.activity_select_location_confirm);
        mapView = findViewById(R.id.activity_select_location_map_view);
        mBackButton.setOnClickListener(v -> finish());
        findViewById(R.id.activity_select_location_goto_mylocation).setOnClickListener(this);
        StatusBarUtil.setStatusBarMarginTop(mBackButton, 0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        initMap();
        initView();
        initMapStyle();
    }

    void initView() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        Intent intent = getIntent();
        if (intent != null) {
            Serializable object = intent.getSerializableExtra("current");
            if (object instanceof Attraction && ((Attraction) object).getLocation() != null) {
                locateLocation((Attraction) object);
            } else {
                locateMyLocation(onReceiveLocationOption.JUMPTO_MARKER);
            }
        } else {
            locateMyLocation(onReceiveLocationOption.JUMPTO_MARKER);
        }
        mConfirmButton.setOnClickListener(v -> {
            if (reverseGeoCodeResult != null) {
                MyDialog dialog = new MyDialog(SelectLocationActivity.this);
                dialog.setTitle("选择地点");
                dialog.setMessage("是否选择" + reverseGeoCodeResult.getAddress());
                dialog.setNegtive("取消");
                dialog.setPositive("确认");
                dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        Attraction attraction = new Attraction();
                        String str = reverseGeoCodeResult.getAddress();
                        if (reverseGeoCodeResult.getPoiRegionsInfoList() != null && reverseGeoCodeResult.getPoiRegionsInfoList().size() != 0) {
                            ReverseGeoCodeResult.PoiRegionsInfo region = reverseGeoCodeResult.getPoiRegionsInfoList().get(0);
                            if (region.regionName != null) str += region.regionName;
                        }
                        OverlayOptions textOptions = new TextOptions().text(str).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(reverseGeoCodeResult.getLocation());
                        selectedTextMarker = (Text) baiduMap.addOverlay(textOptions);
                        attraction.setLatitude(reverseGeoCodeResult.getLocation().latitude);
                        attraction.setLongtitude(reverseGeoCodeResult.getLocation().longitude);
                        attraction.setLocation(str);
                        attraction.setName(str);
                        bundle.putSerializable("selection", attraction);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onCloseClick() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void locateLocation(@NonNull Attraction attraction) {
        if (baiduMap == null)
            return;
        baiduMap.clear();
        LatLng latLng = new LatLng(attraction.getLatitude(), attraction.getLongtitude());
        if (latLng == null) return;
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        showSelectedMarker(latLng);
        if (attraction.getName() != null) {
            OverlayOptions textOptions = new TextOptions().text(attraction.getName()).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(latLng);
            selectedTextMarker = (Text) baiduMap.addOverlay(textOptions);
        }
    }

    void initMap() {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMarkerDragListener(this);
        baiduMap.setOnMarkerClickListener(this);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        myLocationConfiguration.accuracyCircleFillColor = 0x00000000;
        myLocationConfiguration.accuracyCircleStrokeColor = 0x00000000;
        baiduMap.setMyLocationConfiguration(myLocationConfiguration);
        MapUtil.hideLogo(mapView);
        baiduMap.setOnMapLoadedCallback(() -> {
            mapView.showScaleControl(false);
            mapView.showZoomControls(false);
        });
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        poiSearchAround = PoiSearch.newInstance();
        poiSearchAround.setOnGetPoiSearchResultListener(this);
    }

    void initMapStyle() {
        MapCustomStyleOptions mapCustomStyleOptions = new MapCustomStyleOptions();
        mapCustomStyleOptions.localCustomStylePath(getCustomStyleFilePath(this, DarkModeUtil.isDarkMode(this) ? CUSTOM_FILE_NAME_DARK : CUSTOM_FILE_NAME_TEA));
        mapCustomStyleOptions.customStyleId(DarkModeUtil.isDarkMode(this) ? DARK_ID : LIGHT_ID);
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

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.activity_select_location_goto_mylocation)) {
            locateMyLocation(onReceiveLocationOption.JUMPTO);
        }
    }

    void locateMyLocation(onReceiveLocationOption option) {
        try {
            locationClient = new LocationClient(this);
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
        if (poiSearchAround != null) poiSearchAround.destroy();
        if (geoCoder != null) geoCoder.destroy();
        if (locationClient != null) locationClient.stop();
        if (baiduMap != null) baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
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

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (null != geoCodeResult && null != geoCodeResult.getLocation())
            if (geoCodeResult != null && geoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
            }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null != reverseGeoCodeResult && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR && null != reverseGeoCodeResult.getLocation() && null != reverseGeoCodeResult.getAddress()) {
            this.reverseGeoCodeResult = reverseGeoCodeResult;
            if (reverseGeoCodeResult.getAddressDetail() != null && reverseGeoCodeResult.getAddressDetail().city != null)
                currentCity = reverseGeoCodeResult.getAddressDetail().city;
            if (selectedTextMarker != null) {
                selectedTextMarker.setText(reverseGeoCodeResult.getAddress());
            } else {
                String str = reverseGeoCodeResult.getAddress();
                if (reverseGeoCodeResult.getPoiRegionsInfoList() != null && reverseGeoCodeResult.getPoiRegionsInfoList().size() != 0) {
                    ReverseGeoCodeResult.PoiRegionsInfo region = reverseGeoCodeResult.getPoiRegionsInfoList().get(0);
                    if (region.regionName != null) str += region.regionName;
                }
                OverlayOptions textOptions = new TextOptions().text(str).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(reverseGeoCodeResult.getLocation());
                selectedTextMarker = (Text) baiduMap.addOverlay(textOptions);
            }
            if (reverseGeoCodeResult.getPoiList() != null)
                setSearchAroundResult(reverseGeoCodeResult.getPoiList());
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker == null) return;
        LatLng latLng = marker.getPosition();
        if (latLng == null) selectedTextMarker.setVisible(false);
        if (selectedTextMarker != null) {
            List<Overlay> overlays = new ArrayList<>();
            overlays.add(selectedTextMarker);
            baiduMap.removeOverLays(overlays);
            selectedTextMarker = null;
        }
        if (geoCoder != null) {
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (selectedTextMarker != null) {
            List<Overlay> overlays = new ArrayList<>();
            overlays.add(selectedTextMarker);
            baiduMap.removeOverLays(overlays);
            selectedTextMarker = null;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            loadIndex = 0;
            return;
        }
        setSearchAroundResult(poiResult.getAllPoi());
    }

    private void setSearchAroundResult(List<PoiInfo> poiInfos) {
        List<Overlay> overlays = new ArrayList<>();
        for (Map.Entry<Marker, PoiInfo> entry : searchAroundMarkers.entrySet()) {
            entry.getKey().hideInfoWindow();
            overlays.add(entry.getKey());
        }
        baiduMap.hideInfoWindow();
        baiduMap.removeOverLays(overlays);
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

    void showSelectedMarker(LatLng latLng) {
        if (baiduMap == null)
            return;
        MarkerOptions option = new MarkerOptions().icon(MapUtil.getMarkerIcon(1)).position(latLng).draggable(true).isForceDisPlay(true).isJoinCollision(true);
        selectedMarker = (Marker) baiduMap.addOverlay(option);
    }

    /**
     * 展示周边搜索结果Marker
     *
     * @param poiInfo
     * @param i
     */
    private void showSearchAroundMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) return;
        Marker existedMarker = isMarkerExisted(poiInfo);
        if (existedMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(poiInfo.getLocation()).icon(MapUtil.getMarkerIcon(0)).perspective(true).isJoinCollision(true);
            Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
            OverlayManager manager = new PoiOverlay(baiduMap);
            manager.addToMap();
            if (null != marker) {
                searchAroundMarkers.put(marker, poiInfo);
                if (i == 0) preSelectMarker = marker;
            }
        }
    }

    Marker isMarkerExisted(PoiInfo poiInfo) {
        if (null == poiInfo) return null;
        for (Map.Entry<Marker, PoiInfo> entry : searchAroundMarkers.entrySet())
            if (Objects.equals(entry.getValue().uid, poiInfo.uid)) return entry.getKey();
        return null;
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null == marker || null == searchAroundMarkers) return false;
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
        if (null == poiInfo) return false;
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(poiInfo.getLocation()).zoom(baiduMap.getMapStatus().zoom).build()));
        baiduMap.showInfoWindow(getPoiInfoWindow(poiInfo));
        if (null != preSelectMarker) {
            preSelectMarker.setForceDisplay(false);
            preSelectMarker.setIcon(MapUtil.getMarkerIcon(0));
        }
        marker.setIcon(MapUtil.getMarkerIcon(1));
        marker.setForceDisplay(true);
        preSelectMarker = marker;
        return true;
    }

    /**
     * 根据POI获取Poi悬浮窗
     *
     * @param poiInfo
     * @return
     */
    @NonNull
    @Contract("_ -> new")
    @SuppressLint("UseCompatLoadingForDrawables")
    private InfoWindow getPoiInfoWindow(@NonNull PoiInfo poiInfo) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.layout_map_infowindow, null);
        ((TextView) view.findViewById(R.id.infowindow_title)).setText(poiInfo.getName());
        ((TextView) view.findViewById(R.id.infowindow_address)).setText(poiInfo.getAddress());
        view.findViewById(R.id.infowindow_title).setClickable(false);
        view.findViewById(R.id.infowindow_address).setClickable(false);
        view.setOnClickListener(v -> {
            MyDialog dialog = new MyDialog(SelectLocationActivity.this);
            dialog.setTitle("选择地点");
            dialog.setMessage("是否选择" + ((TextView) view.findViewById(R.id.infowindow_title)).getText() + "(" + ((TextView) view.findViewById(R.id.infowindow_address)).getText() + ")");
            dialog.setNegtive("取消");
            dialog.setPositive("确认");
            dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Attraction attraction = new Attraction();
                    attraction.setLatitude(poiInfo.getLocation().latitude);
                    attraction.setLongtitude(poiInfo.getLocation().longitude);
                    attraction.setLocation(poiInfo.getAddress());
                    attraction.setName(poiInfo.getName());
                    bundle.putSerializable("selection", attraction);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });
        return new InfoWindow(view, poiInfo.getLocation(), -150);
    }

    private enum onReceiveLocationOption {
        NONE, JUMPTO, JUMPTO_MARKER
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        onReceiveLocationOption option;

        MyLocationListener(onReceiveLocationOption isJumpTo) {
            this.option = isJumpTo;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) return;
            int errorCode = location.getLocType();
            float radius = location.getRadius();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String coorType = location.getCoorType();
            if (errorCode == BDLocation.TYPE_NO_PERMISSION_LOCATION_FAIL)
                IToast.makeTextTop(SelectLocationActivity.this, "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                myLocation = location;
                if (option == onReceiveLocationOption.JUMPTO) {
                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                } else if (option == onReceiveLocationOption.JUMPTO_MARKER) {
                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                    LatLng latLng = new LatLng(latitude, longitude);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(latLng).zoom(18.0f);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    showSelectedMarker(latLng);
                    if (location.getLocationDescribe() != null) {
                        OverlayOptions textOptions = new TextOptions().text(location.getLocationDescribe()).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(latLng);
                        selectedTextMarker = (Text) baiduMap.addOverlay(textOptions);
                    } else {
                        if (geoCoder != null) {
                            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
                        }
                    }
                }
                locationOption.setScanSpan(0);
                locationClient.stop();
                locationClient.setLocOption(locationOption);
            }
        }
    }

}
