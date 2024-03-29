package com.example.realestatemanager.controller.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.InterestRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.realestatemanager.utils.Utils.updateUiPlace;

/**
 * Fragment that displays informations about the place
 */
public class InformationFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    //------------------------------------------
    //BIND VIEWS
    //--------------------------------------------
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
    @BindView(R.id.street_address_text_view_information) TextView streetAddressTextView;
    @BindView(R.id.complement_text_view_information) TextView complementTextView;
    @BindView(R.id.postal_code_and_city_text_view_information) TextView postalCodeAndCityTextView;
    @BindView(R.id.country_text_view_information) TextView countryTextView;
    @BindView(R.id.text_view_price_detail_fragment) TextView priceTextView;
    @BindView(R.id.material_convert_price_button) MaterialButton convertPriceButton;
    //---------------------------------------------------
    //-----------------------------------------------------------
    private PlaceViewModel viewModel;
    private InterestRecyclerViewAdapter adapter;
    private long price;

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
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ButterKnife.bind(this, result);
        configureViewModel();
        long placeId = preferences.getLong(PLACE_ID, -1);
        LifecycleOwner owner = getViewLifecycleOwner();

        if (placeId != 0 && placeId != -1) {
            viewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    updateUiWithData(place, owner);
                    adapter = new InterestRecyclerViewAdapter();
                    configureRecyclerViewForInterestsHorizontal();
                    price = place.getPrice();
                }
            });
        }
        return result;
    }

    //--------------------------------
    //CONFIGURATION
    //-----------------------------------
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    /**
     * configure the recycler view for the interests, displayed in horizontal
     */
    private void configureRecyclerViewForInterestsHorizontal() {
        this.adapter = new InterestRecyclerViewAdapter();
        this.recyclerViewInterest.setAdapter(adapter);
        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
    }
    //--------------------------------------------------
    //ACTIONS
    //----------------------------------------------------
    /**
     * Convert the price of the place in euros or dollars
     */
    @OnClick(R.id.material_convert_price_button)
    public void convertPrice() {
        Utils.convertPrice(getContext(), convertPriceButton, priceTextView, price);
    }
    //--------------------------------------------------
    //UPDATE UI
    //---------------------------------------------------
    /**
     * get the interests in database of the given place
     * @param placeId the id of the place
     */
    private void getInterests(long placeId) {
        viewModel.getInterests(placeId).observe(this, this::updateInterestsList);
    }

    /**
     * update the interests of the place for the recycler view
     * @param interests list of the interests of the given place
     */
    private void updateInterestsList(List<Interest> interests) {
        if (interests.size()>0) {
            this.adapter.updateInterestData(interests);
        }
    }

    private void updateUiWithData(Place place, LifecycleOwner owner) {
        updateUiPlace(getContext(), place, managerOfPlaceTextView, creationDateTextView, priceTextView,
                descriptionTextView, surfaceTextView, nbrOfRoomsTextView, nbrOfBedroomsTextView,
                nbrOfBathroomsTextView, statusTextView, dateOfSaleTextView, layoutDateOfSale);
        getInterests(place.getId());
        Utils.updateUiAddress(getContext(), place, viewModel, owner,
                streetAddressTextView, complementTextView, postalCodeAndCityTextView, countryTextView);
    }
}
