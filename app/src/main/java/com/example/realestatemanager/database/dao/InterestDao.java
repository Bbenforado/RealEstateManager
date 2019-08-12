package com.example.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Place;

import java.util.List;

@Dao
public interface InterestDao {

    @Query("SELECT * FROM interests WHERE idPlace = :idPlace")
    LiveData<List<Interest>> getInterests(long idPlace);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createInterest(Interest interest);

    @Query("DELETE FROM interests WHERE idPlace = :id")
    int deleteInterests(long id);

    //FOR CONTENT PROVIDER
    @Query("SELECT * FROM interests WHERE idInterest = :id")
    Cursor getInterestsWithCursor(long id);

    @Insert
    long insertInterest(Interest interest);

    @Update
    int updateInterest(Interest interest);
}
