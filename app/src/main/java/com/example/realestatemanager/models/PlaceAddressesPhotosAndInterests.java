package com.example.realestatemanager.models;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class PlaceAddressesPhotosAndInterests {

    @Embedded
    public Place place;

    /*@Relation(parentColumn = "id", entityColumn = "idPlace", entity = Address.class)
    public List<Address> address;
    @Relation(parentColumn = "id", entityColumn = "placeId", entity = Photo.class)
    public List<Photo> photos;
    @Relation(parentColumn = "id", entityColumn = "idPlace", entity = Interest.class)
    public List<Interest> interests;*/
    @Embedded
    public Address address;
    @Embedded
    public List<Photo> photos;
    @Embedded
    public List<Interest> interests;

    public PlaceAddressesPhotosAndInterests(Place place, Address address, List<Photo> photos, List<Interest> interests) {
        this.place = place;
        this.address = address;
        this.photos = photos;
        this.interests = interests;
    }

    public Place getPlace() {
        return place;
    }

    public Address getAddress() {
        return address;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Interest> getInterests() {
        return interests;
    }
}
