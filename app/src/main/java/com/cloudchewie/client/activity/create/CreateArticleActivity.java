package com.cloudchewie.client.activity.create;

import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudchewie.client.R;
import com.cloudchewie.client.activity.discover.ArticleDetailActivity;
import com.cloudchewie.client.activity.global.BaseActivity;
import com.cloudchewie.client.entity.Article;
import com.cloudchewie.client.util.basic.DomainUtil;
import com.cloudchewie.client.util.image.BitmapUtil;
import com.cloudchewie.client.util.ui.KeyBoardUtil;
import com.cloudchewie.client.util.ui.RichEditorUtil;
import com.cloudchewie.client.util.ui.StatusBarUtil;
import com.cloudchewie.client.util.widget.CommonPopupWindow;
import com.cloudchewie.client.util.widget.RichEditor;
import com.cloudchewie.ui.IToast;
import com.cloudchewie.ui.MyDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yalantis.ucrop.UCropActivity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class CreateArticleActivity extends BaseActivity implements View.OnClickListener {
    Article article;
    RefreshLayout swipeRefreshLayout;
    EditText title;
    EditText content;
    RichEditor richEditor;
    ConstraintLayout operationLayout;
    String currentImageUrl = "";
    String currentImageHtml = "";
    CommonPopupWindow popupWindow;
    TextView cancelButton;
    TextView publishButton;
    TextView previewButton;
    boolean isTextBackGround = false;
    int maxTitleLength = 30;
    int defaultTextSize = 15;
    int currentTextSize = 3;
    int minTextSize = 1;
    int maxTextSize = 7;
    int currentPenColor = Color.BLACK;
    boolean isSelectingPenColor = false;
    private List<ImageItem> selectImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_create_article);
        title = findViewById(R.id.activity_create_article_title);
        content = findViewById(R.id.activity_create_article_content);
        richEditor = findViewById(R.id.activity_create_article_editor);
        operationLayout = findViewById(R.id.activity_create_article_operation_layout);
        cancelButton = findViewById(R.id.activity_create_article_cancel);
        previewButton = findViewById(R.id.activity_create_article_preview);
        publishButton = findViewById(R.id.activity_create_article_publish);
        operationLayout.setVisibility(View.GONE);
        initView();
        initEditor();
        initSwipeRefresh();
        initImagePopupWindow();
        setPublishEnabled(false);
    }

    void setPublishEnabled(boolean isEnabled) {
        if (isEnabled) {
            publishButton.setSelected(true);
            publishButton.setEnabled(true);
            previewButton.setSelected(true);
            previewButton.setEnabled(true);
            publishButton.setTextColor(getColor(R.color.color_prominent));
            previewButton.setTextColor(getColor(R.color.color_prominent));
        } else {
            publishButton.setSelected(false);
            publishButton.setEnabled(false);
            previewButton.setSelected(false);
            previewButton.setEnabled(false);
            publishButton.setTextColor(getColor(R.color.color_light_gray));
            previewButton.setTextColor(getColor(R.color.color_light_gray));
        }
    }

    void initEditor() {
        richEditor.setEditorHeight(650);
        richEditor.setEditorBackgroundColor(R.color.card_background);
        richEditor.setEditorFontSize(defaultTextSize);
        richEditor.setEditorFontColor(getColor(R.color.color_accent));
        richEditor.setTextColor(getColor(R.color.color_accent));
        richEditor.setPlaceholder("分享你的打卡经历");
        richEditor.setInputEnabled(true);
        richEditor.setOnTextChangeListener(text -> {
            if (TextUtils.isEmpty(title.getText().toString().trim())) {
                setPublishEnabled(false);
                return;
            }
            if (TextUtils.isEmpty(text)) setPublishEnabled(false);
            else setPublishEnabled(!TextUtils.isEmpty(Html.fromHtml(text)));
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
                    setPublishEnabled(false);
                    return;
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(html))) {
                        setPublishEnabled(false);
                        return;
                    } else setPublishEnabled(true);
                }
                setPublishEnabled(!TextUtils.isEmpty(s.toString()));
            }
        });
        richEditor.setOnDecorationChangeListener((text, types) -> {
            ArrayList<String> flagArr = new ArrayList<>();
            for (int i = 0; i < types.size(); i++)
                flagArr.add(types.get(i).name());
            if (flagArr.contains("BOLD")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_bold)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_bold)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("ITALIC")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_italic)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_italic)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("UNDERLINE")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_underline)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_underline)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("STRIKETHROUGH")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_strikethrough)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_strikethrough)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("ORDEREDLIST")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_red));
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_selector_icon));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("UNORDEREDLIST")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_red));
                ((ImageView) findViewById(R.id.activity_create_article_operation_ol)).setColorFilter(getColor(R.color.color_selector_icon));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_ul)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("SUPERSCRIPT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_superscript)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_superscript)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("SUBSCRIPT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_subscript)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_subscript)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYLEFT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_leftalignment)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_leftalignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYCENTER")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_centeralignment)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_centeralignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
            if (flagArr.contains("JUSTIFYRIGHT")) {
                ((ImageView) findViewById(R.id.activity_create_article_operation_rightalignment)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(R.id.activity_create_article_operation_rightalignment)).setColorFilter(getColor(R.color.color_selector_icon));
            }
        });
        richEditor.setImageClickListener(imageUrl -> {
            try {
                imageUrl = URLDecoder.decode(imageUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            currentImageUrl = imageUrl;
            currentImageHtml = getImageHtml(currentImageUrl);
            popupWindow.showBottom(getWindow().getDecorView().findViewById(android.R.id.content), 0.5f);
        });
    }

    private void initImagePopupWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(CreateArticleActivity.this).inflate(R.layout.layout_post_image_operation, null);
        view.findViewById(R.id.linear_cancle).setOnClickListener(v -> popupWindow.dismiss());
        view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
            if (currentImageUrl.endsWith(".gif")) {
                IToast.makeTextBottom(this, "暂不支持编辑.gif类型的图片", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CreateArticleActivity.this, UCropActivity.class);
                intent.putExtra("filePath", currentImageUrl);
                String destDir = getFilesDir().getAbsolutePath();
                String fileName = "img_" + System.currentTimeMillis() + ".png";
                intent.putExtra("outPath", destDir + fileName);
                startActivityForResult(intent, 11);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.linear_delete_pic).setOnClickListener(v -> {
            richEditor.setHtml(richEditor.getHtml().replace(currentImageHtml, ""));
            if (RichEditorUtil.isEmpty(richEditor.getHtml())) richEditor.setHtml("");
            currentImageUrl = "";
            currentImageHtml = "";
            popupWindow.dismiss();
        });
        view.findViewById(R.id.linear_download_pic).setOnClickListener(v -> {
            IToast.makeTextBottom(this, "图片保存成功", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
            currentImageUrl = "";
            currentImageHtml = "";
        });
        popupWindow = new CommonPopupWindow.Builder(CreateArticleActivity.this).setView(view).setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).setOutsideTouchable(true).setAnimationStyle(R.style.UpDownAnimation).create();
        popupWindow.setOnDismissListener(() -> richEditor.setInputEnabled(true));
    }

    String getImageHtml(String url) {
        int width = 300;
        int height = (int) (BitmapUtil.getAspectRatio(url) * 300);
        return "<img src=\"" + url + "\" alt=\"" + url + "\" width=\"" + width + "\" height=\"" + height + "\">";
    }

    void initView() {
        title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxTitleLength)});
        cancelButton.setOnClickListener(v -> {
            MyDialog dialog = new MyDialog(CreateArticleActivity.this);
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
        previewButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateArticleActivity.this, ArticlePreviewActivity.class);
            Article article = DomainUtil.getArticle(this);
            article.setThumbupCount(0);
            article.setCollectionCount(0);
            article.setCommentCount(0);
            article.setContent(richEditor.getHtml());
            article.setTitle(title.getText().toString());
            intent.putExtra("article", article);
            startActivity(intent);
        });
        publishButton.setOnClickListener(v -> {
            if (richEditor.getHtml().length() < 200)
                IToast.makeTextTop(this, "您的文章字数过少,暂时无法发布!", Toast.LENGTH_SHORT).show();
            else {
                IToast.makeTextBottom(this, "文章发布成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateArticleActivity.this, ArticleDetailActivity.class);
                Article article = DomainUtil.getArticle(this);
                article.setContent(richEditor.getHtml());
                intent.putExtra("article", article);
                startActivity(intent);
            }
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
        richEditor.setOnFocusChangeListener((v, b) -> {
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
        Intent intent = getIntent();
        if (intent != null) {
            Serializable object = intent.getSerializableExtra("article");
            if (object instanceof Article) {
                article = (Article) object;
                title.setText(article.getTitle());
                richEditor.setHtml(article.getContent());
            }
        }
    }

    @Override
    public void onBackPressed() {
        MyDialog dialog = new MyDialog(CreateArticleActivity.this);
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
        swipeRefreshLayout = findViewById(R.id.activity_create_article_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    void changeColor(int id, boolean isSelected, boolean isEnable) {
        if (isEnable) {
            if (isSelected) {
                ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.color_red));
            } else {
                ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.color_selector_icon));
            }
        } else {
            ((ImageView) findViewById(id)).setColorFilter(getColor(R.color.color_light_gray));
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
                else richEditor.setTextBackgroundColor(getColor(R.color.content_background));
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
                setPenColor(getColor(R.color.color_accent));
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

    public void selectImage(int requestCode, List<ImageItem> imageItems) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(true);
        Intent intent = new Intent(this, ImageGridActivity.class);
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
                for (ImageItem imageItem : selectImages) {
                    String url = imageItem.path;
                    int width = 300;
                    int height = (int) (BitmapUtil.getAspectRatio(url) * 300);
                    richEditor.setAlignCenter();
                    richEditor.insertImage(url, url, width, height);
                }
                selectImages.clear();
                currentImageUrl = "";
                currentImageHtml = "";
                KeyBoardUtil.openKeybord(title, CreateArticleActivity.this);
                richEditor.postDelayed(() -> {
                    if (richEditor != null) richEditor.scrollToBottom();
                }, 200);
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                if (!TextUtils.isEmpty(outPath)) {
                    richEditor.setAlignCenter();
                    richEditor.setHtml(richEditor.getHtml().replace(currentImageHtml, getImageHtml(outPath)));
                    currentImageUrl = "";
                    currentImageHtml = "";
                }
            }
        }
    }
}
