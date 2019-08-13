package com.example.realestatemanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.R;
import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.activities.DetailActivity;
import com.example.realestatemanager.adapters.RecyclerViewPlaceAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.utils.ItemClickSupport;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *list fragment displays the list of the created places by the user
 * used to display all places when app is opened
 * and to display results of the search activity
 */
public class ListFragment extends Fragment {

    //----------------------------------------
    //BIND VIEWS
    //----------------------------------------
    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_floating_action_button)
    FloatingActionButton floatingButtonAddPlace;
    //-------------------------------------------
    //
    //---------------------------------------------
    private PlaceViewModel viewModel;
    private SharedPreferences preferences;
    private String[] longClickFunctionality = {"Edit place"};
    private RecyclerViewPlaceAdapter adapterPlace;
    //----------------------------------------------
    //
    //----------------------------------------------
    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PLACE_ID = "placeId";
    private static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private static final String APP_MODE = "appMode";
    private static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";
    private static final String QUERRIED_PLACES = "querriedPlaces";
    private static final String INDEX_ROW = "index";
    private static final String ADDRESS_ID = "addressId";
    private static final String NO_PLACES_SAVED = "noPlacesSaved";

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, result);
        preferences = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putInt(INDEX_ROW, -1).apply();
        configureViewModel();
        configureRecyclerView();
        configureOnClickRecyclerView();
        //if it s results activity
        if (preferences.getInt(KEY_RESULTS_ACTIVITY, -1) == 1) {
            floatingButtonAddPlace.setVisibility(View.GONE);
            List<PlaceAddressesPhotosAndInterests> results = retrievedPlaces();
            updateData(results);

        } else {
            viewModel.getAllData().observe(this, new Observer<List<PlaceAddressesPhotosAndInterests>>() {
                @Override
                public void onChanged(List<PlaceAddressesPhotosAndInterests> list) {
                    if (list.size() == 0) {
                        preferences.edit().putInt(NO_PLACES_SAVED, 1).apply();
                    } else {
                        preferences.edit().putInt(NO_PLACES_SAVED, -1).apply();
                    }
                    updateData(list);
                }
            });
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("on destroy view frag list");
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, -1).apply();
        System.out.println("key result = " + preferences.getInt(KEY_RESULTS_ACTIVITY, -1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("on dstroy frag list");
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, -1).apply();
        System.out.println("key result = " + preferences.getInt(KEY_RESULTS_ACTIVITY, -1));
    }

    //-------------------------------------------
    //CONFIGURATION
    //-------------------------------------------
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void configureRecyclerView() {
        adapterPlace = new RecyclerViewPlaceAdapter(Glide.with(this));
        recyclerView.setAdapter(adapterPlace);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * Configure the on click method on items of the list
     * if user click long, he/she can
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        PlaceAddressesPhotosAndInterests place = adapterPlace.getPlaceAddressesPhotosAndInterests(position);
                        preferences.edit().putLong(PLACE_ID, place.getPlace().getId()).apply();
                        preferences.edit().putLong(ADDRESS_ID, place.getPlace().getIdAddress()).apply();
                        String appMode = preferences.getString(APP_MODE, null);

                        if (appMode.equals(getString(R.string.app_mode_tablet))) {
                            preferences.edit().putInt(INDEX_ROW, position).apply();
                            adapterPlace.notifyDataSetChanged();
                            ((MainActivity)getActivity()).refreshFragmentInfo();

                        } else {
                            Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                            startActivity(detailIntent);
                        }
                    }
                })
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        PlaceAddressesPhotosAndInterests place = adapterPlace.getPlaceAddressesPhotosAndInterests(position);
                        displayLongClickDialog(place);

                        return false;
                    }
                });
    }

    //----------------------------------------------
    //ACTIONS
    //------------------------------------------------
    @OnClick(R.id.add_floating_action_button)
    public void launchAddPlaceFormActivity() {
        preferences.edit().putInt(STATUS_FORM_ACTIVITY, 0).apply();
        Intent intent = new Intent(getContext(), AddFormActivity.class);
        startActivity(intent);
    }


    //-----------------------------------------------
    //GET DATA
    //------------------------------------------------
    /**
     * update data for recycler view
     * @param list of PlaceAddressesPhotosAndInterests (all that are saved or only results of the search activity)
     */
    private void updateData(List<PlaceAddressesPhotosAndInterests> list) {
        adapterPlace.updatePlaceData(list);
    }

    /**
     * @return the list of PlaceAddressesPhotosAndInterests results of the search activity
     */
    //FOR RESULTS ACTIVITY
    private List<PlaceAddressesPhotosAndInterests> retrievedPlaces() {
        List<PlaceAddressesPhotosAndInterests> placeList;
        Gson gson = new Gson();
        String json = preferences.getString(QUERRIED_PLACES, null);
        Type type = new TypeToken<List<PlaceAddressesPhotosAndInterests>>() {
        }.getType();
        placeList = gson.fromJson(json, type);
        return placeList;
    }

    //-----------------------------------
    //
    //------------------------------------------------------
    /**
     * configure on long click on item of the list
     * user can edit the place
     * @param place the item selected
     */
    private void displayLongClickDialog(PlaceAddressesPhotosAndInterests place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(longClickFunctionality, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.edit().putLong(PLACE_ID, place.getPlace().getId()).apply();
                preferences.edit().putInt(STATUS_FORM_ACTIVITY, 1).apply();
                Intent editIntent = new Intent(getContext(), AddFormActivity.class);
                startActivity(editIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
