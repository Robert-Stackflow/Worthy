package com.cloudchewie.client.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cloudchewie.client.R;
import com.cloudchewie.client.database.AppDatabase;
import com.cloudchewie.client.databinding.ActivityMainBinding;
import com.cloudchewie.client.fragment.FollowFragment;
import com.cloudchewie.client.fragment.HomeFragment;
import com.cloudchewie.client.fragment.MapFragment;
import com.cloudchewie.client.fragment.MessageFragment;
import com.cloudchewie.client.fragment.UserFragment;
import com.cloudchewie.client.ui.NoScrollViewPager;
import com.cloudchewie.client.util.LocalStorage;
import com.yh.bottomnavigationex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding bind;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private ImageButton userButton;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalStorage.init(AppDatabase.getInstance(getApplicationContext()));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BottomNavigationViewEx bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.enableLabelVisibility(false);
        bottomNavigation.enableItemHorizontalTranslation(false);
        {
            fragments = new ArrayList<>(5);
            fragments.add(new HomeFragment());
            fragments.add(new FollowFragment());
            fragments.add(new MapFragment());
            fragments.add(new MessageFragment());
            fragments.add(new UserFragment());
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
            ((NoScrollViewPager) findViewById(R.id.viewpager)).setAdapter(adapter);
            ((NoScrollViewPager) findViewById(R.id.viewpager)).setNoScroll(false);
            ((BottomNavigationViewEx) findViewById(R.id.bottom_navigation)).setupWithViewPager(((ViewPager) findViewById(R.id.viewpager)));
        }
    }

    @Override
    public void onClick(View view) {
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