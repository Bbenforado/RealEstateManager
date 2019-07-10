package com.example.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;

import java.util.List;

@Dao
public interface InterestDao {

    @Query("SELECT * FROM interests")
    LiveData<List<Interest>> getInterests();

    @Query("SELECT * FROM interests WHERE idPlace = :idPlace")
    LiveData<Interest> getInterest(long idPlace);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createInterest(Interest interest);

    @Insert
    long insertInterest(Interest interest);

    @Update
    int updateInterest(Interest interest);

    @Query("DELETE FROM interests WHERE id = :id")
    int deleteInterest(long id);
}
