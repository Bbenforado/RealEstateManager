package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Interest;

import java.util.ArrayList;
import java.util.List;

public class InterestRecyclerViewAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private List<Interest> interestList;
    private Context context;

    public InterestRecyclerViewAdapter() {
        this.interestList = new ArrayList<>();
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_detail_recycler_view_interest_item, viewGroup, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        holder.updateInterestList(interestList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    public Interest getInterest(int position) {
        return this.interestList.get(position);
    }


    public void updateInterestData(List<Interest> interests) {
        this.interestList = interests;
        notifyDataSetChanged();
    }
}
