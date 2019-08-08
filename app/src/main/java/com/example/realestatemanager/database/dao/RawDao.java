package com.example.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;

import java.text.SimpleDateFormat;
import java.util.List;

@Dao
public interface RawDao {

    @RawQuery (observedEntities = {Place.class, Photo.class, Interest.class, Address.class})
    LiveData<List<PlaceAddressesPhotosAndInterests>> getPlacesAndData(SimpleSQLiteQuery query);
}
