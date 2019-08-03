package com.example.realestatemanager.models;


import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "places")
public class Place {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int nbrOfRooms;
    private int nbrOfBathrooms;
    private int nbrOfBedrooms;
    private long surface;
    private String type;
    private long price;
    private Date creationDate;
    private Date dateOfSale;
    private String author;
    private String description;

    @Ignore
    public Place() {

    }

    public Place(int nbrOfRooms, int nbrOfBathrooms, int nbrOfBedrooms, String type, long price, Date creationDate, Date dateOfSale, String author, String description, long surface) {
        this.nbrOfRooms = nbrOfRooms;
        this.nbrOfBathrooms = nbrOfBathrooms;
        this.nbrOfBedrooms = nbrOfBedrooms;
        this.type = type;
        this.price = price;
        this.creationDate = creationDate;
        this.dateOfSale = dateOfSale;
        this.author = author;
        this.description = description;
        this.surface = surface;
    }

    //----------------------------------
    //GETTERS
    //-----------------------------------

    public long getId() {
        return id;
    }

    public int getNbrOfRooms() {
        return nbrOfRooms;
    }

    public int getNbrOfBathrooms() {
        return nbrOfBathrooms;
    }

    public int getNbrOfBedrooms() {
        return nbrOfBedrooms;
    }

    public String getType() {
        return type;
    }

    public long getPrice() {
        return price;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public long getSurface() {
        return surface;
    }

    //----------------------------------
    //SETTERS
    //-----------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public void setNbrOfRooms(int nbrOfRooms) {
        this.nbrOfRooms = nbrOfRooms;
    }

    public void setNbrOfBathrooms(int nbrOfBathrooms) {
        this.nbrOfBathrooms = nbrOfBathrooms;
    }

    public void setNbrOfBedrooms(int nbrOfBedrooms) {
        this.nbrOfBedrooms = nbrOfBedrooms;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSurface(long surface) {
        this.surface = surface;
    }


    //FOR CONTENT PROVIDER
    public static Place fromContentValues(ContentValues values) {
        final Place place = new Place();
        if (values.containsKey("type")) place.setType(values.getAsString("type"));
        if (values.containsKey("price")) place.setPrice(values.getAsInteger("price"));
        if (values.containsKey("surface")) place.setSurface(values.getAsLong("surface"));
        if (values.containsKey("nbrOfRooms")) place.setNbrOfRooms(values.getAsInteger("nbrOfRooms"));
        if (values.containsKey("nbrOfBedrooms")) place.setNbrOfBedrooms(values.getAsInteger("nbrOfBedrooms"));
        if (values.containsKey("nbrOfBathrooms")) place.setNbrOfBathrooms(values.getAsInteger("nbrOfBathrooms"));
        if (values.containsKey("author")) place.setAuthor(values.getAsString("author"));
        if (values.containsKey("description")) place.setDescription(values.getAsString("description"));
        return place;
    }
}
