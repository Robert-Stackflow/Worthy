package com.cloudchewie.client.activity.create;

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

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.VibrateUtils;
import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.adapter.MyNineGridImageViewAdapter;
import com.cloudchewie.client.domin.UserViewInfo;
import com.cloudchewie.client.util.image.CommonPopupWindow;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.CustomDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yalantis.ucrop.UCropActivity;

import java.util.ArrayList;
import java.util.List;

public class CreatePostActivity extends BaseActivity {
    RefreshLayout swipeRefreshLayout;
    EditText content;
    TextView wordCount;
    ImageView pickImage;
    TextView cancelButton;
    TextView publishButton;
    NineGridImageView<UserViewInfo> nineGridImageView;
    int currentEditIndex;
    int maxSize = 200;
    List<ImageItem> selectImages = new ArrayList<>();
    private CommonPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_create_post);
        content = findViewById(R.id.activity_create_post_content);
        wordCount = findViewById(R.id.activity_create_post_count);
        pickImage = findViewById(R.id.activity_create_post_add_picture);
        nineGridImageView = findViewById(R.id.activity_create_post_nine_grid);
        cancelButton = findViewById(R.id.activity_create_post_cancel);
        publishButton = findViewById(R.id.activity_create_post_publish);
        initView();
        initSwipeRefresh();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        wordCount.setText("0/" + maxSize);
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        publishButton.setOnClickListener(v -> finish());
        publishButton.setSelected(false);
        publishButton.setEnabled(false);
        publishButton.setTextColor(getColor(R.color.text_color_light_gray));
        cancelButton.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(CreatePostActivity.this);
            dialog.setMessage("是否将本次编辑保存为草稿？\n保存后下次可以继续编写");
            dialog.setNegtive("放弃并退出");
            dialog.setPositive("保存为草稿");
            dialog.setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    finish();
                }
            });
            dialog.show();
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordCount.setText(charSequence.length() + "/" + maxSize);
                if (charSequence.length() > 0) {
                    publishButton.setSelected(true);
                    publishButton.setEnabled(true);
                    publishButton.setTextColor(getColor(R.color.color_prominent));
                } else {
                    publishButton.setSelected(false);
                    publishButton.setEnabled(false);
                    publishButton.setTextColor(getColor(R.color.text_color_light_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pickImage.setOnClickListener(v -> selectImage(104));
        nineGridImageView.setAdapter(new MyNineGridImageViewAdapter());
        nineGridImageView.setItemImageLongClickListener((context, imageView, index, list) -> {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(CreatePostActivity.this).inflate(R.layout.layout_post_image_operation, null);
            view.findViewById(R.id.linear_cancle).setOnClickListener(v -> popupWindow.dismiss());
            view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
                Intent intent = new Intent(CreatePostActivity.this, UCropActivity.class);
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
                if (nineGridImageView.getChildCount() >= 9)
                    pickImage.setVisibility(View.GONE);
                else
                    pickImage.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
            });
            view.findViewById(R.id.linear_download_pic).setVisibility(View.GONE);
            popupWindow = new CommonPopupWindow.Builder(CreatePostActivity.this)
                    .setView(view)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setOutsideTouchable(true)
                    .setAnimationStyle(R.style.UpDownAnimation)
                    .create();
            popupWindow.showBottom(getWindow().getDecorView().findViewById(android.R.id.content), 1f);
            VibrateUtils.vibrate(50);
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        CustomDialog dialog = new CustomDialog(CreatePostActivity.this);
        dialog.setMessage("是否将本次编辑保存为草稿？\n保存后下次可以继续编写");
        dialog.setNegtive("放弃并退出");
        dialog.setPositive("保存为草稿");
        dialog.setOnClickBottomListener(new CustomDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                finish();
            }

            @Override
            public void onNegtiveClick() {
                finish();
            }
        });
        dialog.show();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_create_post_swipe_refresh);
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
                NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, ImageUrlUtil.getViewInfos(urls));
                if (nineGridImageView.getChildCount() >= 9)
                    pickImage.setVisibility(View.GONE);
                else
                    pickImage.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                if (!TextUtils.isEmpty(outPath)) {
                    selectImages.get(currentEditIndex).path = outPath;
                    List<UserViewInfo> userViewInfos = nineGridImageView.getImagesData();
                    userViewInfos.set(currentEditIndex, new UserViewInfo(outPath));
                    NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, userViewInfos);
                    if (nineGridImageView.getChildCount() >= 9)
                        pickImage.setVisibility(View.GONE);
                    else
                        pickImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
