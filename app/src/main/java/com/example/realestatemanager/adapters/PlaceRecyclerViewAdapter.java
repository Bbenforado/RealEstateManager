package com.example.realestatemanager.adapters;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.ListFragment;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private List<Place> placeList;
    private List<Address> addressList;
    PlaceViewModel viewModel;
    Context context;

    public PlaceRecyclerViewAdapter() {
        this.placeList = new ArrayList<>();
        this.addressList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, viewGroup, false);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(context);
        viewModel = ViewModelProviders.of((FragmentActivity) context, viewModelFactory).get(PlaceViewModel.class);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {

        holder.updateUi(placeList.get(position), addressList.get(position), context);


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public Place getPlace(int position) {
        return this.placeList.get(position);
    }

    public void updatePlaceData(List<Place> places) {
        this.placeList = places;
        this.notifyDataSetChanged();
    }

    public void updateAddressData(List<Address> addresses) {
        this.addressList = addresses;
        notifyDataSetChanged();
    }

}
