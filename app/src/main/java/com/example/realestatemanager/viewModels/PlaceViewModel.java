package com.example.realestatemanager.viewModels;

import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.repositories.AddressDataRepository;
import com.example.realestatemanager.repositories.InterestDataRepository;
import com.example.realestatemanager.repositories.PhotoDataRepository;
import com.example.realestatemanager.repositories.PlaceDataRepository;
import com.example.realestatemanager.repositories.RawDataRepository;

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
    private final PhotoDataRepository photoDataSource;
    private final RawDataRepository rawDataSource;
    private final Executor executor;

    /*@Nullable
    private LiveData<Place> currentPlace;*/

    public PlaceViewModel(PlaceDataRepository placeDataSource, AddressDataRepository addressDataSource,
                          InterestDataRepository interestDataSource, PhotoDataRepository photoDataRepository, RawDataRepository rawDataRepository, Executor executor) {
        this.placeDataSource = placeDataSource;
        this.addressDataSource = addressDataSource;
        this.interestDataSource = interestDataSource;
        this.photoDataSource = photoDataRepository;
        this.rawDataSource = rawDataRepository;
        this.executor = executor;
    }

    //-----------------------------------------------
    //FOR PLACES
    //-------------------------------------------------------
    public LiveData<List<PlaceAddressesPhotosAndInterests>> getPlacesAndData(SimpleSQLiteQuery query) {
        return rawDataSource.getPlacesAndData(query);
    }


    public LiveData<Place> getPlace(long placeId) {
        return placeDataSource.getPlace(placeId);
    }

    public LiveData<List<Place>> getPlaces() {
        return placeDataSource.getPlaces();
    }

    public LiveData<List<Place>> getPlaceForGivenParameters(SupportSQLiteQuery query) {
        return placeDataSource.getPlacesForGivenParameters(query);
    }

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

    public void updatePlace(Place place) {
        executor.execute(() -> {
            placeDataSource.updatePlace(place);
        });
    }

    //-----------------------------------------------------
    //FOR ADDRESSES
    //--------------------------------------------------------------
    public LiveData<List<Long>> getPlaceIdForGivenParamFromAddresses(SimpleSQLiteQuery query) {
        return addressDataSource.getPlaceIfForGivenParamFromAdresses (query);
    }

    /*public LiveData<Address> getAddress(long placeId) {
        return addressDataSource.getAddress(placeId);
    }*/
    public LiveData<Address> getAddressOfAPlace(long idAddressInPlace) {
        return addressDataSource.getAddressOfAPlace(idAddressInPlace);
    }

    public long getPlaceId(long idAddress) {
        return placeDataSource.getPlaceId(idAddress);
    }

    public LiveData<List<Address>> getAddresses() {
        return addressDataSource.getAddresses();
    }

    public long createAddress(Address address) {
        Callable<Long> insertCallable = () -> addressDataSource.createAddress(address);
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

    public void updateAddress(Address address) {
        executor.execute(() -> {
            addressDataSource.updateAddress(address);
        });
    }

    //-------------------------------------------------
    //FOR INTERESTS
    //-----------------------------------------------------
    public LiveData<List<Long>> getPlaceIdForGivenParameters(SupportSQLiteQuery query) {
        return interestDataSource.getPlaceIdForGivenParameters(query);
    }


    public void createInterest(Interest interest) {
        /*Callable<Long> insertCallable = () -> interestDataSource.createInterest(interest);
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
        return rowId;*/
        executor.execute(() -> {
            interestDataSource.createInterest(interest);
        });
    }

    public void updateInterest(Interest interest) {
        executor.execute(() -> {
            interestDataSource.updateInterest(interest);
        });
    }

    public LiveData<List<Interest>> getInterests(long placeId) {
        return interestDataSource.getInterests(placeId);
    }

    public LiveData<Interest> getInterest(long placeId) {
        return interestDataSource.getInterest(placeId);
    }

    public void deleteInterests(long placeId) {
        executor.execute(() -> {
            interestDataSource.deleteInterests(placeId);
        });
    }

    //-------------------------------------------------
    //FOR PHOTOS
    //----------------------------------------------------
    public void createPhoto(Photo photo) {
        executor.execute(() -> {
            photoDataSource.createPhoto(photo);
        });
    }

    public LiveData<List<Photo>> getPhotos() {
        return photoDataSource.getPhotos();
    }

    public LiveData<List<Photo>> getPhotosForAPlace(long placeId) {
        return photoDataSource.getPhotosForAPlace(placeId);
    }

    public LiveData<List<Long>> getPlaceIdForGivenParam(SupportSQLiteQuery query) {
        return photoDataSource.getPlaceIdForGivenParam(query);
    }

    public void updatePhoto(Photo photo) {
        executor.execute(() -> {
            photoDataSource.updatePhoto(photo);
        });
    }

    public void deletePhoto(long photoId) {
        executor.execute(() -> {
            photoDataSource.deletePhoto(photoId);
        });
    }
}