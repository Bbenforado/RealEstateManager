package com.example.realestatemanager.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos", foreignKeys = @ForeignKey(entity = Place.class,
        parentColumns = "id",
        childColumns = "placeId"))
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String uri;
    private long placeId;
    private String description;

    @Ignore
    public Photo() {

    }

    public Photo(String uri) {
        this.uri = uri;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //FOR CONTENT PROVIDER
    public static Photo fromContentValues(ContentValues values) {
        final Photo photo = new Photo();
        if (values.containsKey("uri")) photo.setUri(values.getAsString("uri"));
        if (values.containsKey("placeId")) photo.setPlaceId(values.getAsLong("placeId"));
        if (values.containsKey("description")) photo.setDescription(values.getAsString("description"));
        return photo;
    }

}
