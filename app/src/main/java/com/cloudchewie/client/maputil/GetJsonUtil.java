package com.cloudchewie.client.maputil;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GetJsonUtil {
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName),
                    StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
