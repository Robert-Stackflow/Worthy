package com.cloudchewie.client.activity.settings;

import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.VibrateUtils;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.MyNineGridImageViewAdapter;
import com.cloudchewie.client.bean.ListBottomSheetBean;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.util.widget.CommonPopupWindow;
import com.cloudchewie.client.util.widget.ListBottomSheet;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.CheckBoxItem;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.SettingItem;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yalantis.ucrop.UCropActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedbackActivity extends BaseActivity {
    SettingItem typeEntry;
    EditText describeEdit;
    TextView describeCount;
    EditText contactEdit;
    ImageView pickImage;
    TextView cancelButton;
    TextView submitButton;
    CheckBoxItem isUploadLog;
    RefreshLayout swipeRefreshLayout;
    NineGridImageView<ImageViewInfo> nineGridImageView;
    int currentEditIndex;
    int maxSize = 200;
    int maxNameSize = 20;
    List<String> types;
    List<ImageItem> selectImages = new ArrayList<>();
    private CommonPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_feedback);
        typeEntry = findViewById(R.id.activity_feedback_type);
        describeEdit = findViewById(R.id.activity_feedback_describe);
        describeCount = findViewById(R.id.activity_feedback_describe_count);
        contactEdit = findViewById(R.id.activity_feedback_contact);
        pickImage = findViewById(R.id.activity_feedback_add_picture);
        nineGridImageView = findViewById(R.id.activity_feedback_nine_grid);
        cancelButton = findViewById(R.id.activity_feedback_cancel);
        submitButton = findViewById(R.id.activity_feedback_submit);
        isUploadLog = findViewById(R.id.activity_feedback_switch_upload_log);
        initView();
        initSelectImage();
        initSwipeRefresh();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        isUploadLog.setTitlePadding(0, 0, 0, 0);
        typeEntry.setTitlePadding(0, 0, 0, 0);
        describeCount.setText("0/" + maxSize);
        describeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        types = Arrays.asList(getResources().getStringArray(R.array.feedback_type));
        typeEntry.setOnClickListener(v -> {
            ListBottomSheet bottomSheet = new ListBottomSheet(FeedbackActivity.this, ListBottomSheetBean.strToBean(types));
            bottomSheet.setOnItemClickedListener(position -> {
                if (types != null) typeEntry.setTipText(types.get(position));
                bottomSheet.dismiss();
            });
            bottomSheet.show();
            bottomSheet.setOnDismissListener(dialog -> updateSubmitState());
        });
        submitButton.setOnClickListener(v -> {
            IToast.makeTextBottom(this, "提交成功，感谢您的反馈建议", Toast.LENGTH_SHORT).show();
            finish();
        });
        submitButton.setSelected(false);
        submitButton.setEnabled(false);
        submitButton.setTextColor(getColor(R.color.color_light_gray));
        cancelButton.setOnClickListener(v -> finish());
        describeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                describeCount.setText(charSequence.length() + "/" + maxSize);
                updateSubmitState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        contactEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSubmitState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void updateSubmitState() {
        if (types == null)
            types = Arrays.asList(getResources().getStringArray(R.array.feedback_type));
        if ((types != null && types.contains(typeEntry.getTip())) && describeEdit.getText().toString().length() > 0 && contactEdit.getText().toString().length() > 0) {
            submitButton.setSelected(true);
            submitButton.setEnabled(true);
            submitButton.setTextColor(getColor(R.color.color_prominent));
        } else {
            submitButton.setSelected(false);
            submitButton.setEnabled(false);
            submitButton.setTextColor(getColor(R.color.color_light_gray));
        }
    }

    void initSelectImage() {
        pickImage.setOnClickListener(v -> selectImage(104));
        nineGridImageView.setAdapter(new MyNineGridImageViewAdapter());
        nineGridImageView.setItemImageLongClickListener((context, imageView, index, list) -> {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(FeedbackActivity.this).inflate(R.layout.layout_post_image_operation, null);
            view.findViewById(R.id.linear_cancle).setOnClickListener(v -> popupWindow.dismiss());
            view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
                Intent intent = new Intent(FeedbackActivity.this, UCropActivity.class);
                intent.putExtra("filePath", list.get(index).getUrl());
                String destDir = getFilesDir().getAbsolutePath();
                String fileName = "img_" + System.currentTimeMillis() + ".png";
                intent.putExtra("outPath", destDir + fileName);
                startActivityForResult(intent, 11);
                currentEditIndex = index;
                popupWindow.dismiss();
            });
            view.findViewById(R.id.linear_delete_pic).setOnClickListener(v -> {
                nineGridImageView.deleteImage(index);
                selectImages.remove(index);
                if (nineGridImageView.getChildCount() >= 9) pickImage.setVisibility(View.GONE);
                else pickImage.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
            });
            view.findViewById(R.id.linear_download_pic).setVisibility(View.GONE);
            popupWindow = new CommonPopupWindow.Builder(FeedbackActivity.this).setView(view).setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).setOutsideTouchable(true).setAnimationStyle(R.style.UpDownAnimation).create();
            popupWindow.showBottom(getWindow().getDecorView().findViewById(android.R.id.content), 1f);
            VibrateUtils.vibrate(50);
            return false;
        });
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_feedback_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    public void selectImage(int requestCode) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(9 - ((nineGridImageView.getImagesData() == null) ? 0 : nineGridImageView.getImagesData().size()));
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (requestCode == 104) {
                selectImages.addAll((ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
                List<String> urls = new ArrayList<>();
                for (ImageItem imageItem : selectImages)
                    urls.add(imageItem.path);
                NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, ImageUrlUtil.urlToImageViewInfo(urls));
                if (nineGridImageView.getChildCount() >= 9) pickImage.setVisibility(View.GONE);
                else pickImage.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                if (!TextUtils.isEmpty(outPath)) {
                    selectImages.get(currentEditIndex).path = outPath;
                    List<ImageViewInfo> imageViewInfos = nineGridImageView.getImagesData();
                    imageViewInfos.set(currentEditIndex, new ImageViewInfo(outPath));
                    NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, imageViewInfos);
                    if (nineGridImageView.getChildCount() >= 9) pickImage.setVisibility(View.GONE);
                    else pickImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
