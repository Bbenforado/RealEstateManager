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
    private long id;
    private String type;
    private long idPlace;
    private long idAddress;

    @Ignore
    public Interest() {

    }

    public Interest(String type, long idPlace) {
        this.type = type;
        this.idPlace = idPlace;
    }

    //-------------------------
    //GETTERS
    //------------------------
    public long getIdAddress() {
        return idAddress;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public long getIdPlace() {
        return idPlace;
    }
    //--------------------
    //SETTERS
    //--------------------
    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    //FOR CONTENT PROVIDER
    public static Interest fromContentValues(ContentValues values) {
        final Interest interest = new Interest();
        if (values.containsKey("type")) interest.setType(values.getAsString("type"));
        if (values.containsKey("idPlace")) interest.setIdPlace(values.getAsLong("idPlace"));
        return interest;
    }
}
