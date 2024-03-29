package com.example.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.models.PlaceIdAndAddressId;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM places")
    LiveData<List<Place>> getPlaces();

    @Query("SELECT * FROM places WHERE id = :placeId")
    LiveData<Place> getPlace(long placeId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT places.id, places.type, addresses.addressId, addresses.latLng, count(*) FROM places, addresses WHERE places.idAddress = addresses.addressId GROUP BY places.id")
    LiveData<List<PlaceIdAndAddressId>> getPlaceAndAddressId();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT *, count(*) FROM places, addresses, photos, interests WHERE places.idAddress = addresses.addressId AND places.id = interests.idPlace AND places.id = photos.placeId GROUP BY places.id")
    LiveData<List<PlaceAddressesPhotosAndInterests>> getAllData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPlace(Place place);

    @Insert
    long insertPlace(Place place);

    @Update
    int updatePlace(Place place);

    //FOR CONTENT PROVIDER
    @Query("SELECT * FROM places WHERE id = :placeId")
    Cursor getPlacesWithCursor(long placeId);
}
