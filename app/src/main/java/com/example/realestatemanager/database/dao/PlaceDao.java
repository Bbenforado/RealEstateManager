package com.example.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.fragments.ListFragment;
import com.example.realestatemanager.models.Place;
import com.google.android.material.shape.ShapePath;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM places")
    LiveData<List<Place>> getPlaces();

    @Query("SELECT * FROM places WHERE id = :placeId")
    LiveData<Place> getPlace(long placeId);

    @RawQuery(observedEntities = Place.class)
    LiveData<List<Place>> getPlacesForGivenParameters(SupportSQLiteQuery query);

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPlace(Place place);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPlace(Place place);

    @Insert
    long insertPlace(Place place);

    @Update
    int updatePlace(Place place);

    @Query("DELETE FROM places WHERE id = :placeId")
    int deletePlace(long placeId);
}
