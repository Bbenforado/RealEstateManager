package com.example.realestatemanager.models;

public class PlaceIdAndAddressId {

    public long id;
    public long addressId;
    public String latLng;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public PlaceIdAndAddressId() {
    }

    public long getId() {
        return id;
    }

    public void setId(long placeId) {
        this.id = placeId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }
}
