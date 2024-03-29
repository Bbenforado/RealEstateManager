package com.example.realestatemanager.models;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "addresses")
public class Address {

    @PrimaryKey(autoGenerate = true)
    private long addressId;
    private int streetNumber;
    private String streetName;
    private String complement;
    private String postalCode;
    private String city;
    private String country;
    private String latLng;

    @Ignore
    public Address() {

    }

    public Address(int streetNumber, String streetName, String complement, String postalCode,
                   String city, String country) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.complement = complement;
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;
    }

    //---------------------------------
    //GETTERS
    //---------------------------------
    public String getLatLng() {
        return latLng;
    }

    public long getAddressId() {
        return addressId;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getComplement() {
        return complement;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
    //---------------------------------------
    //SETTERS
    //----------------------------------------
    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }
    public void setAddressId(long id) {
        this.addressId = id;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //FOR CONTENT PROVIDER
    public static Address fromContentValues(ContentValues values) {
        final Address address = new Address();
        if (values.containsKey("streetNumber")) address.setStreetNumber(values.getAsInteger("streetNumber"));
        if (values.containsKey("streetName")) address.setStreetName(values.getAsString("streetName"));
        if (values.containsKey("complement")) address.setComplement(values.getAsString("complement"));
        if (values.containsKey("postalCode")) address.setPostalCode(values.getAsString("postalCode"));
        if (values.containsKey("country")) address.setCountry(values.getAsString("country"));
        if (values.containsKey("city")) address.setCity(values.getAsString("city"));
        if (values.containsKey("latLng")) address.setLatLng(values.getAsString("latLng"));
        return address;
    }

    public static Address fromContentValuesUpdate(ContentValues values) {
        final Address address = new Address();
        if (values.containsKey("addressId")) address.setAddressId(values.getAsLong("addressId"));
        if (values.containsKey("streetNumber")) address.setStreetNumber(values.getAsInteger("streetNumber"));
        if (values.containsKey("streetName")) address.setStreetName(values.getAsString("streetName"));
        if (values.containsKey("complement")) address.setComplement(values.getAsString("complement"));
        if (values.containsKey("postalCode")) address.setPostalCode(values.getAsString("postalCode"));
        if (values.containsKey("country")) address.setCountry(values.getAsString("country"));
        if (values.containsKey("city")) address.setCity(values.getAsString("city"));
        if (values.containsKey("latLng")) address.setLatLng(values.getAsString("latLng"));
        return address;
    }
}
