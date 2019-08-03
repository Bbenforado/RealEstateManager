package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "interests", foreignKeys = @ForeignKey(entity = Place.class,
parentColumns = "id",
childColumns = "idPlace"))
public class Interest {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private long idPlace;

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

    public void setType(String type) {
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }
}
