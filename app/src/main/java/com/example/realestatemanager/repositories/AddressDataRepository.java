package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.database.dao.AddressDao;
import com.example.realestatemanager.models.Address;

import java.util.List;

public class AddressDataRepository {

    private final AddressDao addressDao;

    public AddressDataRepository(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    //GET PLACE
    public LiveData<Address> getAddress(long placeId) {
        return this.addressDao.getAddress(placeId);
    }

    public LiveData<List<Long>> getPlaceIfForGivenParamFromAdresses(SimpleSQLiteQuery query) {
        return addressDao.getPlacesIdForGivenParameters(query);
    }

    public LiveData<List<Address>> getAddresses() {
        return this.addressDao.getAddresses();
    }

    //CREATE
    public long createAddress(Address address) {
        return addressDao.createAddress(address);
    }

    //UPDATE
    public void updateAddress(Address address) {
        addressDao.updateAddress(address);
    }
}
