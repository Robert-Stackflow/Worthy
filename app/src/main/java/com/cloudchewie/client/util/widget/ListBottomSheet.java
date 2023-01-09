package com.cloudchewie.client.util.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.ListBottomSheetAdapter;
import com.cloudchewie.client.bean.ListBottomSheetBean;
import com.cloudchewie.client.util.decoration.DividerItemDecoration;
import com.cloudchewie.ui.BottomSheet;

import java.util.List;

public class ListBottomSheet extends BottomSheet {
    List<ListBottomSheetBean> beanList;
    RecyclerView recyclerView;
    ListBottomSheetAdapter adapter;
    TextView cancelButton;
    OnCancelListener onCancelListener;
    ListBottomSheetAdapter.OnItemClickedListener listener;

    public ListBottomSheet(@NonNull Context context, List<ListBottomSheetBean> beanList) {
        super(context);
        this.beanList = beanList;
        initView();
    }

    public ListBottomSheet setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    void initView() {
        setBackGroundTint(R.color.content_background);
        setTitleBarBackGroundTint(R.color.content_background);
        setMainLayout(R.layout.layout_list_bottom_sheet);
        recyclerView = mainView.findViewById(R.id.layout_list_bottom_sheet_recyclerview);
        cancelButton = mainView.findViewById(R.id.layout_list_bottom_sheet_cancel);
        cancelButton.setOnClickListener(v -> {
            if (onCancelListener != null)
                onCancelListener.OnCancle();
            dismiss();
        });
        adapter = new ListBottomSheetAdapter(getContext(), beanList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public ListBottomSheet setOnItemClickedListener(ListBottomSheetAdapter.OnItemClickedListener listener) {
        this.listener = listener;
        if (adapter != null) adapter.setOnItemClickedListener(listener);
        return this;
    }

    public interface OnCancelListener {
        void OnCancle();
    }
}
