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
    private long idPhoto;
    private String uri;
    private long placeId;
    private String descriptionPhoto;

    @Ignore
    public Photo() {

    }

    public Photo(String uri) {
        this.uri = uri;
    }

    public long getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(long id) {
        this.idPhoto = id;
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

    public String getDescriptionPhoto() {
        return descriptionPhoto;
    }

    public void setDescriptionPhoto(String description) {
        this.descriptionPhoto = description;
    }

    //FOR CONTENT PROVIDER
    public static Photo fromContentValues(ContentValues values, long id) {
        final Photo photo = new Photo();
        if (values.containsKey("uri")) photo.setUri(values.getAsString("uri"));
        photo.setPlaceId(id);
        if (values.containsKey("descriptionPhoto")) photo.setDescriptionPhoto(values.getAsString("descriptionPhoto"));
        return photo;
    }

    public static Photo fromContentValuesUpdate(ContentValues values) {
        final Photo photo = new Photo();
        if (values.containsKey("idPhoto")) photo.setIdPhoto(values.getAsLong("idPhoto"));
        if (values.containsKey("uri")) photo.setUri(values.getAsString("uri"));
        if (values.containsKey("placeId")) photo.setPlaceId(values.getAsLong("placeId"));
        if (values.containsKey("descriptionPhoto")) photo.setDescriptionPhoto(values.getAsString("descriptionPhoto"));
        return photo;
    }

}
