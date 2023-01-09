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
import android.util.Log;
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
import com.cloudchewie.client.entity.RequestTopic;
import com.cloudchewie.client.entity.Topic;
import com.cloudchewie.client.util.image.ImageUrlUtil;
import com.cloudchewie.client.util.image.ImageViewInfo;
import com.cloudchewie.client.util.image.NineGridUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.util.widget.CommonPopupWindow;
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
import java.util.List;

public class CreateTopicActivity extends BaseActivity {
    EditText nameEdit;
    EditText describeEdit;
    TextView describeCount;
    EditText reasonEdit;
    TextView reasonCount;
    ImageView pickImage;
    TextView cancelButton;
    TextView publishButton;
    RefreshLayout swipeRefreshLayout;
    NineGridImageView<ImageViewInfo> nineGridImageView;
    RequestTopic requestTopic;
    int currentEditIndex;
    int maxSize = 200;
    int maxNameSize = 20;
    List<ImageItem> selectImages = new ArrayList<>();
    private CommonPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_create_topic);
        nameEdit = findViewById(R.id.activity_create_topic_name);
        describeEdit = findViewById(R.id.activity_create_topic_describe);
        describeCount = findViewById(R.id.activity_create_topic_describe_count);
        reasonEdit = findViewById(R.id.activity_create_topic_reason);
        reasonCount = findViewById(R.id.activity_create_topic_reason_count);
        pickImage = findViewById(R.id.activity_create_topic_add_picture);
        nineGridImageView = findViewById(R.id.activity_create_topic_nine_grid);
        cancelButton = findViewById(R.id.activity_create_topic_cancel);
        publishButton = findViewById(R.id.activity_create_topic_publish);
        initView();
        initSelectImage();
        initSwipeRefresh();
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        describeCount.setText("0/" + maxSize);
        describeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        reasonCount.setText("0/" + maxSize);
        reasonEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        nameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNameSize)});
        publishButton.setOnClickListener(v -> {
            IToast.makeTextBottom(this, "申请成功，您可以在系统通知查看申请结果", Toast.LENGTH_SHORT).show();
            finish();
        });
        publishButton.setSelected(false);
        publishButton.setEnabled(false);
        publishButton.setTextColor(getColor(R.color.color_light_gray));
        cancelButton.setOnClickListener(v -> {
            MyDialog dialog = new MyDialog(CreateTopicActivity.this);
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
        describeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                describeCount.setText(charSequence.length() + "/" + maxSize);
                updatePublishState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        reasonEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reasonCount.setText(charSequence.length() + "/" + maxSize);
                updatePublishState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updatePublishState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updatePublishState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            Serializable object = intent.getSerializableExtra("request_topic");
            if (object instanceof RequestTopic) {
                Log.d("xuruida", "enter");
                requestTopic = (RequestTopic) object;
                if (requestTopic.getTopic() != null) {
                    Topic topic = requestTopic.getTopic();
                    nameEdit.setText(topic.getName());
                    describeEdit.setText(topic.getDescribe());
                }
                reasonEdit.setText(requestTopic.getReason());
            }
        }
    }

    void updatePublishState() {
        if (nameEdit.getText().toString().length() > 0 && describeEdit.getText().toString().length() > 0 && reasonEdit.getText().toString().length() > 0) {
            publishButton.setSelected(true);
            publishButton.setEnabled(true);
            publishButton.setTextColor(getColor(R.color.color_prominent));
        } else {
            publishButton.setSelected(false);
            publishButton.setEnabled(false);
            publishButton.setTextColor(getColor(R.color.color_light_gray));
        }
    }

    void initSelectImage() {
        pickImage.setOnClickListener(v -> selectImage(104));
        nineGridImageView.setAdapter(new MyNineGridImageViewAdapter());
        nineGridImageView.setItemImageLongClickListener((context, imageView, index, list) -> {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(CreateTopicActivity.this).inflate(R.layout.layout_post_image_operation, null);
            view.findViewById(R.id.linear_cancle).setOnClickListener(v -> popupWindow.dismiss());
            view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
                Intent intent = new Intent(CreateTopicActivity.this, UCropActivity.class);
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
            popupWindow = new CommonPopupWindow.Builder(CreateTopicActivity.this).setView(view).setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).setOutsideTouchable(true).setAnimationStyle(R.style.UpDownAnimation).create();
            popupWindow.showBottom(getWindow().getDecorView().findViewById(android.R.id.content), 1f);
            VibrateUtils.vibrate(50);
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        MyDialog dialog = new MyDialog(CreateTopicActivity.this);
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
        swipeRefreshLayout = findViewById(R.id.activity_create_topic_swipe_refresh);
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
