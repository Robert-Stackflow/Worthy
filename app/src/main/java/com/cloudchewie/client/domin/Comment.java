/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.domin;

import android.media.Image;

import java.util.Date;
import java.util.List;

public class Comment {
    int userId;
    String username;
    Date date;
    String content;
    List<Image> images;
    int commentCount;
    int thumbupCount;
    COMMENT_TYPE type;

    enum COMMENT_TYPE {
        TEXT,
        TEXT_IMAGE
    }
}
