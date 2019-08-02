package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.adapters.DetailFragmentAdapter;
import com.example.realestatemanager.adapters.DetailPhotoRecyclerViewAdapter;
import com.example.realestatemanager.adapters.DetailRecyclerViewAdapter;
import com.example.realestatemanager.adapters.PhotoRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.example.realestatemanager.utils.Utils.convertDollarToEuro;
import static com.example.realestatemanager.utils.Utils.getLatLngOfPlace;
import static com.example.realestatemanager.utils.Utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements OnMapReadyCallback {

    //----------------------------------------------
    //BIND VIEWS
    //------------------------------------------------
    @Nullable
    @BindView(R.id.edit_floating_action_button_detail_fragment)
    FloatingActionButton editFloatingActionButton;
    @Nullable
    @BindView(R.id.viewpager) ViewPager viewPager;
    @Nullable
    @BindView(R.id.main_tabs) TabLayout tabLayout;
    @BindView(R.id.recycler_view_detail_photos) RecyclerView recyclerViewPhotos;

    @BindView(R.id.image_view_detail)
    ImageView imageViewPhoto;
    @Nullable
    @BindView(R.id.text_view_detail_fragment_no_item_selected) TextView textViewNoItemSelected;
    //FOR TABLET MODE
    @Nullable
    @BindView(R.id.type_of_place_text_view_detail_frag_tablet_mode) TextView textViewTypeOfPlaceTabletMode;
    @Nullable
    @BindView(R.id.layout_date_of_sale_tablet_mode) LinearLayout layoutDateOfSaleTabletMode;
    @Nullable
    @BindView(R.id.creation_date_detail_tablet_mode_text_view) TextView textViewCreationDateTabletMode;
    @Nullable
    @BindView(R.id.status_text_view_detail_tablet_mode_fragment) TextView textViewStatusTabletMode;
    @Nullable
    @BindView(R.id.date_of_sale_detail_tablet_mode_text_view) TextView textViewDateOfSaleTabletMode;
    @Nullable
    @BindView(R.id.text_view_description_detail_tablet_mode_fragment) TextView textViewDescriptionTabletMode;
    @Nullable
    @BindView(R.id.text_view_price_detail_tablet_mode_fragment) TextView textViewPriceTabletMode;
    @Nullable
    @BindView(R.id.material_convert_price_button_tablet_mode) MaterialButton buttonConvertPriceTabletMode;
    @Nullable
    @BindView(R.id.text_view_surface_detail_table_mode_fragment) TextView textViewSurfaceTabletMode;
    @Nullable
    @BindView(R.id.text_view_nbr_rooms_detail_tablet_mode_fragment) TextView textViewNbrOfRoomsTabletMode;
    @Nullable
    @BindView(R.id.text_view_nbr_bedrooms_detail_tablet_mode_fragment) TextView textViewNbrOfBedroomsTabletMode;
    @Nullable
    @BindView(R.id.text_view_nbr_bathrooms_detail_tablet_mode_fragment) TextView textViewNbrOfBathroomsTabletMode;
    @Nullable
    @BindView(R.id.street_address_text_view_detail_fragment_tablet_mode) TextView textViewStreetTabletMode;
    @Nullable
    @BindView(R.id.postal_code_and_city_text_view_detail_fragment_tablet_mode) TextView textViewPostalCodeAndCityTabletMode;
    @Nullable
    @BindView(R.id.real_estate_manager_text_view_detail_fragment_tablet_mode) TextView textViewManagerTabletMode;
    @Nullable
    @BindView(R.id.complement_text_view_detail_fragment_tablet_mode) TextView textViewComplementTabletMode;
    @Nullable
    @BindView(R.id.country_text_view_detail_fragment_tablet_mode) TextView textViewCountryTabletMode;
    @Nullable
    @BindView(R.id.map_view_detail_fragment_tablet_mode) MapView mapView;
    @Nullable
    @BindView(R.id.recycler_view_detail_interest_detail_fragment_tablet_mode) RecyclerView recyclerViewInterests;
    @Nullable
    @BindView(R.id.text_view_no_internet_detail_fragment_tablet_mode) TextView textViewNoInternet;
    //--------------------------------------------------
    //--------------------------------------------------
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    private static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private static final String APP_MODE = "appMode";
    //----------------------------------------------------
    //-----------------------------------------------------
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    private String[] titles = {"Information", "View location"};
    private int[] iconTabLayout = {R.drawable.house_white, R.drawable.ic_address_white};
    private DetailPhotoRecyclerViewAdapter adapter;
    private DetailRecyclerViewAdapter adapterForInterests;
    private DetailFragmentAdapter viewPagerAdapter;
    private long placeId;
    private GoogleMap googleMap;
    private long price;
    private SimpleDateFormat formatter;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //------------------------------------------------
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        placeId = preferences.getLong(PLACE_ID, -1);
        preferences.edit().putInt(STATUS_FORM_ACTIVITY, -1).apply();

        if (placeId != 0 && placeId != -1) {
            configureViewModel();
            if (preferences.getString(APP_MODE, null).equals(getString(R.string.app_mode_phone))) {
                configureViewpagerAndTabs();
                configureRecyclerView();
                getPhotos(placeId);
            } else if (preferences.getString(APP_MODE, null).equals(getString(R.string.app_mode_tablet))) {
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                configureRecyclerViewForTablet();
                adapterForInterests = new DetailRecyclerViewAdapter();
                Utils.configureRecyclerViewForInterests(getContext(), adapterForInterests, recyclerViewInterests);

                viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                    @Override
                    public void onChanged(Place place) {
                        updateUi(place);
                        price = place.getPrice();
                    }
                });
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(this::onMapReady);
            }
            textViewNoItemSelected.setVisibility(View.GONE);
        } else {
            textViewNoItemSelected.setVisibility(View.VISIBLE);
        }

        return view;
    }

    //------------------------------------------------
    //ACTIONS
    //------------------------------------------------
    @Optional
    @OnClick(R.id.material_convert_price_button_tablet_mode)
    public void convertPrice() {
        if (buttonConvertPriceTabletMode.getText().toString().equals(getString(R.string.button_text_convert_to_euros))) {
            int priceInEuros = convertDollarToEuro((int)price);
            String priceEuros = priceInEuros + " €";
            textViewPriceTabletMode.setText(priceEuros);
            buttonConvertPriceTabletMode.setText(getString(R.string.button_text_convert_to_dollars));
        } else if (buttonConvertPriceTabletMode.getText().toString().equals(getString(R.string.button_text_convert_to_dollars))) {
            String priceInDollars = price + " $";
            textViewPriceTabletMode.setText(priceInDollars);
            buttonConvertPriceTabletMode.setText(getString(R.string.button_text_convert_to_euros));
        }
    }

    private void updateUi(Place place) {
        textViewTypeOfPlaceTabletMode.setText(place.getType());
        if (place.getDateOfSale() == null) {
            layoutDateOfSaleTabletMode.setVisibility(View.GONE);
            textViewStatusTabletMode.setText(getString(R.string.status_available));
            textViewStatusTabletMode.setTextColor(getResources().getColor(R.color.green));
        } else {
            layoutDateOfSaleTabletMode.setVisibility(View.VISIBLE);
            Date dateOfSale = place.getDateOfSale();
            String date = formatter.format(dateOfSale);
            textViewDateOfSaleTabletMode.setText(date);
            textViewStatusTabletMode.setText(getString(R.string.status_sold));
            textViewStatusTabletMode.setTextColor(getResources().getColor(R.color.red));
        }
        textViewManagerTabletMode.setText(place.getAuthor());
        textViewCreationDateTabletMode.setText(formatter.format(place.getCreationDate()));
        String priceInDollars = place.getPrice() + " $";
        textViewPriceTabletMode.setText(priceInDollars);

        if (place.getDescription() != null) {
            textViewDescriptionTabletMode.setText(place.getDescription());
        } else {
            textViewDescriptionTabletMode.setText(getString(R.string.not_informed_yet));
        }
        if (place.getSurface() != 0) {
            String surface = place.getSurface() + " m²";
            textViewSurfaceTabletMode.setText(surface);
        } else {
            textViewSurfaceTabletMode.setText(getString(R.string.not_informed_yet));
        }
        if (place.getNbrOfRooms() != 0) {
            textViewNbrOfRoomsTabletMode.setText(String.valueOf(place.getNbrOfRooms()));
        } else {
            textViewNbrOfRoomsTabletMode.setText(getString(R.string.not_informed_yet));
        }
        if (place.getNbrOfBathrooms() != 0) {
            textViewNbrOfBathroomsTabletMode.setText(String.valueOf(place.getNbrOfBathrooms()));
        } else {
            textViewNbrOfBathroomsTabletMode.setText(getString(R.string.not_informed_yet));
        }
        if (place.getNbrOfBedrooms() != 0) {
            textViewNbrOfBedroomsTabletMode.setText(String.valueOf(place.getNbrOfBedrooms()));
        } else {
            textViewNbrOfBedroomsTabletMode.setText(getString(R.string.not_informed_yet));
        }
        getInterests(place.getId());
        viewModel.getAddress(place.getId()).observe(this, new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                textViewStreetTabletMode.setText(address.getStreetNumber() + " " + address.getStreetName());
                if (address.getComplement() != null) {
                    if (!address.getComplement().equals(getString(R.string.not_informed))) {
                        textViewComplementTabletMode.setText(address.getComplement());
                    } else {
                        textViewComplementTabletMode.setVisibility(View.GONE);
                    }
                } else {
                    textViewComplementTabletMode.setVisibility(View.GONE);
                }
                textViewPostalCodeAndCityTabletMode.setText(address.getPostalCode() + " " + address.getCity());
                textViewCountryTabletMode.setText(address.getCountry());
            }
        });
    }
    @Override
    public void onResume() {
        if (preferences.getString(APP_MODE, null).equals("tablet") && isNetworkAvailable(getContext())) {
            if (placeId != -1 && placeId != 0) {
                mapView.onResume();
            }
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (preferences.getString(APP_MODE, null).equals("tablet") && isNetworkAvailable(getContext())) {
            if (placeId != -1 && placeId != 0) {
                mapView.onDestroy();
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (preferences.getString(APP_MODE, null).equals("tablet") && isNetworkAvailable(getContext())) {
            if (placeId != -1 && placeId != 0) {
                mapView.onLowMemory();
            }
        }
    }

    //----------------------------------------------
    //ACTIONS
    //-----------------------------------------------
    @OnClick(R.id.edit_floating_action_button_detail_fragment)
    public void editPlace() {
        preferences.edit().putInt(STATUS_FORM_ACTIVITY, 1).apply();
        Intent intent = new Intent(getContext(), AddFormActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------
    //CONFIGURATION
    //-----------------------------------------------
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void configureViewpagerAndTabs() {
        viewPagerAdapter = new DetailFragmentAdapter(getActivity().getSupportFragmentManager(), titles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(iconTabLayout[i]);
        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void configureRecyclerView() {
        this.adapter = new DetailPhotoRecyclerViewAdapter(Glide.with(this));
        this.recyclerViewPhotos.setAdapter(adapter);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
    }

    private void configureRecyclerViewForTablet() {
        viewModel.getPhotosForAPlace(placeId).observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(photos, Glide.with(getContext()));
                recyclerViewPhotos.setAdapter(adapter);
                recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
            }
        });

    }

    private void getPhotos(long placeId) {
        viewModel.getPhotosForAPlace(placeId).observe(this, this::updatePhotoList);
    }

    private void updatePhotoList(List<Photo> photos) {
        if (photos.size() > 0) {
            this.adapter.updatePhotoData(photos);
            recyclerViewPhotos.setVisibility(View.VISIBLE);
            imageViewPhoto.setVisibility(View.GONE);
        } else {
            recyclerViewPhotos.setVisibility(View.GONE);
            imageViewPhoto.setVisibility(View.VISIBLE);
            imageViewPhoto.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapsInitializer.initialize(getContext());
        if (isNetworkAvailable(getContext())) {
            if (placeId != -1 && placeId != 0) {
                viewModel.getAddress(placeId).observe(this, new Observer<com.example.realestatemanager.models.Address>() {
                    @Override
                    public void onChanged(com.example.realestatemanager.models.Address adressOfPlace) {

                        if (adressOfPlace.getLatLng() != null) {
                            String latLng = adressOfPlace.getLatLng();
                            LatLng latLngOfAddress = getLatLngOfPlace(latLng);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngOfAddress, 16);
                            googleMap.animateCamera(cameraUpdate);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLngOfAddress));
                        } else {
                            Toast.makeText(getContext(), getString(R.string.toast_message_place_location_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_message_address_not_found), Toast.LENGTH_SHORT).show();
            }
        } else {
            textViewNoInternet.setVisibility(View.VISIBLE);
            mapView.setVisibility(View.GONE);
        }
    }



    private void getInterests(long placeId) {
        viewModel.getInterests(placeId).observe(this, this::updateInterestsList);
    }

    private void updateInterestsList(List<Interest> interests) {
        if (interests.size()>0) {
            this.adapterForInterests.updateInterestData(interests);
        }
    }
    //----------------------------------------------------
}
