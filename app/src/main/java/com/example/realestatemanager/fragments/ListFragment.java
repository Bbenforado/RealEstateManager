package com.example.realestatemanager.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.activities.DetailActivity;
import com.example.realestatemanager.adapters.PlaceRecyclerViewAdapter;
import com.example.realestatemanager.adapters.RecyclerViewPlaceAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.utils.ItemClickSupport;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.facebook.stetho.inspector.protocol.module.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class ListFragment extends Fragment {

    //----------------------------------------
    //BIND VIEWS
    //----------------------------------------
    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_floating_action_button)
    FloatingActionButton floatingButtonAddPlace;
    //-------------------------------------------
    //
    //---------------------------------------------
    private PlaceRecyclerViewAdapter adapter;
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    private String[] longClickFunctionality = {"Edit place"};
    private List<Address> addressList;
    private RecyclerViewPlaceAdapter adapterPlace;
    //----------------------------------------------
    //
    //----------------------------------------------
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    private static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private static final String APP_MODE = "appMode";
    private static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";
    private static final String QUERRIED_PLACES = "querriedPlaces";
    private static final String INDEX_ROW = "index";
    private static final String ADDRESS_ID = "addressId";
    public static final String NO_PLACES_SAVED = "noPlacesSaved";

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, result);
        preferences = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putInt(INDEX_ROW, -1).apply();
        configureViewModel();

        //if it s results activity
        if (preferences.getInt(KEY_RESULTS_ACTIVITY, -1) == 1) {
            floatingButtonAddPlace.setVisibility(View.GONE);
            addressList = new ArrayList<>();
            List<PlaceAddressesPhotosAndInterests> results = retrievedPlaces();
            configureRecyclerViewForResultsActivity();
            configureOnClickRecyclerViewForResultActivity();
            updateData(results);

        } else {
            configureRecyclerView();
            configureOnClickRecyclerView();
            getPlaces();
            getAddresses();
            getPhotos();
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, -1).apply();
    }

    //-------------------------------------------
    //CONFIGURATION
    //-------------------------------------------
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void configureRecyclerView() {
        this.adapter = new PlaceRecyclerViewAdapter(Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureRecyclerViewForResultsActivity() {
        adapterPlace = new RecyclerViewPlaceAdapter(Glide.with(this));
        recyclerView.setAdapter(adapterPlace);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureOnClickRecyclerViewForResultActivity() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        PlaceAddressesPhotosAndInterests place = adapterPlace.getPlaceAddressesPhotosAndInterests(position);
                        preferences.edit().putLong(PLACE_ID, place.getPlace().getId()).apply();
                        preferences.edit().putLong(ADDRESS_ID, place.getPlace().getIdAddress()).apply();
                        String appMode = preferences.getString(APP_MODE, null);

                        if (appMode.equals(getString(R.string.app_mode_tablet))) {
                            preferences.edit().putInt(INDEX_ROW, position).apply();
                            adapterPlace.notifyDataSetChanged();
                            ((MainActivity)getActivity()).refreshFragmentInfo();

                        } else {
                            Intent editIntent = new Intent(getContext(), DetailActivity.class);
                            startActivity(editIntent);
                        }
                    }
                })
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        PlaceAddressesPhotosAndInterests place = adapterPlace.getPlaceAddressesPhotosAndInterests(position);
                        displayLongClickDialogForResultActivity(place);

                        return false;
                    }
                });
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Place place = adapter.getPlace(position);
                        preferences.edit().putLong(PLACE_ID, place.getId()).apply();
                        preferences.edit().putLong(ADDRESS_ID, place.getIdAddress()).apply();
                        String appMode = preferences.getString(APP_MODE, null);

                        if (appMode.equals(getString(R.string.app_mode_tablet))) {
                            preferences.edit().putInt(INDEX_ROW, position).apply();
                            adapter.notifyDataSetChanged();
                            ((MainActivity)getActivity()).refreshFragmentInfo();

                        } else {
                            Intent editIntent = new Intent(getContext(), DetailActivity.class);
                            startActivity(editIntent);
                        }
                    }
                })
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        Place place = adapter.getPlace(position);
                        displayLongClickDialog(place);

                        return false;
                    }
                });
    }

    //----------------------------------------------
    //ACTIONS
    //------------------------------------------------
    @OnClick(R.id.add_floating_action_button)
    public void launchAddPlaceFormActivity() {
        preferences.edit().putInt(STATUS_FORM_ACTIVITY, 0).apply();
        Intent intent = new Intent(getContext(), AddFormActivity.class);
        startActivity(intent);
    }

    //-----------------------------------------------
    //GET DATA
    //------------------------------------------------
    private void getPlaces() {
        viewModel.getPlaces().observe(this, this::updatePlacesList);
    }

    private void getAddresses() {
        viewModel.getAddresses().observe(this, this::updateAddressesList);
    }

    private void getPhotos() {
        viewModel.getPhotos().observe(this, this::updatePhotosList);
    }

    private void updateData(List<PlaceAddressesPhotosAndInterests> list) {
        adapterPlace.updatePlaceData(list);
    }

    private void updatePhotosList(List<Photo> photos) {
        this.adapter.updatePhotoData(photos);
    }

    private void updatePlacesList(List<Place> places) {
        this.adapter.updatePlaceData(places);
        if (places.size() == 0) {
            preferences.edit().putInt(NO_PLACES_SAVED, 1).apply();
        } else {
            preferences.edit().putInt(NO_PLACES_SAVED, -1).apply();
        }
    }

    private void updateAddressesList(List<Address> addresses) {
        this.adapter.updateAddressData(addresses);
    }

    //FOR RESULTS ACTIVITY
    private List<PlaceAddressesPhotosAndInterests> retrievedPlaces() {
        List<PlaceAddressesPhotosAndInterests> placeList;
        Gson gson = new Gson();
        String json = preferences.getString(QUERRIED_PLACES, null);
        Type type = new TypeToken<List<PlaceAddressesPhotosAndInterests>>() {
        }.getType();
        placeList = gson.fromJson(json, type);
        return placeList;
    }

    //-----------------------------------
    //
    //------------------------------------------------------
    private void displayLongClickDialog(Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(longClickFunctionality, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.edit().putLong(PLACE_ID, place.getId()).apply();
                preferences.edit().putInt(STATUS_FORM_ACTIVITY, 1).apply();
                Intent editIntent = new Intent(getContext(), AddFormActivity.class);
                startActivity(editIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void displayLongClickDialogForResultActivity(PlaceAddressesPhotosAndInterests place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(longClickFunctionality, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.edit().putLong(PLACE_ID, place.getPlace().getId()).apply();
                preferences.edit().putInt(STATUS_FORM_ACTIVITY, 1).apply();
                Intent editIntent = new Intent(getContext(), AddFormActivity.class);
                startActivity(editIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
