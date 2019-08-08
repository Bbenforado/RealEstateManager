package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.database.dao.PlaceDao;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.models.PlaceIdAndAddressId;

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

    public LiveData<List<PlaceIdAndAddressId>> getPlaceAndAddressId() {
        return placeDao.getPlaceAndAddressId();
    }

    public LiveData<List<PlaceAddressesPhotosAndInterests>> getAllData() {
        return this.placeDao.getAllData();
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
