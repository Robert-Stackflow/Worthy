package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GeoCoding implements Serializable {
    PoiLocation location;
    int precise;
    int confidence;
    int comprehension;
    String level;

    public GeoCoding() {
    }

    public GeoCoding(PoiLocation location, int precise, int confidence, int comprehension, String level) {
        this.location = location;
        this.precise = precise;
        this.confidence = confidence;
        this.comprehension = comprehension;
        this.level = level;
    }

    @NonNull
    @Override
    public String toString() {
        return "GeoCoding{" +
                "location=" + location +
                ", precise=" + precise +
                ", confidence=" + confidence +
                ", comprehension=" + comprehension +
                ", level='" + level + '\'' +
                '}';
    }

    public PoiLocation getLocation() {
        return location;
    }

    public void setLocation(PoiLocation location) {
        this.location = location;
    }

    public int getPrecise() {
        return precise;
    }

    public void setPrecise(int precise) {
        this.precise = precise;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getComprehension() {
        return comprehension;
    }

    public void setComprehension(int comprehension) {
        this.comprehension = comprehension;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
