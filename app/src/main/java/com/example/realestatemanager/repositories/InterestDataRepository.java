package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.database.dao.InterestDao;
import com.example.realestatemanager.models.Interest;

import java.util.List;

public class InterestDataRepository {

    private final InterestDao interestDao;

    public InterestDataRepository(InterestDao interestDao) {
        this.interestDao = interestDao;
    }

    //GET PLACE
    public LiveData<Interest> getInterest(long idPlace) {
        return this.interestDao.getInterest(idPlace);
    }

    public LiveData<List<Interest>> getInterests(long placeId) {
        return this.interestDao.getInterests(placeId);
    }

    public LiveData<List<Long>> getPlaceIdForGivenParameters(SupportSQLiteQuery query) {
        return interestDao.getPlacesIdForGivenParameters(query);
    }

    //CREATE
    public long createInterest(Interest interest) {
        return interestDao.createInterest(interest);
    }

    //UPDATE
    public void updateInterest(Interest interest) {
        interestDao.updateInterest(interest);
    }

    //DELETE
    public void deleteInterests(long placeId) {
        interestDao.deleteInterests(placeId);
    }
}
