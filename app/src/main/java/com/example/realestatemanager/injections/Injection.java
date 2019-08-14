package com.example.realestatemanager.injections;

import android.content.Context;

import com.example.realestatemanager.database.RealEstateManagerDatabase;
import com.example.realestatemanager.repositories.AddressDataRepository;
import com.example.realestatemanager.repositories.InterestDataRepository;
import com.example.realestatemanager.repositories.PhotoDataRepository;
import com.example.realestatemanager.repositories.PlaceDataRepository;
import com.example.realestatemanager.repositories.RawDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PlaceDataRepository providePlaceDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PlaceDataRepository(database.placeDao());
    }

    public static AddressDataRepository provideAddressDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new AddressDataRepository(database.addressDao());
    }

    public static InterestDataRepository provideInterestDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new InterestDataRepository(database.interestDao());
    }

    public static PhotoDataRepository providePhotoDataSource(Context context) {
        RealEstateManagerDatabase database =  RealEstateManagerDatabase.getInstance(context);
        return new PhotoDataRepository(database.photoDao());
    }

    public static RawDataRepository provideRawDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new RawDataRepository(database.rawDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PlaceDataRepository dataSourcePlace = providePlaceDataSource(context);
        AddressDataRepository dataSourceAddress = provideAddressDataSource(context);
        InterestDataRepository dataSourceInterest = provideInterestDataSource(context);
        PhotoDataRepository dataSourcePhoto = providePhotoDataSource(context);
        RawDataRepository dataSourceRawData = provideRawDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourcePlace, dataSourceAddress, dataSourceInterest, dataSourcePhoto, dataSourceRawData, executor);
    }
}
