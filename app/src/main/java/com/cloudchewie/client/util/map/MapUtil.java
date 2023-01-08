package com.cloudchewie.client.util.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.cloudchewie.client.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MapUtil {
    public static final String CUSTOM_FILE_NAME_DARK = "dark.sty";
    public static final String CUSTOM_FILE_NAME_TEA = "tea.sty";
    public static String DARK_ID = "972468541ad9993b7b3a5f8bfecd3ec7";
    public static String LIGHT_ID = "20709c4bb8da59110666942b516f20b0";
    public static String BAIDUMAP_VERSION = "8.6.6";

    public static boolean isBaiduMapVersionSupport(String curVersion) {
        return compareVersion(BAIDUMAP_VERSION, curVersion);
    }

    @NonNull
    public static String getCustomStyleFilePath(@NonNull Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;
        try {
            inputStream = context.getAssets().open(customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists())
                customStyleFile.delete();
            customStyleFile.createNewFile();
            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parentPath + "/" + customStyleFileName;
    }

    private static boolean compareVersion(@NonNull String baseVersion, @NonNull String curVersion) {
        String[] baseVersionList = baseVersion.split("\\.");
        String[] curVersionList = curVersion.split("\\.");
        boolean flag = true;
        for (int i = 0; i < curVersionList.length; i++) {
            if (Integer.parseInt(baseVersionList[i]) > Integer.parseInt(curVersionList[i])) {
                flag = false;
                break;
            } else if (Integer.parseInt(baseVersionList[i]) < Integer.parseInt(curVersionList[i])) {
                break;
            } else if (Integer.parseInt(baseVersionList[i]) == Integer.parseInt(curVersionList[i])) {
            }
        }
        return flag;
    }

    public static BitmapDescriptor getMarkerIcon(int size) {
        switch (size) {
            case 0:
                return BitmapDescriptorFactory.fromResource(R.drawable.img_poi_default_24);
            case 1:
                return BitmapDescriptorFactory.fromResource(R.drawable.img_poi_default_40);
            case 2:
                return BitmapDescriptorFactory.fromResource(R.drawable.img_poi_default_80);
            default:
                return BitmapDescriptorFactory.fromResource(R.drawable.img_poi_default_24);
        }
    }

    public static void hideLogo(MapView mapView) {
        if (mapView == null) return;
        View child = mapView.getChildAt(1);
        if (child instanceof ImageView) child.setVisibility(View.GONE);
    }
}
