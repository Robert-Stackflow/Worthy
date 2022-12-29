package com.cloudchewie.client.activity.create;

import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.util.image.CommonPopupWindow;
import com.cloudchewie.client.util.ui.BitmapUtil;
import com.cloudchewie.client.util.ui.KeyBoardUtil;
import com.cloudchewie.client.util.ui.RichEditorUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.widget.RichEditor;
import com.cloudchewie.ui.CustomDialog;
import com.cloudchewie.ui.TitleBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yalantis.ucrop.UCropActivity;

import java.util.ArrayList;


public class CreateArticleActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    TitleBar titleBar;
    RxPermissions rxPermissions;
    EditText title;
    EditText content;
    RichEditor richEditor;
    ConstraintLayout operationLayout;
    String currentUrl = "";
    CommonPopupWindow popupWindow;
    boolean isTextBackGround = false;
    int maxTitleLength = 30;
    int defaultTextSize = 15;
    int currentTextSize = 3;
    int minTextSize = 1;
    int maxTextSize = 7;
    int currentPenColor = Color.BLACK;
    boolean isSelectingPenColor = false;
    private ArrayList<ImageItem> selectImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMargin(this);
        setContentView(R.layout.activity_create_article);
        titleBar = findViewById(R.id.activity_create_article_titlebar);
        title = findViewById(R.id.activity_create_article_title);
        content = findViewById(R.id.activity_create_article_content);
        richEditor = findViewById(R.id.activity_create_article_editor);
        operationLayout = findViewById(R.id.activity_create_article_operation_layout);
        operationLayout.setVisibility(View.GONE);
        rxPermissions = new RxPermissions(this);
        initView();
        initPop();
        initEditor();
        initSwipeRefresh();
    }

    void initEditor() {
        richEditor.setEditorHeight(800);
        richEditor.setEditorBackgroundColor(R.color.card_background);
        richEditor.setEditorFontSize(defaultTextSize);
        richEditor.setEditorFontColor(R.color.color_accent);
        richEditor.setPlaceholder("分享你的打卡经历");
        richEditor.setInputEnabled(true);
        richEditor.setOnTextChangeListener(text -> {
            if (TextUtils.isEmpty(title.getText().toString().trim())) {
                titleBar.getRightButton().setSelected(false);
                titleBar.getRightButton().setEnabled(false);
                return;
            }
            if (TextUtils.isEmpty(text)) {
                titleBar.getRightButton().setSelected(false);
                titleBar.getRightButton().setEnabled(false);
            } else {
                if (TextUtils.isEmpty(Html.fromHtml(text))) {
                    titleBar.getRightButton().setSelected(false);
                    titleBar.getRightButton().setEnabled(false);
                } else {
                    titleBar.getRightButton().setSelected(true);
                    titleBar.getRightButton().setEnabled(true);
                }
            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String html = richEditor.getHtml();
                if (TextUtils.isEmpty(html)) {
                    titleBar.getRightButton().setSelected(false);
                    titleBar.getRightButton().setEnabled(false);
                    return;
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(html))) {
                        titleBar.getRightButton().setSelected(false);
                        titleBar.getRightButton().setEnabled(false);
                        return;
                    } else {
                        titleBar.getRightButton().setSelected(true);
                        titleBar.getRightButton().setEnabled(true);
                    }
                }
                if (TextUtils.isEmpty(s.toString())) {
                    titleBar.getRightButton().setSelected(false);
                    titleBar.getRightButton().setEnabled(false);
                } else {
                    titleBar.getRightButton().setSelected(true);
                    titleBar.getRightButton().setEnabled(true);
                }
            }
        });
        richEditor.setOnDecorationChangeListener((text, types) -> {
            ArrayList<String> flagArr = new ArrayList<>();
            for (int i = 0; i < types.size(); i++)
                flagArr.add(types.get(i).name());
            if (flagArr.contains("BOLD")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_bold)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_bold)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("ITALIC")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_italic)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_italic)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("UNDERLINE")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_underline)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_underline)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("STRIKETHROUGH")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_strikethrough)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_strikethrough)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("ORDEREDLIST")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_fe0000));
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_selector_icon));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("UNORDEREDLIST")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_fe0000));
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_selector_icon));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("SUPERSCRIPT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_superscript)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_superscript)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("SUBSCRIPT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_subscript)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_subscript)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYLEFT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_leftalignment)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_leftalignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYCENTER")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_centeralignment)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_centeralignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYRIGHT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_rightalignment)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_rightalignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
        });
        richEditor.setImageClickListener(imageUrl -> {
            currentUrl = imageUrl;
            popupWindow.showBottom(getWindow().getDecorView().findViewById(android.R.id.content), 0.5f);
        });
    }

    private void initPop() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(CreateArticleActivity.this).inflate(R.layout.newapp_pop_picture, null);
        view.findViewById(R.id.linear_cancle).setOnClickListener(v -> popupWindow.dismiss());
        view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
            Intent intent = new Intent(CreateArticleActivity.this, UCropActivity.class);
            intent.putExtra("filePath", currentUrl);
            String destDir = getFilesDir().getAbsolutePath();
            String fileName = "img_" + System.currentTimeMillis() + ".png";
            intent.putExtra("outPath", destDir + fileName);
            startActivityForResult(intent, 11);
            popupWindow.dismiss();
        });
        view.findViewById(R.id.linear_delete_pic).setOnClickListener(v -> {
            String removeUrl = "<img src=\"" + currentUrl + "\" alt=\"picture\"  width=\"100%\">" + "<br><br>";
            String newUrl = richEditor.getHtml().replace(removeUrl, "");
            currentUrl = "";
            richEditor.setHtml(newUrl);
            if (RichEditorUtil.isEmpty(richEditor.getHtml()))
                richEditor.setHtml("");
            popupWindow.dismiss();
        });
        popupWindow = new CommonPopupWindow.Builder(CreateArticleActivity.this)
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.UpDownAnimation)
                .create();
        popupWindow.setOnDismissListener(() -> richEditor.setInputEnabled(true));
    }

    void initView() {
        titleBar.setRightButtonClickListener(v -> finish());
        title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxTitleLength)});
        titleBar.setLeftButtonClickListener(v -> {
            CustomDialog dialog = new CustomDialog(CreateArticleActivity.this);
            dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
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
        findViewById(R.id.activity_create_article_operation_redo).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_undo).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_location).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_highlight).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_pencolor).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_heading).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_fontbig).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_fontsmall).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_bold).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_italic).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_underline).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_strikethrough).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_subscript).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_superscript).setOnClickListener(this);

        findViewById(R.id.activity_create_article_operation_picture).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_todo).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_ol).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_ul).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_indent).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_outdent).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_quote).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_centeralignment).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_leftalignment).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_rightalignment).setOnClickListener(this);

        findViewById(R.id.activity_create_article_operation_color_black).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_red).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_blue).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_yellow).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_orange).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_pink).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_green).setOnClickListener(this);
        findViewById(R.id.activity_create_article_operation_color_purple).setOnClickListener(this);

        richEditor.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            View view = getCurrentFocus();
            CreateArticleActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int screenHeight = CreateArticleActivity.this.getWindow().getDecorView().getRootView().getHeight();
            int heightDifference = screenHeight - r.bottom;
            if (heightDifference > 0 && view != title) {
                if (operationLayout != null) {
                    operationLayout.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(operationLayout.getLayoutParams());
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.bottomMargin = heightDifference;
                    operationLayout.setLayoutParams(layoutParams);
                }
            } else {
                if (operationLayout != null) {
                    operationLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        CustomDialog dialog = new CustomDialog(CreateArticleActivity.this);
        dialog.setMessage("是否将本次编辑保存为草稿？保存后下次可以继续编写");
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
        swipeRefreshLayout = findViewById(R.id.activity_create_article_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    void changeColor(int id, boolean isSelected, boolean isEnable) {
        if (isEnable) {
            if (isSelected) {
                ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.color_fe0000));
            } else {
                ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.color_selector_icon));
            }
        } else {
            ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.text_color_light_gray));
        }
    }

    void setPenColor(int color) {
        currentPenColor = color;
        richEditor.setTextColor(currentPenColor);
        ((ImageView) findViewById(R.id.activity_create_article_operation_pencolor)).setColorFilter(currentPenColor);
        findViewById(R.id.activity_create_article_divider).setVisibility(View.GONE);
        findViewById(R.id.activity_create_article_operation_color_layout).setVisibility(View.GONE);
        isSelectingPenColor = false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_create_article_operation_highlight:
                againEdit();
                if (!isTextBackGround)
                    richEditor.setTextBackgroundColor(getColor(R.color.tag_background));
                else
                    richEditor.setTextBackgroundColor(getColor(R.color.content_background));
                isTextBackGround = !isTextBackGround;
                changeColor(view.getId(), isTextBackGround, true);
                break;
            case R.id.activity_create_article_operation_fontsmall:
                againEdit();
                if (currentTextSize > minTextSize) {
                    currentTextSize--;
                    changeColor(view.getId(), false, true);
                    changeColor(R.id.activity_create_article_operation_fontbig, false, true);
                } else {
                    changeColor(view.getId(), false, false);
                }
                richEditor.setFontSize(currentTextSize);
                break;
            case R.id.activity_create_article_operation_fontbig:
                againEdit();
                if (currentTextSize < maxTextSize) {
                    currentTextSize++;
                    changeColor(view.getId(), false, true);
                    changeColor(R.id.activity_create_article_operation_fontsmall, false, true);
                } else {
                    changeColor(view.getId(), false, false);
                }
                richEditor.setFontSize(currentTextSize);
                break;
            case R.id.activity_create_article_operation_bold:
                againEdit();
                richEditor.setBold();
                break;
            case R.id.activity_create_article_operation_italic:
                againEdit();
                richEditor.setItalic();
                break;
            case R.id.activity_create_article_operation_underline:
                againEdit();
                richEditor.setUnderline();
                break;
            case R.id.activity_create_article_operation_subscript:
                againEdit();
                richEditor.setSubscript();
                break;
            case R.id.activity_create_article_operation_superscript:
                againEdit();
                richEditor.setSuperscript();
                break;
            case R.id.activity_create_article_operation_strikethrough:
                againEdit();
                richEditor.setStrikeThrough();
                break;
            case R.id.activity_create_article_operation_ol:
                againEdit();
                richEditor.setNumbers();
                break;
            case R.id.activity_create_article_operation_ul:
                againEdit();
                richEditor.setBullets();
                break;
            case R.id.activity_create_article_operation_indent:
                againEdit();
                richEditor.setIndent();
                break;
            case R.id.activity_create_article_operation_outdent:
                againEdit();
                richEditor.setOutdent();
                break;
            case R.id.activity_create_article_operation_heading:
                againEdit();
                richEditor.setHeading(3);
                break;
            case R.id.activity_create_article_operation_pencolor:
                againEdit();
                findViewById(R.id.activity_create_article_divider).setVisibility(View.VISIBLE);
                findViewById(R.id.activity_create_article_operation_color_layout).setVisibility(View.VISIBLE);
                isSelectingPenColor = true;
                break;
            case R.id.activity_create_article_operation_color_black:
                againEdit();
                setPenColor(Color.BLACK);
                break;
            case R.id.activity_create_article_operation_color_red:
                againEdit();
                setPenColor(Color.RED);
                break;
            case R.id.activity_create_article_operation_color_blue:
                againEdit();
                setPenColor(Color.BLUE);
                break;
            case R.id.activity_create_article_operation_color_orange:
                againEdit();
                setPenColor(Color.rgb(255, 187, 51));
                break;
            case R.id.activity_create_article_operation_color_yellow:
                againEdit();
                setPenColor(Color.YELLOW);
                break;
            case R.id.activity_create_article_operation_color_purple:
                againEdit();
                setPenColor(Color.rgb(170, 102, 204));
                break;
            case R.id.activity_create_article_operation_color_pink:
                againEdit();
                setPenColor(Color.rgb(255, 20, 147));
                break;
            case R.id.activity_create_article_operation_color_green:
                againEdit();
                setPenColor(Color.GREEN);
                break;
            case R.id.activity_create_article_operation_quote:
                againEdit();
                richEditor.insertHorizontalRule();
                break;
            case R.id.activity_create_article_operation_todo:
                againEdit();
                richEditor.insertTodo();
                break;
            case R.id.activity_create_article_operation_leftalignment:
                againEdit();
                richEditor.setAlignLeft();
                break;
            case R.id.activity_create_article_operation_centeralignment:
                againEdit();
                richEditor.setAlignCenter();
                break;
            case R.id.activity_create_article_operation_rightalignment:
                againEdit();
                richEditor.setAlignRight();
                break;
            case R.id.activity_create_article_operation_picture:
                if (!TextUtils.isEmpty(richEditor.getHtml())) {
                    ArrayList<String> arrayList = RichEditorUtil.returnImageUrlsFromHtml(richEditor.getHtml());
                    if (arrayList != null && arrayList.size() >= 9) {
                        Toast.makeText(CreateArticleActivity.this, "最多添加9张照片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                selectImage(104, selectImages);
                KeyBoardUtil.hideKeyBoard(CreateArticleActivity.this);
                break;
            case R.id.activity_create_article_operation_undo:
                richEditor.undo();
                break;
            case R.id.activity_create_article_operation_redo:
                richEditor.redo();
                break;
        }
    }

    public void selectImage(int requestCode, ArrayList<ImageItem> imageItems) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, requestCode);
    }

    private void againEdit() {
        richEditor.focusEditor();
        KeyBoardUtil.openKeybord(title, CreateArticleActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (requestCode == 104) {
                selectImages.clear();
                selectImages.addAll((ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
                againEdit();
                Log.d("xuruida", String.valueOf(BitmapUtil.getAspectRatio(selectImages.get(0).path)));
                richEditor.insertImage(selectImages.get(0).path, "picture", 300, (int) (BitmapUtil.getAspectRatio(selectImages.get(0).path) * 300));
                KeyBoardUtil.openKeybord(title, CreateArticleActivity.this);
                richEditor.postDelayed(() -> {
                    if (richEditor != null)
                        richEditor.scrollToBottom();
                    Log.d("xuruida", richEditor.getHtml());
                }, 200);
            }
        } else if (resultCode == -1) {
            if (requestCode == 11) {
                String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                if (!TextUtils.isEmpty(outPath)) {
                    String newHtml = richEditor.getHtml().replace(currentUrl, outPath);
                    richEditor.setHtml(newHtml);
                    currentUrl = "";
                }
            }
        }
    }
}
