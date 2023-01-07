package com.cloudchewie.client.bean;

import androidx.annotation.NonNull;

public class ReverseGeoCoding {
    PoiLocation location;
    String formatted_address;
    String business;
    AddressComponent addressComponent;

    public ReverseGeoCoding(PoiLocation location, String formatted_address, String business) {
        this.location = location;
        this.formatted_address = formatted_address;
        this.business = business;
    }

    public ReverseGeoCoding() {
    }

    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }

    public PoiLocation getLocation() {
        return location;
    }

    public void setLocation(PoiLocation location) {
        this.location = location;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReverseGeoCoding{" +
                "location=" + location +
                ", formatted_address='" + formatted_address + '\'' +
                ", business='" + business + '\'' +
                ", addressComponent=" + addressComponent +
                '}';
    }
}
