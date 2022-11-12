package com.cloudchewie.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.client.R;
import com.cloudchewie.client.ui.CustomDialog;
import com.cloudchewie.client.util.StatusBarUtil;

public class MessageFragment extends Fragment implements View.OnClickListener {
    View mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_message, null);
        StatusBarUtil.setMargin(mainView.findViewById(R.id.message_titlebar), 0, StatusBarUtil.getHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.message_clear).setOnClickListener(this);
        return mainView;
    }


    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.message_clear)) {
            final CustomDialog dialog = new CustomDialog(getActivity());
            dialog.setMessage("是否清除所有未读消息?")
                    .setSingle(false).setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "清除所有未读消息", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "取消清除未读消息", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    }
}
