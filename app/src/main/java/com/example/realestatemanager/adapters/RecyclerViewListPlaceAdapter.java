package com.example.realestatemanager.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewListPlaceAdapter extends RecyclerView.Adapter<ListPlaceViewHolder> {

    private List<PlaceAddressesPhotosAndInterests> placeList;
    private Context context;
    private RequestManager glide;
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    private static final String INDEX_ROW = "index";
    private static final String APP_MODE = "appMode";


    public RecyclerViewListPlaceAdapter(RequestManager glide) {
        this.placeList = new ArrayList<>();
        this.glide = glide;
    }

    @NonNull
    @Override
    public ListPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, viewGroup, false);

        return new ListPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPlaceViewHolder holder, int position) {
        if (placeList.size() > 0) {
            if (preferences.getString(APP_MODE, null).equals("tablet")) {
                int index = preferences.getInt(INDEX_ROW, -1);
                if (index == position) {
                    holder.priceTextView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                    holder.statusTextView.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.priceTextView.setTextColor(context.getResources().getColor(R.color.blue));
                    holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.statusTextView.setTextColor(context.getResources().getColor(R.color.blue));
                }
            }
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
