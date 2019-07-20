package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.adapters.DetailFragmentAdapter;
import com.example.realestatemanager.adapters.DetailRecyclerViewAdapter;
import com.example.realestatemanager.adapters.PlaceRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
 /*   @BindView(R.id.text_view_type_of_place_detail_fragment)
    TextView typeOfPlaceTextView;
    @BindView(R.id.text_view_price_detail_fragment)
    TextView priceTextView;
    @BindView(R.id.text_view_surface_detail_fragment)
    TextView surfaceTextView;
    @BindView(R.id.text_view_nbr_rooms_detail_fragment)
    TextView nbrOfRoomsTextView;
    @BindView(R.id.text_view_nbr_bathrooms_detail_fragment)
    TextView nbrOfBathroomsTextView;
    @BindView(R.id.text_view_nbr_bedrooms_detail_fragment)
    TextView nbrOfBedroomsTextView;
    @BindView(R.id.text_view_description_detail_fragment)
    TextView descriptionTextView;*/
    @BindView(R.id.edit_floating_action_button_detail_fragment)
    FloatingActionButton editFloatingActionButton;
    //@BindView(R.id.recycler_view_detail_interest)
    //RecyclerView recyclerViewInterest;
    @BindView(R.id.slider)
    SliderLayout sliderLayout;
 /*   @BindView(R.id.status_text_view_detail_fragment) TextView statusTextView;
    @BindView(R.id.real_estate_manager_text_view_detail_fragment) TextView managerOfPlaceTextView;
    @BindView(R.id.creation_date_detail_text_view) TextView creationDateTextView;*/
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    public static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    //private DetailRecyclerViewAdapter adapter;
    private String[] titles = {"Information", "View location"};
    private int[] iconTabLayout = {R.drawable.house_white, R.drawable.ic_address_white};

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.main_tabs) TabLayout tabLayout;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.layout) LinearLayout linearLayout;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //------------------------------------------------
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        configureViewpagerAndTabs();
        configureViewModel();
        long placeId = preferences.getLong(PLACE_ID, -1);
        viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                //updateUi(place);
                //configureRecyclerView();
                displayPhotosOfPlace(placeId);
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
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

    /*private void configureRecyclerView() {
        this.adapter = new DetailRecyclerViewAdapter();
        this.recyclerViewInterest.setAdapter(adapter);
        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(getContext()));
    }*/

    private void configureViewpagerAndTabs() {
        System.out.println("configure tabs");
        viewPager.setAdapter(new DetailFragmentAdapter(getActivity().getSupportFragmentManager(), titles) {
        });
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(iconTabLayout[i]);
        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        System.out.println("viewpager = " + viewPager.getHeight());
        System.out.println("scroll = " + scrollView.getHeight());
        System.out.println("linear = " + linearLayout.getHeight());
        System.out.println("tab = " + tabLayout.getHeight());
    }

    //--------------------------------------------------
    //UPDATE UI
    //---------------------------------------------------
    /*private void updateUi(Place place) {
        displayPhotosOfPlace(place.getId());
        managerOfPlaceTextView.setText(place.getAuthor());
        creationDateTextView.setText(place.getCreationDate());
        if (place.getStatus() == 0) {
            statusTextView.setText("Available");
        } else {
            statusTextView.setText("Sold");
        }
        typeOfPlaceTextView.setText(place.getType());
        priceTextView.setText(String.valueOf(place.getPrice()));
        if (place.getDescription() != null) {
            descriptionTextView.setText(place.getDescription());
        } else {
            descriptionTextView.setText("Not informed yet");
        }
        if (place.getSurface() != 0) {
            surfaceTextView.setText(String.valueOf(place.getSurface()));
        } else {
            surfaceTextView.setText("Not informed yet");
        }
        if (place.getNbrOfRooms() != 0) {
            nbrOfRoomsTextView.setText(String.valueOf(place.getNbrOfRooms()));
        } else {
            nbrOfRoomsTextView.setText("Not informed yet");
        }
        if (place.getNbrOfBathrooms() != 0) {
            nbrOfBathroomsTextView.setText(String.valueOf(place.getNbrOfBathrooms()));
        } else {
            nbrOfBathroomsTextView.setText("Not informed yet");
        }
        if (place.getNbrOfBedrooms() != 0) {
            nbrOfBedroomsTextView.setText(String.valueOf(place.getNbrOfBedrooms()));
        } else {
            nbrOfBedroomsTextView.setText("Not informed yet");
        }
        getInterests(place.getId());
    }*/

    //--------------------------------------------
    //METHODS
    //----------------------------------------------
    /*private void getInterests(long placeId) {
        viewModel.getInterests(placeId).observe(this, this::updateInterestsList);
    }

    private void updateInterestsList(List<Interest> interests) {
        this.adapter.updateInterestData(interests);
    }*/

    /*private void getPhotos(long placeId) {
        viewModel.getPhotosForAPlace(placeId).observe(this, this::updatePhotosList);
    }*/

    /*private void updatePhotosList(List<Photo> photos) {

    }*/

    //----------------------------------------------------
    private void displayPhotosOfPlace(long placeId) {

        viewModel.getPhotosForAPlace(placeId).observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {

                HashMap<String, File> file_maps = new HashMap<String, File>();

                for (int i=0; i<photos.size(); i++) {

                    String path = photos.get(i).getUri();
                    File file = new File(path);

                    file_maps.put("Photo n " + i, file);

                }
                for (String name : file_maps.keySet()) {
                    DefaultSliderView sliderView = new DefaultSliderView(getContext());
                    // initialize a SliderLayout
                   sliderView
                            .image(file_maps.get(name));

                    sliderLayout.addSlider(sliderView);
                }
            }
        });
    }
}
