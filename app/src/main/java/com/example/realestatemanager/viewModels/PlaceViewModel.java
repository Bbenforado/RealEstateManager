package com.example.realestatemanager.viewModels;

import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.repositories.AddressDataRepository;
import com.example.realestatemanager.repositories.InterestDataRepository;
import com.example.realestatemanager.repositories.PlaceDataRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlaceViewModel extends ViewModel {

    private final PlaceDataRepository placeDataSource;
    private final AddressDataRepository addressDataSource;
    private final InterestDataRepository interestDataSource;
    private final Executor executor;

    @Nullable
    private LiveData<Place> currentPlace;

    public PlaceViewModel(PlaceDataRepository placeDataSource, AddressDataRepository addressDataSource, InterestDataRepository interestDataSource, Executor executor) {
        this.placeDataSource = placeDataSource;
        this.addressDataSource = addressDataSource;
        this.interestDataSource = interestDataSource;
        this.executor = executor;
    }

    //FOR PLACES
    public LiveData<Place> getPlace(long placeId) {
        return placeDataSource.getPlace(placeId);
    }

    public LiveData<List<Place>> getPlaces() {
        return placeDataSource.getPlaces();
    }

    /*public long createPlace(Place place) {
        System.out.println("view model create place");
        return executor.execute(placeDataSource.createPlace(place));
    }*/

    public long createPlace(Place place) {
        Callable<Long> insertCallable = () -> placeDataSource.createPlace(place);
        long rowId = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    //FOR ADDRESSES
    public LiveData<Address> getAddress(long placeId) {
        return addressDataSource.getAddress(placeId);
    }

    public LiveData<List<Address>> getAddresses() {
        return addressDataSource.getAddresses();
    }

    public long createAddress(Address address) {
        System.out.println("view model create address");
        return addressDataSource.createAddress(address);
    }

    //FOR INTERESTS
    public long createInterest(Interest interest) {
        return interestDataSource.createInterest(interest);
    }
}