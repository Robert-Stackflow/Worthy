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
import android.view.View;
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
import com.cloudchewie.client.entity.Post;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.util.widget.ListBottomSheet;
import com.cloudchewie.ninegrid.NineGridImageView;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MyDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yalantis.ucrop.UCropActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePostActivity extends BaseActivity {
    Post post;
    RefreshLayout swipeRefreshLayout;
    EditText content;
    TextView wordCount;
    ImageView pickImage;
    TextView cancelButton;
    TextView publishButton;
    NineGridImageView<ImageViewInfo> nineGridImageView;
    int currentEditIndex;
    int maxSize = 200;
    List<ImageItem> selectImages = new ArrayList<>();
    private ListBottomSheet popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
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
        publishButton.setOnClickListener(v -> {
            IToast.makeTextBottom(this, "帖子发布成功", Toast.LENGTH_SHORT).show();
            finish();
        });
        publishButton.setSelected(false);
        publishButton.setEnabled(false);
        publishButton.setTextColor(getColor(R.color.color_light_gray));
        cancelButton.setOnClickListener(v -> {
            MyDialog dialog = new MyDialog(CreatePostActivity.this);
            dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
            dialog.setNegtive("不保存");
            dialog.setPositive("保存");
            dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    finish();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
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
                    publishButton.setTextColor(getColor(R.color.color_light_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pickImage.setOnClickListener(v -> selectImage(104));
        nineGridImageView.setAdapter(new MyNineGridImageViewAdapter());
        nineGridImageView.setItemImageLongClickListener((context, imageView, index, list) -> {

            List<String> operations = Arrays.asList(getResources().getStringArray(R.array.creation_image_operation));
            popupWindow = new ListBottomSheet(this, ListBottomSheetBean.strToBean(operations));
            popupWindow.setOnItemClickedListener(position -> {
                if (position == 0) {
                    nineGridImageView.deleteImage(index);
                    selectImages.remove(index);
                    if (nineGridImageView.getChildCount() >= 9)
                        pickImage.setVisibility(View.GONE);
                    else
                        pickImage.setVisibility(View.VISIBLE);
                    popupWindow.dismiss();
                } else if (position == 1) {
                    Intent intent = new Intent(CreatePostActivity.this, UCropActivity.class);
                    intent.putExtra("filePath", list.get(index).getUrl());
                    String destDir = getFilesDir().getAbsolutePath();
                    String fileName = "img_" + System.currentTimeMillis() + ".png";
                    intent.putExtra("outPath", destDir + fileName);
                    startActivityForResult(intent, 11);
                    currentEditIndex = index;
                    popupWindow.dismiss();
                } else if (position == 2) {
                    IToast.makeTextBottom(this, "图片保存成功", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
            popupWindow.show();
            VibrateUtils.vibrate(50);
            return false;
        });
        Intent intent = getIntent();
        if (intent != null) {
            Serializable object = intent.getSerializableExtra("post");
            if (object instanceof Post) {
                post = (Post) object;
                content.setText(post.getContent());
            }
        }
    }

    @Override
    public void onBackPressed() {
        MyDialog dialog = new MyDialog(CreatePostActivity.this);
        dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
        dialog.setNegtive("不保存");
        dialog.setPositive("保存");
        dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                finish();
            }

            @Override
            public void onNegtiveClick() {
                finish();
            }

            @Override
            public void onCloseClick() {
                dialog.dismiss();
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
                NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, ImageUrlUtil.urlToImageViewInfo(urls));
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
                    List<ImageViewInfo> imageViewInfos = nineGridImageView.getImagesData();
                    imageViewInfos.set(currentEditIndex, new ImageViewInfo(outPath));
                    NineGridUtil.setDataSourceWithoutUserFragment(nineGridImageView, imageViewInfos);
                    if (nineGridImageView.getChildCount() >= 9)
                        pickImage.setVisibility(View.GONE);
                    else
                        pickImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
