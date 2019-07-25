package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private List<Place> placeList;
    private List<Address> addressList;
    private List<Interest> interestList;
    private List<Photo> photoList;
    //private PlaceViewModel viewModel;
    private Context context;
    private RequestManager glide;

    /*public interface OnItemClickListener {
        void onItemClick(Place item);
    }*/

    public PlaceRecyclerViewAdapter(RequestManager glide) {
        this.placeList = new ArrayList<>();
        this.addressList = new ArrayList<>();
        this.photoList = new ArrayList<>();
        this.glide = glide;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, viewGroup, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.updateUi(placeList.get(position), addressList.get(position),
                photoList, context, this.glide);
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

    public void updatePhotoData(List<Photo> photos) {
        this.photoList = photos;
        notifyDataSetChanged();
    }

    public void updateInterestData(List<Interest> interests) {
        this.interestList = interests;
        notifyDataSetChanged();
    }

}
