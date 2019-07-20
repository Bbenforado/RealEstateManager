package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.DetailFragmentAdapter;
import com.example.realestatemanager.adapters.DetailRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    public static final String KEY_POSITION = "position";
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    @BindView(R.id.status_text_view_detail_fragment) TextView statusTextView;
    @BindView(R.id.real_estate_manager_text_view_detail_fragment) TextView managerOfPlaceTextView;
    @BindView(R.id.creation_date_detail_text_view) TextView creationDateTextView;
    @BindView(R.id.recycler_view_detail_interest) RecyclerView recyclerViewInterest;
    @BindView(R.id.text_view_surface_detail_fragment)
    TextView surfaceTextView;
    @BindView(R.id.text_view_nbr_rooms_detail_fragment)
    TextView nbrOfRoomsTextView;
    @BindView(R.id.text_view_nbr_bathrooms_detail_fragment)
    TextView nbrOfBathroomsTextView;
    @BindView(R.id.text_view_nbr_bedrooms_detail_fragment)
    TextView nbrOfBedroomsTextView;
    @BindView(R.id.text_view_description_detail_fragment)
    TextView descriptionTextView;
    @BindView(R.id.layout_date_of_sale)
    LinearLayout layoutDateOfSale;
    @BindView(R.id.date_of_sale_detail_text_view) TextView dateOfSaleTextView;
    private PlaceViewModel viewModel;
    private DetailRecyclerViewAdapter adapter;
    private SharedPreferences preferences;

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance(int position) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_information, container, false);
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, result);
        configureViewModel();
        long placeId = preferences.getLong(PLACE_ID, -1);
        viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                updateUi(place);
                configureRecyclerView();
            }
        });
        return result;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void configureRecyclerView() {
        this.adapter = new DetailRecyclerViewAdapter();
        this.recyclerViewInterest.setAdapter(adapter);
        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //--------------------------------------------------
    //UPDATE UI
    //---------------------------------------------------
    private void updateUi(Place place) {
        if (place.getDateOfSale() == null) {
            layoutDateOfSale.setVisibility(View.GONE);
            statusTextView.setText("Available");
        } else {
            layoutDateOfSale.setVisibility(View.VISIBLE);
            dateOfSaleTextView.setText(place.getDateOfSale());
            statusTextView.setText("Sold");
        }
        managerOfPlaceTextView.setText(place.getAuthor());
        creationDateTextView.setText(place.getCreationDate());

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
    }

    private void getInterests(long placeId) {
        viewModel.getInterests(placeId).observe(this, this::updateInterestsList);
    }

    private void updateInterestsList(List<Interest> interests) {
        this.adapter.updateInterestData(interests);
    }

}
