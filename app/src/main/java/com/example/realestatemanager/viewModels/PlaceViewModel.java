package com.example.realestatemanager.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.models.PlaceIdAndAddressId;
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
    //FOR PLACES ADDRESSES PHOTOS INTERESTS
    //-------------------------------------------------------
    public LiveData<List<PlaceAddressesPhotosAndInterests>> getPlacesAndData(SimpleSQLiteQuery query) {
        return rawDataSource.getPlacesAndData(query);
    }

    public LiveData<List<PlaceAddressesPhotosAndInterests>> getAllData() {
        return placeDataSource.getAllData();
    }

    //----------------------
    //FOR PLACES
    //-------------------------
    public LiveData<Place> getPlace(long placeId) {
        return placeDataSource.getPlace(placeId);
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
    public LiveData<Address> getAddressOfAPlace(long idAddressInPlace) {
        return addressDataSource.getAddressOfAPlace(idAddressInPlace);
    }

    public LiveData<List<PlaceIdAndAddressId>> getPlaceAndAddressId() {
        return placeDataSource.getPlaceAndAddressId();
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
    public void createInterest(Interest interest) {
        executor.execute(() -> {
            interestDataSource.createInterest(interest);
        });
    }

    public LiveData<List<Interest>> getInterests(long placeId) {
        return interestDataSource.getInterests(placeId);
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