package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.database.dao.PlaceDao;
import com.example.realestatemanager.models.Place;

import java.util.List;

public class PlaceDataRepository {

    private final PlaceDao placeDao;


    public PlaceDataRepository(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    //GET PLACE
    public LiveData<Place> getPlace(long placeId) {
        return this.placeDao.getPlace(placeId);
    }

    public LiveData<List<Place>> getPlaces() {
        return this.placeDao.getPlaces();
    }

    //CREATE
    public long createPlace(Place place) {
        System.out.println("repository create place");
        return placeDao.createPlace(place);
    }
}
