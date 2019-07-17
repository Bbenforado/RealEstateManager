package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    private List<Interest> interestList;
    Context context;

    public DetailRecyclerViewAdapter() {
        this.interestList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_detail_recycler_view_interest_item, viewGroup, false);

        /*ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(context);
        PlaceViewModel viewModel = ViewModelProviders.of((FragmentActivity) context, viewModelFactory).get(PlaceViewModel.class);*/

        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
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
