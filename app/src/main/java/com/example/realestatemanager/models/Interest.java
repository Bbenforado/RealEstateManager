package com.example.realestatemanager.models;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "interests",
        foreignKeys =
        @ForeignKey(entity = Place.class,
        parentColumns = "id",
        childColumns = "idPlace"))/*,
        @ForeignKey(entity = Address.class,
        parentColumns = "id",
        childColumns = "idAddress")})*/
public class Interest {

    @PrimaryKey(autoGenerate = true)
    private long idInterest;
    private String interestType;
    private long idPlace;
    private long idAddressForInterest;

    @Ignore
    public Interest() {

    }

    public Interest(String interestType, long idPlace) {
        this.interestType = interestType;
        this.idPlace = idPlace;
    }

    //-------------------------
    //GETTERS
    //------------------------
    public long getIdAddressForInterest() {
        return idAddressForInterest;
    }

    public String getInterestType() {
        return interestType;
    }

    public long getIdInterest() {
        return idInterest;
    }

    public long getIdPlace() {
        return idPlace;
    }
    //--------------------
    //SETTERS
    //--------------------
    public void setIdAddressForInterest(long idAddress) {
        this.idAddressForInterest = idAddress;
    }

    public void setInterestType(String type) {
        this.interestType = type;
    }

    public void setIdInterest(long id) {
        this.idInterest = id;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    //FOR CONTENT PROVIDER
    public static Interest fromContentValues(ContentValues values) {
        final Interest interest = new Interest();
        if (values.containsKey("type")) interest.setInterestType(values.getAsString("type"));
        if (values.containsKey("idPlace")) interest.setIdPlace(values.getAsLong("idPlace"));
        return interest;
    }
}
