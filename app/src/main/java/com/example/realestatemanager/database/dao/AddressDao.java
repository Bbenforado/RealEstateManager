package com.example.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Place;

import java.util.List;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM addresses")
    LiveData<List<Address>> getAddresses();

    @Query("SELECT * FROM addresses WHERE idPlace = :placeId")
    LiveData<Address> getAddress(long placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createAddress(Address address);

    @Insert
    long insertAddress(Address address);

    @Update
    int updateAddress(Address address);

    @Query("DELETE FROM addresses WHERE id = :addressId")
    int deleteAddress(long addressId);
}
