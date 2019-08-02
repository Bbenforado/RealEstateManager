package com.example.realestatemanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.getLatLngOfPlace;
import static com.example.realestatemanager.utils.Utils.getLocationFromAddress;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap googleMap;
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    private LatLngBounds bounds;
    //----------------------------------------------
    private static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        configureViewModel();
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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            return;
        }
        googleMap.setOnMarkerClickListener(this);

    }

    //---------------------------------------
    //PERMISSIONS
    //-------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_ACCESS_COURSE_FINE_LOCATION) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                showMyLocation();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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

    //-----------------------------------------
    //CONFIGURATION
    //--------------------------------------------
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    //------------------------------------------
    //METHODS
    //-------------------------------------------
    private void showMyLocation() {
        if (getUserLocation(this) == null) {
            Toast.makeText(this, getString(R.string.toast_message_user_location_not_found), Toast.LENGTH_SHORT).show();
        } else {
            Double userLat = getUserLocation(this).getLatitude();
            Double userLng = getUserLocation(this).getLongitude();

            LatLng latLng = new LatLng(userLat, userLng);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            float zoomLevel = 16.0f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            LatLng latLng1 = createNewLatLngForBounds(userLat, userLng, 2000);
            LatLng latLng2 = createNewLatLngForBounds(userLat, userLng, -2000);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng1);
            builder.include(latLng2);
            bounds = builder.build();

            /*Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("user location"));
            marker.setTag(-1);
            marker.showInfoWindow();*/
            getAllAddresses();
        }
    }

    private LatLng createNewLatLngForBounds(double latitude, double longitude, long meters) {
        double newLat = getNewLat(latitude, meters);
        double newLng = getNewLng(longitude, latitude, meters);
        return new LatLng(newLat, newLng);
    }

    //GET DATA
    private void getAllAddresses() {
        viewModel.getAddresses().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {

                for(int i = 0; i<addresses.size(); i++) {
                    if (addresses.get(i).getLatLng() != null) {
                        String latLng = addresses.get(i).getLatLng();
                        long placeId = addresses.get(i).getIdPlace();
                        LatLng latLngOfPlace = getLatLngOfPlace(latLng);
                        if (bounds.contains(latLngOfPlace)) {
                            showPlaceOnMap(placeId, latLngOfPlace);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_message_place_location_not_found), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showPlaceOnMap(long id, LatLng latLng) {
        if (latLng != null) {
            CameraUpdateFactory.newLatLng(latLng);
            Marker placeMarker = googleMap.addMarker(new MarkerOptions()
            .position(latLng));
            placeMarker.setTag(id);
        } else {
            Toast.makeText(this, getString(R.string.toast_message_place_location_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    public Location getUserLocation(LocationListener listener) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);
        if (!enabled) {
            Toast.makeText(this, getString(R.string.toast_message_no_location_provider), Toast.LENGTH_SHORT).show();
        }

        final long MIN_TIME_BW_UPDATES = 1000;
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
        Location myLocation = null;
        try {
            locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        long tag = (long) marker.getTag();
            preferences.edit().putLong(PLACE_ID, tag).apply();
            Intent detailIntent = new Intent(this, DetailActivity.class);
            startActivity(detailIntent);
        return false;
    }

    @Override
    public void onCameraIdle() {
        getAllAddresses();
    }

    private double getNewLat(double latitude, long meters) {
        double earth = 6378.137;  //radius of the earth in kilometer
        double pi = Math.PI;
        double m = (1 / ((2 * pi / 360) * earth)) / 1000;  //1 meter in degree

        return latitude + (meters * m);
    }

    private double getNewLng(double longitude, double latitude, long meters) {
        double earth = 6378.137;  //radius of the earth in kilometer
        double pi = Math.PI;
        double m = (1 / ((2 * pi / 360) * earth)) / 1000;  //1 meter in degree

        return longitude + (meters * m) / Math.cos(latitude * (pi / 180));
    }
}
