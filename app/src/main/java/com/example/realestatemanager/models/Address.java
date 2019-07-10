package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "addresses", foreignKeys =
        @ForeignKey(entity = Place.class,
                parentColumns = "id",
                childColumns = "idPlace"))
public class Address {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int streetNumber;
    private String streetName;
    private String complement;
    private String postalCode;
    private String city;
    private String country;
    private long idPlace;
    private long idInterest;

    //for interest
    /*public Address(int streetNumber, String streetName, String complement, String postalCode,
                   String country, String city, long idInterest) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.complement = complement;
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;
        this.idInterest = idInterest;
    }*/

    //for place
    public Address(long idPlace, int streetNumber, String streetName, String complement, String postalCode, String city,
                   String country) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.complement = complement;
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;
        this.idPlace = idPlace;
    }

    //---------------------------------
    //GETTERS
    //---------------------------------

    public long getId() {
        return id;
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

    public long getIdPlace() {
        return idPlace;
    }

    public long getIdInterest() {
        return idInterest;
    }

    public String getCity() {
        return city;
    }
    //---------------------------------------
    //SETTERS
    //----------------------------------------

    public void setId(long id) {
        this.id = id;
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

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    public void setIdInterest(long idInterest) {
        this.idInterest = idInterest;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
