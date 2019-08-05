package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

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

    public LiveData<List<Place>> getPlacesForGivenParameters(SupportSQLiteQuery query) {
        return placeDao.getPlacesForGivenParameters(query);
    }

    public long getPlaceId(long addressId) {
        return placeDao.getPlaceId(addressId);
    }

    //CREATE
    public long createPlace(Place place) {
        return placeDao.createPlace(place);
    }

    //UPDATE
    public void updatePlace(Place place) {
        placeDao.updatePlace(place);
    }
}
