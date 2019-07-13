package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
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

    public LiveData<List<Interest>> getInterests() {
        return this.interestDao.getInterests();
    }

    //CREATE
    public long createInterest(Interest interest) {
        return interestDao.createInterest(interest);
    }

    //UPDATE
    public void updateInterest(Interest interest) {
        interestDao.updateInterest(interest);
    }
}
