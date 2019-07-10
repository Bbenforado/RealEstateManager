package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.database.dao.AddressDao;
import com.example.realestatemanager.database.dao.PlaceDao;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Place;

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

    public LiveData<List<Address>> getAddresses() {
        return this.addressDao.getAddresses();
    }

    //CREATE
    public long createAddress(Address address) {
        System.out.println("repository create address");
        return addressDao.createAddress(address);
    }
}
