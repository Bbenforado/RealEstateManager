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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.adapters.DetailFragmentAdapter;
import com.example.realestatemanager.adapters.DetailPhotoRecyclerViewAdapter;
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
    @BindView(R.id.edit_floating_action_button_detail_fragment)
    FloatingActionButton editFloatingActionButton;
    //@BindView(R.id.recycler_view_detail_interest)
    //RecyclerView recyclerViewInterest;
    /*@BindView(R.id.slider)
    SliderLayout sliderLayout;*/
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
    private DefaultSliderView sliderView;
    private DetailPhotoRecyclerViewAdapter adapter;

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.main_tabs) TabLayout tabLayout;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.layout) LinearLayout linearLayout;
    @BindView(R.id.recycler_view_detail_photos) RecyclerView recyclerViewPhotos;
    @BindView(R.id.image_view_detail)
    ImageView imageViewPhoto;

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
        configureRecyclerView();
        //displayGenericPhoto();

        long placeId = preferences.getLong(PLACE_ID, -1);
        getPhotos(placeId);
        /*viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                //displayPhotosOfPlace(placeId);

            }
        });*/
        return view;
    }

    @Override
    public void onStop() {
        //sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("on start");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("on resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("on pause");
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
        viewPager.setAdapter(new DetailFragmentAdapter(getActivity().getSupportFragmentManager(), titles) {
        });
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
    /*private void displayGenericPhoto() {
        sliderView = new DefaultSliderView(getContext());
        // initialize a SliderLayout
        sliderView.image(R.drawable.no_image);
        sliderView.setScaleType(BaseSliderView.ScaleType.Fit);

        sliderLayout.addSlider(sliderView);
    }

    private void displayPhotosOfPlace(long placeId) {

        viewModel.getPhotosForAPlace(placeId).observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {

                HashMap<String, File> file_maps = new HashMap<String, File>();

                for (int i = 0; i < photos.size(); i++) {

                    String path = photos.get(i).getUri();
                    File file = new File(path);

                    file_maps.put("Photo n " + i, file);

                }
                for (String name : file_maps.keySet()) {
                    sliderLayout.removeAllSliders();
                    sliderView = new DefaultSliderView(getContext());
                    sliderView.image(file_maps.get(name));
                    sliderLayout.addSlider(sliderView);
                }
            }
        });
    }*/
}
