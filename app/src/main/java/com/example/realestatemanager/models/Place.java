package com.example.realestatemanager.models;

import android.content.pm.PackageManager;

import androidx.room.Entity;
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
    private int status;
    private String creationDate;
    private String dateOfSale;
    private String author;
    private String description;

    public Place() {

    }

    //constructor with date of sale
    public Place(int nbrOfRooms, int nbrOfBathrooms, int nbrOfBedrooms, String type, long price,
                 int status, String creationDate, String dateOfSale, String author, String description, long surface) {
        this.nbrOfRooms = nbrOfRooms;
        this.nbrOfBathrooms = nbrOfBathrooms;
        this.nbrOfBedrooms = nbrOfBedrooms;
        this.type = type;
        this.price = price;
        this.status = status;
        this.creationDate = creationDate;
        this.dateOfSale = dateOfSale;
        this.author = author;
        this.description = description;
        this.surface = surface;
    }

    //constructor without date of sale
    public Place(int nbrOfRooms, int nbrOfBathrooms, int nbrOfBedrooms, String type, long price,
                 int status, String creationDate, String author, String description, long surface) {
        this.nbrOfRooms = nbrOfRooms;
        this.nbrOfBathrooms = nbrOfBathrooms;
        this.nbrOfBedrooms = nbrOfBedrooms;
        this.type = type;
        this.price = price;
        this.status = status;
        this.creationDate = creationDate;
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

    public int getStatus() {
        return status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDateOfSale() {
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

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDateOfSale(String dateOfSale) {
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
}
