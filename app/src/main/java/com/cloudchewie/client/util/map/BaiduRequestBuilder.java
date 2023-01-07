package com.cloudchewie.client.util.map;

import static com.cloudchewie.client.util.basic.Constant.BAIDU_POI_BASE_URL;
import static com.cloudchewie.client.util.basic.Constant.BAIDU_SECRET_KEY_WEB;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class BaiduRequestBuilder {
    //公园、动物园、植物园、游乐园、博物馆、水族馆、海滨浴场、文物古迹、教堂、风景区、景点、寺庙、其他
    public static String TAG_ATTRACTION = "旅游景点";
    //度假村、农家院
    public static String TAG_ENTAINMENT = "休闲娱乐";
    //岛屿、山峰、水系、其他
    public static String TAG_NATURE = "自然地物";
    //艺术团体、美术馆、展览馆、文化宫、其他
    public static String TAG_CULTURE = "文化传媒";
    //高等院校、图书馆、科技馆、其他
    public static String TAG_TEACH = "教育培训";
    private static BaiduRequestBuilder builder;
    List<String> queryList;
    String tag;
    int pageSize;
    int pageNum;
    String ak;
    SCOPE_TYPE scope;
    SEARCH_TYPE type;
    OUTPUT_TYPE output;
    //行政区域检索参数
    String region;
    //圆形区域检索参数
    double[] centroid;
    int radius;
    //多边形区域检索参数
    List<double[]> pointList;

    public BaiduRequestBuilder() {
        setAk(BAIDU_SECRET_KEY_WEB);
        if (type == null)
            type = SEARCH_TYPE.DEFAULT;
        if (output == null)
            output = OUTPUT_TYPE.JSON;
        if (scope == null)
            scope = SCOPE_TYPE.SIMPLE;
    }

    @NonNull
    @Contract(" -> new")
    public static BaiduRequestBuilder init() {
        return new BaiduRequestBuilder();
    }

    public static BaiduRequestBuilder queryOf(String query) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.query(query);
    }

    public static BaiduRequestBuilder queryOf(List<String> query) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.query(query);
    }

    public static BaiduRequestBuilder tagOf(String tag) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.tag(tag);
    }

    public static BaiduRequestBuilder pageSizeOf(int pageSize) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.pageSize(pageSize);
    }

    public static BaiduRequestBuilder pageNumOf(int pageNum) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.pageNum(pageNum);
    }

    public static BaiduRequestBuilder scopeOf(SCOPE_TYPE scope) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.scope(scope);
    }

    public static BaiduRequestBuilder regionOf(String region) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.region(region);
    }

    public static BaiduRequestBuilder akOf(String ak) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.ak(ak);
    }

    public static BaiduRequestBuilder centroidOf(double lat, double lon) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.centroid(lat, lon);
    }

    public static BaiduRequestBuilder radiusOf(int radius) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.radius(radius);
    }

    public static BaiduRequestBuilder typeOf(SEARCH_TYPE type) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.type(type);
    }

    public static BaiduRequestBuilder pointOf(double lat, double lon) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.point(lat, lon);
    }

    public static BaiduRequestBuilder pointOf(List<double[]> points) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.point(points);
    }

    public static BaiduRequestBuilder outputOf(OUTPUT_TYPE output) {
        if (builder == null)
            builder = new BaiduRequestBuilder();
        return builder.output(output);
    }

    public String build() {
        StringBuilder url = new StringBuilder(BAIDU_POI_BASE_URL);
        url.append("?").append("scope=").append(scope == SCOPE_TYPE.SIMPLE ? 0 : 1);
//        url.append("&").append("tag=").append(tag);
        url.append("&").append("page_num=").append(pageNum);
        url.append("&").append("page_size=").append(pageSize);
        url.append("&").append("ak=").append(ak);
        url.append("&").append("output=").append(output == OUTPUT_TYPE.JSON ? "json" : "xml");
        switch (type) {
            case DEFAULT:
                if (queryList == null)
                    queryList = new ArrayList<>();
                queryList.add(tag);
                url.append("&").append("query=");
                url.append(queryList.get(0));
                url.append("&").append("region=").append(region);
                break;
            case CIRCLE:
                if (queryList == null)
                    queryList = new ArrayList<>();
                queryList.add(tag);
                url.append("&").append("query=");
                for (int i = 0; i < queryList.size(); i++)
                    url.append(queryList.get(i)).append(i < queryList.size() - 1 ? "$" : "");
                if (centroid != null)
                    url.append("&").append("location=").append(centroid[0]).append(",").append(centroid[1]);
                url.append("&").append("radius=").append(radius);
                break;
            case POLYGON:
                if (queryList == null)
                    queryList = new ArrayList<>();
                queryList.add(tag);
                url.append("&").append("query=");
                for (int i = 0; i < queryList.size(); i++)
                    url.append(queryList.get(i)).append(i < queryList.size() - 1 ? "$" : "");
                if (pointList != null) {
                    url.append("&").append("bounds=");
                    for (int i = 0; i < pointList.size(); i++)
                        url.append(pointList.get(i)[0]).append(",").append(pointList.get(i)[1]).append(i < pointList.size() - 1 ? "," : "");
                }
                break;
        }
        return url.toString();
    }

    public BaiduRequestBuilder query(String query) {
        addQuery(query);
        return this;
    }

    public BaiduRequestBuilder query(List<String> query) {
        addQuery(query);
        return this;
    }

    public BaiduRequestBuilder tag(String tag) {
        setTag(tag);
        return this;
    }

    public BaiduRequestBuilder pageSize(int pageSize) {
        setPageSize(pageSize);
        return this;
    }

    public BaiduRequestBuilder output(OUTPUT_TYPE output) {
        setOutput(output);
        return this;
    }

    public BaiduRequestBuilder pageNum(int pageNum) {
        setPageNum(pageNum);
        return this;
    }

    public BaiduRequestBuilder scope(SCOPE_TYPE scope) {
        setScope(scope);
        return this;
    }

    public BaiduRequestBuilder region(String region) {
        setRegion(region);
        return this;
    }

    public BaiduRequestBuilder ak(String ak) {
        setAk(ak);
        return this;
    }

    public BaiduRequestBuilder centroid(double lat, double lon) {
        setCentroid(lat, lon);
        return this;
    }

    public BaiduRequestBuilder radius(int radius) {
        setRadius(radius);
        return this;
    }

    public BaiduRequestBuilder type(SEARCH_TYPE type) {
        setType(type);
        return this;
    }

    public BaiduRequestBuilder point(double lat, double lon) {
        addPoint(lat, lon);
        return this;
    }

    public BaiduRequestBuilder point(List<double[]> points) {
        addPoint(points);
        return this;
    }

    public void setQueryList(List<String> queryList) {
        this.queryList = queryList;
    }

    public void addQuery(String query) {
        if (queryList == null)
            queryList = new ArrayList<>();
        this.queryList.add(query);
    }

    public void addQuery(List<String> queryList) {
        if (this.queryList == null)
            this.queryList = new ArrayList<>();
        this.queryList.addAll(queryList);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public void setScope(SCOPE_TYPE scope) {
        this.scope = scope;
    }

    public void setType(SEARCH_TYPE type) {
        this.type = type;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public void setCentroid(double lat, double lon) {
        double[] point = new double[2];
        point[0] = lat;
        point[1] = lon;
        this.centroid = point;
    }

    public void setOutput(OUTPUT_TYPE output) {
        this.output = output;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setPointList(List<double[]> pointList) {
        this.pointList = pointList;
    }

    public void addPoint(double lat, double lon) {
        if (this.pointList == null)
            this.pointList = new ArrayList<>();
        double[] point = new double[2];
        point[0] = lat;
        point[1] = lon;
        pointList.add(point);
    }

    public void addPoint(List<double[]> pointList) {
        if (this.pointList == null)
            this.pointList = new ArrayList<>();
        this.pointList.addAll(pointList);
    }

    public enum SEARCH_TYPE {
        DEFAULT, CIRCLE, POLYGON
    }

    public enum SCOPE_TYPE {
        SIMPLE, DETAIL
    }

    public enum OUTPUT_TYPE {
        JSON, XML
    }
}
