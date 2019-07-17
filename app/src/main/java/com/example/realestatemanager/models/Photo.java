package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos", foreignKeys = @ForeignKey(entity = Place.class,
        parentColumns = "id",
        childColumns = "placeId"))
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String uri;
    private long placeId;
    private boolean isMainPhoto;

    /*public Photo(String uri, long placeId) {
        this.uri = uri;
        this.placeId = placeId;
    }*/

    public Photo(String uri) {
        this.uri = uri;
    }

    public boolean isMainPhoto() {
        return isMainPhoto;
    }

    public void setMainPhoto(boolean mainPhoto) {
        isMainPhoto = mainPhoto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }
}
