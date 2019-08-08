package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.database.dao.AddressDao;
import com.example.realestatemanager.database.dao.RawDao;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;

import java.util.List;

public class RawDataRepository {

    private final RawDao rawDao;

    public RawDataRepository(RawDao rawDao) {
        this.rawDao = rawDao;
    }

    public LiveData<List<PlaceAddressesPhotosAndInterests>> getPlacesAndData(SimpleSQLiteQuery query) {
        return rawDao.getPlacesAndData(query);
    }

    public LiveData<PlaceAddressesPhotosAndInterests> getPlaceAndAddress(SimpleSQLiteQuery query) {
        return rawDao.getplaceAndAddress(query);
    }
}
