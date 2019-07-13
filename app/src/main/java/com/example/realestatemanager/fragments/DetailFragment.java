package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    @BindView(R.id.text_view_type_of_place_detail_fragment) TextView typeOfPlaceTextView;
    @BindView(R.id.text_view_price_detail_fragment) TextView priceTextView;
    @BindView(R.id.text_view_surface_detail_fragment) TextView surfaceTextView;
    @BindView(R.id.text_view_nbr_rooms_detail_fragment) TextView nbrOfRoomsTextView;
    @BindView(R.id.text_view_nbr_bathrooms_detail_fragment) TextView nbrOfBathroomsTextView;
    @BindView(R.id.text_view_nbr_bedrooms_detail_fragment) TextView nbrOfBedroomsTextView;
    @BindView(R.id.text_view_description_detail_fragment) TextView descriptionTextView;
    @BindView(R.id.edit_floating_action_button_detail_fragment) FloatingActionButton editFloatingActionButton;
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    public static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //------------------------------------------------
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, view);
        configureViewModel();
        long placeId = preferences.getLong(PLACE_ID, -1);
        viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                updateUi(place);
            }
        });
        return view;
    }

    //----------------------------------------------
    //ACTIONS
    //-----------------------------------------------
    @OnClick(R.id.edit_floating_action_button_detail_fragment)
    public void editPlace() {
        preferences.edit().putInt(STATUS_FORM_ACTIVITY, 1).apply();
        System.out.println("status here = " + preferences.getInt(STATUS_FORM_ACTIVITY, -1));
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

    //--------------------------------------------------
    //UPDATE UI
    //---------------------------------------------------
    private void updateUi(Place place) {
        typeOfPlaceTextView.setText(place.getType());
        priceTextView.setText(String.valueOf(place.getPrice()));
        if (place.getDescription() != null) {
            descriptionTextView.setText(place.getDescription());
        } else {
            descriptionTextView.setText("Not informed yet");
        }
        if (place.getSurface() != 0) {
            surfaceTextView.setText(String.valueOf(place.getSurface()));
        }else {
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
    }

    //--------------------------------------------
    //METHODS
    //----------------------------------------------

}
