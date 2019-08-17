package com.example.realestatemanager.controller.activities;

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
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceIdAndAddressId;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.getLatLngOfPlace;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap googleMap;
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    private LatLngBounds bounds;
    //----------------------------------------------
    private static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    private static final String ADDRESS_ID = "addressId";


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

            LatLng latLng1 = createNewLatLngForBounds(userLat, userLng, 1000);
            LatLng latLng2 = createNewLatLngForBounds(userLat, userLng, -1000);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng1);
            builder.include(latLng2);
            bounds = builder.build();

            /*Marker marker;
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng));
                   // .title("user location"));
            marker.setTag(-1);
            marker.showInfoWindow();*/
            getPlacesAndShowMarkersOnMap();
        }
    }

    private LatLng createNewLatLngForBounds(double latitude, double longitude, long meters) {
        double newLat = getNewLat(latitude, meters);
        double newLng = getNewLng(longitude, latitude, meters);
        return new LatLng(newLat, newLng);
    }

    //GET DATA
    private void getPlacesAndShowMarkersOnMap() {
        viewModel.getPlaceAndAddressId().observe(this, new Observer<List<PlaceIdAndAddressId>>() {
            @Override
            public void onChanged(List<PlaceIdAndAddressId> placeIdAndAddressIds) {
                for (int i = 0; i<placeIdAndAddressIds.size(); i++) {

                    System.out.println("tour n " + i);
                    System.out.println("place id = " + placeIdAndAddressIds.get(i).getId());
                    System.out.println("address id = " + placeIdAndAddressIds.get(i).getAddressId());

                    if (placeIdAndAddressIds.get(i).getLatLng() != null) {
                        String latLng = placeIdAndAddressIds.get(i).getLatLng();
                        LatLng latLngOfPlace = getLatLngOfPlace(latLng);
                        if (bounds.contains(latLngOfPlace)) {
                            String type = placeIdAndAddressIds.get(i).getType();
                            long placeId = placeIdAndAddressIds.get(i).getId();
                            showPlaceOnMap(type, placeId, latLngOfPlace);
                        }
                    }
                }
            }
        });
    }

    private void showPlaceOnMap(String type, long id, LatLng latLng) {
        if (latLng != null) {
            CameraUpdateFactory.newLatLng(latLng);
            Marker placeMarker = googleMap.addMarker(new MarkerOptions()
            .position(latLng));
            placeMarker.setTag(id);
            switch (type) {
                case "Loft":
                    placeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    break;
                case "Mansion":
                    placeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    break;
                case "Duplex":
                    placeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    break;
                case "Penthouse":
                    placeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    break;
                    default:
                        placeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            }
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
        System.out.println("on marker click");
        long tag = (long) marker.getTag();

        viewModel.getPlace(tag).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                long addressId = place.getIdAddress();
                preferences.edit().putLong(ADDRESS_ID, addressId).apply();
                preferences.edit().putLong(PLACE_ID, tag).apply();
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("result", -1);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
            }
        });


            return false;
    }

    @Override
    public void onCameraIdle() {
        getPlacesAndShowMarkersOnMap();
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
