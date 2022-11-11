package com.cloudchewie.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.SettingsActivity;

public class UserFragment extends Fragment implements View.OnClickListener {
    View mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_user, null);
        mainView.findViewById(R.id.user_settings).setOnClickListener(this);
        mainView.findViewById(R.id.switch_daynight).setOnClickListener(this);
        return mainView;
    }


    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.user_settings)) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (view == mainView.findViewById(R.id.switch_daynight)) {
//            NeumorphImageButton switchDaynight = (NeumorphImageButton) view;
//            if (switchDaynight != null) switchDaynight.setOnClickListener(v -> {
//                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED && (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES) == 0) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                } else {
//                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//            });
        }
    }
}
