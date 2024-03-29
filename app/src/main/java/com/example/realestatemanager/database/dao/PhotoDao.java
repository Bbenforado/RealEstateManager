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

import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos")
    LiveData<List<Photo>> getPhotos();

    @Query("SELECT * FROM photos WHERE placeId = :idPlace")
    LiveData<List<Photo>> getPhotosForAPlace(long idPlace);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPhoto(Photo photo);

    @Update
    int updatePhoto(Photo photo);

    @Query("DELETE FROM photos WHERE idPhoto = :id")
    int deletePhoto(long id);

    //FOR CONTENT PROVIDER
    @Query("SELECT * FROM photos WHERE idPhoto = :id")
    Cursor getPhotosWithCursor(long id);

    @Insert
    long insertPhoto(Photo photo);
}
