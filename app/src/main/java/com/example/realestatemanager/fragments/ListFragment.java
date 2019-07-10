package com.example.realestatemanager.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.PlaceRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ListFragment extends Fragment {

    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView recyclerView;
    private PlaceRecyclerViewAdapter adapter;
    private List<Place> placeList;
    private PlaceViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_list, container, false);
        View result = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, result);

        configureRecyclerView();

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);

        getPlaces();

        return result;
    }

    private void configureRecyclerView() {
        this.adapter = new PlaceRecyclerViewAdapter();
        this.recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    /*@Override
    public void onClickDeleteButton(int position) {

    }*/

    private void getPlaces() {
        viewModel.getPlaces().observe(this, this::updatePlacesList);
    }
    private void updatePlacesList(List<Place> places) {
        this.adapter.updateData(places);
    }
}
