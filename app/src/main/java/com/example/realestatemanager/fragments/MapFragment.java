package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.getLatLngOfPlace;
import static com.example.realestatemanager.utils.Utils.getLocationFromAddress;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    //-----------------------------------
    //BIND VIEWS
    //-------------------------------
    @BindView(R.id.map_view)
    MapView mapView;
    //------------------------------------
    //-----------------------------------------
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    //-------------------------------------------
    //-----------------------------------------------
    private SharedPreferences preferences;
    private PlaceViewModel viewModel;
    private long id;
    private GoogleMap gmap;

    public MapFragment() {
        // Required empty public constructor
    }

    /*public static MapFragment newInstance(int position) {
        MapFragment fragment = new MapFragment();
        *//*Bundle args = new Bundle();
        args.putInt(KEY_POSITION_MAP, position);
        fragment.setArguments(args);*//*
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        configureViewModel();
        id = preferences.getLong(PLACE_ID, -1);

        if (id != 0 && id != -1) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        MapsInitializer.initialize(this.getActivity());
        if (id != -1 && id != 0) {
            viewModel.getAddress(id).observe(this, new Observer<com.example.realestatemanager.models.Address>() {
                @Override
                public void onChanged(com.example.realestatemanager.models.Address adressOfPlace) {

                    if (adressOfPlace.getLatLng() != null) {
                        String latLng = adressOfPlace.getLatLng();
                        LatLng latLngOfAddress = getLatLngOfPlace(latLng);


                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngOfAddress, 16);
                            gmap.animateCamera(cameraUpdate);
                            gmap.addMarker(new MarkerOptions()
                                    .position(latLngOfAddress));

                        } else {
                        Toast.makeText(getContext(), getString(R.string.toast_message_place_location_not_found), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_message_address_not_found), Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onResume() {
        if (id != -1 && id != 0) {
            mapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (id != -1 && id != 0) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (id != -1 && id != 0) {
            mapView.onLowMemory();
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }
}
