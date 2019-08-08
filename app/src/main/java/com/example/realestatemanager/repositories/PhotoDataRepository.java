package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.database.dao.InterestDao;
import com.example.realestatemanager.database.dao.PhotoDao;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;

import java.util.List;

public class PhotoDataRepository {

    private final PhotoDao photoDao;

    public PhotoDataRepository(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    public LiveData<List<Photo>> getPhotos() {
        return this.photoDao.getPhotos();
    }

    public LiveData<List<Photo>> getPhotosForAPlace(long placeId) {
        return this.photoDao.getPhotosForAPlace(placeId);
    }

    //CREATE
    public long createPhoto(Photo photo) {
        return photoDao.createPhoto(photo);
    }

    //UPDATE
    public int updatePhoto(Photo photo) {
        return photoDao.updatePhoto(photo);
    }

    //DELETE
    public void deletePhoto(long photoId) {
        photoDao.deletePhoto(photoId);
    }
}
