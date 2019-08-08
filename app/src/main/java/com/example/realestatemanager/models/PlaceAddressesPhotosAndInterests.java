package com.example.realestatemanager.models;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class PlaceAddressesPhotosAndInterests {


    @Embedded
    public Place place;

    @Embedded
    public Address address;

    @Relation(parentColumn = "id",
            entityColumn = "idPlace")
    public List<Interest> interests;

    @Relation(parentColumn = "id",
            entityColumn = "placeId")
    public List<Photo> photos;

    /*@Embedded
    public Photo photo;

    @Embedded
    public Interest interest;*/

    public PlaceAddressesPhotosAndInterests() {
    }

    public Place getPlace() {
        return place;
    }

    public Address getAddress() {
        return address;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
   /* public Photo getPhoto() {
        return photo;
    }

    public Interest getInterest() {
        return interest;
    }*/

    /*public List<Photo> getPhotos() {
        return photos;
    }

    public List<Interest> getInterests() {
        return interests;
    }*/
}
