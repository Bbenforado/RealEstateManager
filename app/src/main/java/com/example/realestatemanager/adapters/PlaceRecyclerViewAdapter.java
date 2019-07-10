package com.example.realestatemanager.adapters;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.ListFragment;
import com.example.realestatemanager.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private List<Place> placeList;
    public interface Listener {void onClickDeleteButton(int position);}
   // private final Listener callback;

    public PlaceRecyclerViewAdapter() {
        this.placeList = new ArrayList<>();
        //this.callback = callback;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, viewGroup, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.updateUi(this.placeList.get(position));

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void updateData(List<Place> places) {
        this.placeList = places;
        this.notifyDataSetChanged();
    }
}
