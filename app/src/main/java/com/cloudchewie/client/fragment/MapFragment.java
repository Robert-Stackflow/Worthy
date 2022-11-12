package com.cloudchewie.client.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.cloudchewie.client.R;
import com.cloudchewie.client.util.StatusBarUtil;

public class MapFragment extends Fragment implements View.OnClickListener {
    View mainView;
    MapView mapView;
    BaiduMap baiduMap;
    LocationClientOption locationOption;
    LocationClient locationClient;

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
        return mainView;
    }

    void initMap() {
        mapView = mainView.findViewById(R.id.map_view);
        mapView.setMapCustomStylePath("/libs/theme/dark_blue");
        mapView.setMapCustomStyleEnable(true);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
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
    }

    void locateMyLocation() {
        try {
            locationClient = new LocationClient(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd09ll");
        locationOption.setScanSpan(1000);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationClient.setLocOption(locationOption);
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float radius = location.getRadius();
            String coorType = location.getCoorType();
            int errorCode = location.getLocType();
            if (errorCode == BDLocation.TYPE_NO_PERMISSION_LOCATION_FAIL)
                Toast.makeText(getActivity(), "定位失败，请授予定位权限!", Toast.LENGTH_SHORT).show();
            else {
                locationOption.setScanSpan(0);
                locationClient.setLocOption(locationOption);
            }
        }
    }

    @Override
    public void onClick(View view) {
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
