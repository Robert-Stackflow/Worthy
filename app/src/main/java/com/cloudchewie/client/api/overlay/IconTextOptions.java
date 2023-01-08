package com.cloudchewie.client.api.overlay;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class IconTextOptions extends OverlayOptions {
    private BitmapDescriptor icon;
    private LatLng position;
    private String text;

    public IconTextOptions icon(BitmapDescriptor icon) {
        if (icon == null) {
            throw new IllegalArgumentException("BDMapSDKException: marker's icon can not be null");
        } else {
            this.icon = icon;
            return this;
        }
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    Overlay a() {
        return new IconText();
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IconTextOptions text(String text) {
        if (text == null) {
            throw new IllegalArgumentException("BDMapSDKException: marker's text can not be null");
        } else {
            this.text = text;
            return this;
        }
    }

    public IconTextOptions position(LatLng position) {
        if (position == null) {
            throw new IllegalArgumentException("BDMapSDKException: marker's position can not be null");
        } else {
            this.position = position;
            return this;
        }
    }
}
