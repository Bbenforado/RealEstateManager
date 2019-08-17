package com.example.realestatemanager.controller.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.getLatLngOfPlace;
import static com.example.realestatemanager.utils.Utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    //-----------------------------------
    //BIND VIEWS
    //-------------------------------
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.text_view_no_internet_map_fragment)
    TextView textViewNoInternet;
    //------------------------------------
    //-----------------------------------------
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    private static final String ADDRESS_ID = "addressId";
    private PlaceViewModel viewModel;
    private long id;
    private long addressId;
    private GoogleMap gMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //-------------------------------------------
        //-----------------------------------------------
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        configureViewModel();
        id = preferences.getLong(PLACE_ID, -1);
        addressId = preferences.getLong(ADDRESS_ID, -1);

       if (isNetworkAvailable(getContext())) {
            if (id != 0 && id != -1) {
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(this);
            }
          } else {
           textViewNoInternet.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        MapsInitializer.initialize(this.getActivity());
            if (id != -1 && id != 0) {
                viewModel.getAddressOfAPlace(addressId).observe(this, new Observer<Address>() {
                    @Override
                    public void onChanged(com.example.realestatemanager.models.Address addressOfPlace) {

                        if (addressOfPlace.getLatLng() != null) {
                            String latLng = addressOfPlace.getLatLng();
                            LatLng latLngOfAddress = getLatLngOfPlace(latLng);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngOfAddress, 16);
                            gMap.animateCamera(cameraUpdate);
                            gMap.addMarker(new MarkerOptions()
                                    .position(latLngOfAddress));

                        } else {
                            mapView.setVisibility(View.GONE);
                            textViewNoInternet.setText(getString(R.string.toast_message_place_location_not_found));
                            textViewNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_message_address_not_found), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onResume() {
        if (isNetworkAvailable(getContext())) {
            if (id != -1 && id != 0) {
                mapView.onResume();
            }
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNetworkAvailable(getContext())) {
            if (id != -1 && id != 0) {
                mapView.onDestroy();
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (isNetworkAvailable(getContext())) {
            if (id != -1 && id != 0) {
                mapView.onLowMemory();
            }
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }
}
