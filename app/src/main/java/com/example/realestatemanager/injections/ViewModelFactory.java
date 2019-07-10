package com.example.realestatemanager.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.repositories.AddressDataRepository;
import com.example.realestatemanager.repositories.InterestDataRepository;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.example.realestatemanager.repositories.PlaceDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PlaceDataRepository placeDataSource;
    private final AddressDataRepository addressDataSource;
    private final InterestDataRepository interestDataSource;
    private final Executor executor;

    public ViewModelFactory(PlaceDataRepository placeDataSource, AddressDataRepository addressDataSource, InterestDataRepository interestDataSource, Executor executor) {
        this.placeDataSource = placeDataSource;
        this.addressDataSource = addressDataSource;
        this.interestDataSource = interestDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlaceViewModel.class)) {
            return (T) new PlaceViewModel(placeDataSource, addressDataSource, interestDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
