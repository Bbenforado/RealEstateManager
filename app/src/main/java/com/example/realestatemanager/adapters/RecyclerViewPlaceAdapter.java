package com.example.realestatemanager.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPlaceAdapter extends RecyclerView.Adapter<ViewHolderPlace> {

    private List<PlaceAddressesPhotosAndInterests> placeList;
    private Context context;
    private RequestManager glide;
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    private static final String INDEX_ROW = "index";
    private static final String APP_MODE = "appMode";


    public RecyclerViewPlaceAdapter(RequestManager glide) {
        this.placeList = new ArrayList<>();
        this.glide = glide;
    }

    @NonNull
    @Override
    public ViewHolderPlace onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, viewGroup, false);
        preferences = view.getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        return new ViewHolderPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlace holder, int position) {
        if (placeList.size() > 0) {
            holder.updateUi(this.placeList.get(position), context, glide);
        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public PlaceAddressesPhotosAndInterests getPlaceAddressesPhotosAndInterests(int position) {
        return this.placeList.get(position);
    }

    public void updatePlaceData(List<PlaceAddressesPhotosAndInterests> places) {
        this.placeList = places;
        this.notifyDataSetChanged();
    }
}