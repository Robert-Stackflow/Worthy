/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.activity.global;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.client.R;
import com.cloudchewie.client.domin.User;
import com.cloudchewie.client.request.UserRequest;
import com.cloudchewie.client.service.UserService;
import com.cloudchewie.client.util.system.LocalStorage;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_login);
        findViewById(R.id.titlebar_left_button).setOnClickListener(this);
        findViewById(R.id.login_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.titlebar_left_button))
            finish();
        else if (view == findViewById(R.id.login_confirm)) {
            String mobile = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.mobile)).getText()).toString();
            String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.password)).getText()).toString();
            if (RegexUtils.isMobileExact(mobile) && password.length() >= 6 && password.length() <= 16) {
                ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Object>() {
                    @Override
                    public Object doInBackground() {
                        User user = new User(mobile, password);
                        UserRequest userRequest = new UserRequest();
                        String token = userRequest.login(user);
                        if (token != null) {
                            LocalStorage.setToken(token);
                            User foundUser = userRequest.find();
                            foundUser.setToken(LocalStorage.getToken());
                            UserService.insert(foundUser);
                            getUserData();
                            finish();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(Object result) {
                    }
                });
            }
        }
    }

    private void getUserData() {
    }
}