package com.example.realestatemanager.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realestatemanager.models.Address;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM addresses WHERE addressId = :idAddressInPlace")
    LiveData<Address> getAddressOfAPlace(long idAddressInPlace);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createAddress(Address address);

    @Update
    int updateAddress(Address address);
}
