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
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.adapters.DetailFragmentAdapter;
import com.example.realestatemanager.adapters.DetailPhotoRecyclerViewAdapter;
import com.example.realestatemanager.adapters.PhotoRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    //----------------------------------------------
    //BIND VIEWS
    //------------------------------------------------
    @BindView(R.id.edit_floating_action_button_detail_fragment)
    FloatingActionButton editFloatingActionButton;
    @Nullable
    @BindView(R.id.viewpager) ViewPager viewPager;
    @Nullable
    @BindView(R.id.main_tabs) TabLayout tabLayout;
    /*@BindView(R.id.nested_scroll_view)
    NestedScrollView scrollView;*/
    //@BindView(R.id.layout) LinearLayout linearLayout;
    @BindView(R.id.recycler_view_detail_photos) RecyclerView recyclerViewPhotos;
    @BindView(R.id.image_view_detail)
    ImageView imageViewPhoto;
    @BindView(R.id.text_view_detail_fragment_no_item_selected) TextView textViewNoItemSelected;
    //FOR TABLET MODE
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
    private DetailFragmentAdapter viewPagerAdapter;
    private long placeId;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        System.out.println("on create detail frag");
        //------------------------------------------------
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        placeId = preferences.getLong(PLACE_ID, -1);

        System.out.println("place id detail frag = " + placeId);

        if (placeId != 0 && placeId != -1) {
            configureViewModel();
            if (preferences.getString(APP_MODE, null).equals(getString(R.string.app_mode_phone))) {
                configureViewpagerAndTabs();
                configureRecyclerView();
                getPhotos(placeId);
            } else if (preferences.getString(APP_MODE, null).equals(getString(R.string.app_mode_tablet))) {
                configureRecyclerViewForTablet();
                viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                    @Override
                    public void onChanged(Place place) {
                        updateUi(place);
                    }
                });
            }


            textViewNoItemSelected.setVisibility(View.GONE);

            /*if (preferences.getString(APP_MODE, null).equals("tablet")) {
                viewPagerAdapter.someMethod();
            }*/

        } else {
            textViewNoItemSelected.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void updateUi(Place place) {

        System.out.println("update ui info frag");

        if (place.getDateOfSale() == null) {
            layoutDateOfSaleTabletMode.setVisibility(View.GONE);
            textViewStatusTabletMode.setText(getString(R.string.status_available));
        } else {
            layoutDateOfSaleTabletMode.setVisibility(View.VISIBLE);
            textViewDateOfSaleTabletMode.setText(place.getDateOfSale());
            textViewStatusTabletMode.setText(getString(R.string.status_sold));
            textViewStatusTabletMode.setTextColor(getResources().getColor(R.color.red));
        }
        textViewManagerTabletMode.setText(place.getAuthor());
        textViewCreationDateTabletMode.setText(place.getCreationDate());
        textViewPriceTabletMode.setText(String.valueOf(place.getPrice()));

        if (place.getDescription() != null) {
            textViewDescriptionTabletMode.setText(place.getDescription());
        } else {
            textViewDescriptionTabletMode.setText(getString(R.string.not_informed_yet));
        }
        if (place.getSurface() != 0) {
            textViewSurfaceTabletMode.setText(String.valueOf(place.getSurface()));
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
        //getInterests(place.getId());
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
    public void onStop() {
        super.onStop();
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
        //viewPager = getView().findViewById(R.id.viewpager);
        viewPagerAdapter = new DetailFragmentAdapter(getActivity().getSupportFragmentManager(), titles);

        viewPager.setAdapter(viewPagerAdapter);

        //tabLayout = getActivity().findViewById(R.id.main_tabs);
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
    //----------------------------------------------------

}
