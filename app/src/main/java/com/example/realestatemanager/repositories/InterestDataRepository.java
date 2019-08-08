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

    public LiveData<List<Interest>> getInterests(long placeId) {
        return this.interestDao.getInterests(placeId);
    }

    //CREATE
    public long createInterest(Interest interest) {
        return interestDao.createInterest(interest);
    }

    //DELETE
    public void deleteInterests(long placeId) {
        interestDao.deleteInterests(placeId);
    }
}
