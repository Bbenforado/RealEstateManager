package com.example.realestatemanager.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
    private List<Photo> photoList;
    private Context context;
    private RequestManager glide;
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    private static final String INDEX_ROW = "index";
    private static final String APP_MODE = "appMode";


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
        preferences = view.getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {

        if (placeList.size()> 0 && addressList.size()>0) {
            if (preferences.getString(APP_MODE, null).equals("tablet")) {
                int index = preferences.getInt(INDEX_ROW, -1);
                if (index == position) {
                    holder.priceTextView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                    holder.statusTextView.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.priceTextView.setTextColor(context.getResources().getColor(R.color.blue));
                    holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.statusTextView.setTextColor(context.getResources().getColor(R.color.blue));
                }
            }
            holder.updateUi(placeList.get(position), addressList.get(position),
                    photoList, context, this.glide);
        }
        if (placeList.size() == 0) {

        }
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


}
