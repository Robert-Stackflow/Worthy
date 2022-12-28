package com.cloudchewie.client.util.ui;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichEditorUtil {

    /**
     * 截取富文本中的图片链接
     */
    public static ArrayList<String> returnImageUrlsFromHtml(String content) {
        ArrayList<String> imageSrcList = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            return imageSrcList;
        }
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*(['\"])?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        String quote;
        String src;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }

        return imageSrcList;
    }


    /**
     * 截取富文本中的纯文本内容
     */
    public static String returnOnlyText(String htmlStr) {
        if (TextUtils.isEmpty(htmlStr)) {
            return "";
        } else {
            String regFormat = "\\s*|\t|\r|\n";
            String regTag = "<[^>]*>";
            String text = htmlStr.replaceAll(regFormat, "").replaceAll(regTag, "");
            text = text.replace("&nbsp;", "");
            return text;
        }
    }


    public static boolean isEmpty(String htmlStr) {
        ArrayList<String> images = returnImageUrlsFromHtml(htmlStr);
        String text = returnOnlyText(htmlStr);
        return TextUtils.isEmpty(text) && images.size() == 0;
    }
}