/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.fragment.nav.DiscoverFragment;
import com.cloudchewie.client.fragment.nav.MapFragment;
import com.cloudchewie.client.fragment.nav.MessageFragment;
import com.cloudchewie.client.fragment.nav.UserFragment;
import com.cloudchewie.client.util.database.AppDatabase;
import com.cloudchewie.client.util.system.LocalStorage;
import com.cloudchewie.ui.NoScrollViewPager;
import com.yh.bottomnavigationex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private ImageButton userButton;
    private NoScrollViewPager viewPager;
    private BottomNavigationViewEx bottomNavigation;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap();
        checkPermission();
        LocalStorage.init(AppDatabase.getInstance(getApplicationContext()));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigation.enableLabelVisibility(false);
        bottomNavigation.enableItemHorizontalTranslation(false);
        bottomNavigation.setSmallTextSize(11);
        bottomNavigation.setLargeTextSize(12);
        bottomNavigation.setIconSize(20);
        {
            fragments = new ArrayList<>(4);
            fragments.add(new MapFragment());
            fragments.add(new DiscoverFragment());
            fragments.add(new MessageFragment());
            fragments.add(new UserFragment());
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(adapter);
            viewPager.setNoScroll(false);
            bottomNavigation.setupWithViewPager(viewPager);
        }
        for (int i = 0; i < bottomNavigation.getBNItemViewCount(); i++)
            bottomNavigation.getBNMenuView().getChildAt(i).setOnLongClickListener(view -> true);
    }

    void checkPermission() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
    }

    void initMap() {
        SDKInitializer.setAgreePrivacy(getApplicationContext(), true);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LocationClient.setAgreePrivacy(true);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "授予权限成功", Toast.LENGTH_SHORT).show();
        }
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> data;

        public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> data) {
            super(fragmentManager);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}