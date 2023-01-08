package com.cloudchewie.client.activity.map;

import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_DARK;
import static com.cloudchewie.client.util.map.MapUtil.CUSTOM_FILE_NAME_TEA;
import static com.cloudchewie.client.util.map.MapUtil.DARK_ID;
import static com.cloudchewie.client.util.map.MapUtil.LIGHT_ID;
import static com.cloudchewie.client.util.map.MapUtil.getCustomStyleFilePath;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CustomMapStyleCallBack;
import com.baidu.mapapi.map.MapCustomStyleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.entity.Attraction;
import com.cloudchewie.client.request.MapRequest;
import com.cloudchewie.client.util.map.MapUtil;
import com.cloudchewie.client.util.system.AppVersionUtil;
import com.cloudchewie.client.util.system.ShareUtil;
import com.cloudchewie.client.util.ui.DarkModeUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MyDialog;

public class LocationDetailActivity extends BaseActivity implements SensorEventListener, View.OnClickListener, OnGetGeoCoderResultListener {
    //Map
    private MapView mapView;
    private BaiduMap baiduMap;
    private GeoCoder geoCoder;
    private SensorManager sensorManager;
    private LocationClient locationClient;
    private LocationClientOption locationOption;
    //控件
    private Button shareButton;
    private TextView mNameView;
    private Button locateButton;
    private ImageView mBackButton;
    private TextView mLocationView;
    private ConstraintLayout bottomLayout;
    //变量
    private double lastX = 0.0;
    private BDLocation myLocation;
    private Attraction mAttraction;
    private LatLng attractionLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        mBackButton = findViewById(R.id.activity_location_detail_back);
        mNameView = findViewById(R.id.activity_location_detail_attraction_name);
        mLocationView = findViewById(R.id.activity_location_detail_attraction_location);
        mapView = findViewById(R.id.activity_location_detail_map_view);
        locateButton = findViewById(R.id.activity_location_detail_baidumap);
        shareButton = findViewById(R.id.activity_location_detail_attraction_share);
        bottomLayout = findViewById(R.id.activity_location_detail_bottom_layout);
        mBackButton.setOnClickListener(v -> finish());
        locateButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        findViewById(R.id.activity_location_detail_goto_mylocation).setOnClickListener(this);
        findViewById(R.id.activity_location_detail_goto_location).setOnClickListener(this);
        StatusBarUtil.setStatusBarMarginTop(mBackButton, 0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        initView();
        initMap();
        initMapStyle();
        locateLocation();
    }

    void initView() {
        Intent intent = this.getIntent();
        mAttraction = (Attraction) intent.getSerializableExtra("attraction");
        if (mAttraction == null) {
            IToast.makeTextBottom(this, "获取景点位置失败,请稍后再试", Toast.LENGTH_SHORT).show();
            bottomLayout.setVisibility(View.GONE);
        } else {
            mNameView.setText(mAttraction.getName());
            mLocationView.setText(mAttraction.getLocation());
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    void initMap() {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
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
        if (mAttraction != null && mAttraction.getLocation() != null && mAttraction.getName() != null) {
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mAttraction.getLatitude(), mAttraction.getLongtitude())));
        }
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
        if (view == findViewById(R.id.activity_location_detail_goto_mylocation)) {
            locateMyLocation(onReceiveLocationOption.JUMPTO);
        } else if (view == findViewById(R.id.activity_location_detail_goto_location)) {
            locateLocation();
        } else if (view == locateButton) {
            if (mAttraction == null) {
                IToast.makeTextBottom(this, "获取景点位置失败,请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                if (!AppVersionUtil.isAppInstalled(this, ShareUtil.getBaiduMapPackageName())) {
                    MyDialog dialog = new MyDialog(this);
                    dialog.setMessage("未安装百度地图APP,是否跳转到浏览器进行导航");
                    dialog.setNegtive("否");
                    dialog.setPositive("是");
                    dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            dialog.dismiss();
                            locateMyLocation(onReceiveLocationOption.JUMPTOBAIDUMAP_WEB);
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
                } else if (!MapUtil.isBaiduMapVersionSupport(AppVersionUtil.getAppVersionName(this, ShareUtil.getBaiduMapPackageName()))) {
                    MyDialog dialog = new MyDialog(this);
                    dialog.setMessage("您安装的百度地图APP版本过低,是否跳转到浏览器进行导航");
                    dialog.setNegtive("否");
                    dialog.setPositive("是");
                    dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            dialog.dismiss();
                            locateMyLocation(onReceiveLocationOption.JUMPTOBAIDUMAP_WEB);
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
                } else {
                    locateMyLocation(onReceiveLocationOption.JUMPTOBAIDUMAP);
                }
            }
        } else if (view == shareButton) {
            IToast.makeTextBottom(this, "功能维护中，暂时无法分享景点", Toast.LENGTH_SHORT).show();
        }
    }

    void jumpToBaiduMap(boolean isSupportWeb) {
        LatLng startPoint = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        LatLng endPoint = new LatLng(mAttraction.getLatitude(), mAttraction.getLongtitude());
        RouteParaOption paraOption = new RouteParaOption().startPoint(startPoint).endPoint(endPoint).busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
        try {
            BaiduMapRoutePlan.setSupportWebRoute(isSupportWeb);
            BaiduMapRoutePlan.openBaiduMapTransitRoute(paraOption, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaiduMapRoutePlan.finish(this);
    }

    void locateLocation() {
        if (mAttraction == null) {
            IToast.makeTextBottom(this, "获取景点位置失败,请稍后再试", Toast.LENGTH_SHORT).show();
        } else {
            baiduMap.clear();
            MapStatus.Builder builder = new MapStatus.Builder();
            LatLng latLng;
            if (attractionLatLng != null) latLng = attractionLatLng;
            else
                latLng = MapRequest.getGeoCoding(mAttraction.getLocation()).getLocation().toLatLng();
            builder.target(latLng).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            MarkerOptions option = new MarkerOptions().icon(MapUtil.getMarkerIcon(1)).position(latLng);
            baiduMap.addOverlay(option);
            OverlayOptions textOptions = new TextOptions().text(mAttraction.getName()).bgColor(0x00000000).fontSize(24).fontColor(getResources().getColor(R.color.color_accent, null)).position(latLng);
            baiduMap.addOverlay(textOptions);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
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
            if (geoCodeResult != null && geoCodeResult.error == SearchResult.ERRORNO.NO_ERROR)
                attractionLatLng = geoCodeResult.getLocation();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null != reverseGeoCodeResult && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR && null != reverseGeoCodeResult.getLocation() && null != reverseGeoCodeResult.getAddressDetail() && null != reverseGeoCodeResult.getAddressDetail().city) {
            geoCoder.geocode(new GeoCodeOption().address(mAttraction.getLocation()).city(reverseGeoCodeResult.getAddressDetail().city));
        }
    }

    private enum onReceiveLocationOption {
        NONE, JUMPTO, JUMPTOBAIDUMAP, JUMPTOBAIDUMAP_WEB,
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
                IToast.makeTextTop(LocationDetailActivity.this, "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                myLocation = location;
                if (option == onReceiveLocationOption.JUMPTO) {
                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                } else if (option == onReceiveLocationOption.JUMPTOBAIDUMAP) {
                    jumpToBaiduMap(false);
                } else if (option == onReceiveLocationOption.JUMPTOBAIDUMAP_WEB) {
                    jumpToBaiduMap(true);
                }
                locationOption.setScanSpan(0);
                locationClient.stop();
                locationClient.setLocOption(locationOption);
            }
        }
    }

}
