package com.example.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.realestatemanager.models.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos")
    LiveData<List<Photo>> getPhotos();

    @Query("SELECT * FROM photos WHERE placeId = :idPlace")
    LiveData<Photo> getPhoto(long idPlace);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createPhoto(Photo photo);

    @Insert
    long insertPhoto(Photo photo);

    @Update
    int updatePhoto(Photo photo);

    @Query("DELETE FROM photos WHERE placeId = :id")
    int deletePhotos(long id);
}
