package com.example.realestatemanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.formatLocation;
import static com.example.realestatemanager.utils.Utils.getLocationFromAddress;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    private static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private PlaceViewModel viewModel;
    private List<LatLng> latLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        configureViewModel();
        latLngs = new ArrayList<>();
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_map_activity);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                askPermissionAndShowMyLocation();
            }
        });

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    showMyLocation();
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void askPermissionAndShowMyLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
                return;
            }
        }
        showMyLocation();
    }

    private void showMyLocation() {
        if (getUserLocation(this) == null) {
            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
        } else {
            //Location location = getUserLocation(this);
            //String userLocation = formatLocation(location);

            Double userLat = getUserLocation(this).getLatitude();
            Double userLng = getUserLocation(this).getLongitude();

            LatLng latLng = new LatLng(userLat, userLng);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            float zoomLevel = 16.0f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("user location"));
            marker.showInfoWindow();
            getAllAddresses(this);
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void getAllAddresses(Context context) {
        viewModel.getAddresses().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                for(int i = 0; i<addresses.size(); i++) {

                    String finalAddress = addresses.get(i).getStreetNumber() + " " + addresses.get(i).getStreetName() + "," +
                            addresses.get(i).getCity() + "," + addresses.get(i).getPostalCode() + " " +
                            addresses.get(i).getCountry();
                    LatLng latLng = getLocationFromAddress(context, finalAddress);
                    latLngs.add(latLng);
                }
                showAllPlacesOnMap(latLngs);
            }
        });
    }

    private void showAllPlacesOnMap(List<LatLng> placesLatLngs) {
        for (int i = 0; i<placesLatLngs.size(); i++) {
            if (placesLatLngs.get(i) != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(placesLatLngs.get(i), 16);
                googleMap.animateCamera(cameraUpdate);
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(placesLatLngs.get(i)));

            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Location getUserLocation(LocationListener listener) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);
        if (!enabled) {
            Toast.makeText(this, "no location provider", Toast.LENGTH_SHORT).show();
        }

        final long MIN_TIME_BW_UPDATES = 1000;
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
        Location myLocation = null;
        try {
            locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
            //myLocation = locationManager.getLastKnownLocation(bestProvider);
            myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        return myLocation;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
