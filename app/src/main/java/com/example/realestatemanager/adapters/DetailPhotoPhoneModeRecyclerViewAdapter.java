package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoPhoneModeRecyclerViewAdapter extends RecyclerView.Adapter<DetailFragmentPhotoPhoneModeViewHolder> {

    private List<Photo> photoList;
    private RequestManager glide;

    public DetailPhotoPhoneModeRecyclerViewAdapter(RequestManager glide) {
        this.photoList = new ArrayList<>();
        this.glide = glide;
    }

    @NonNull
    @Override
    public DetailFragmentPhotoPhoneModeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_detail_recycler_view_photos_item, viewGroup, false);
        return new DetailFragmentPhotoPhoneModeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFragmentPhotoPhoneModeViewHolder holder, int position) {
        String numberOfPictures = String.valueOf(photoList.size());

        holder.textViewNumberPictures.setText(numberOfPictures);
        holder.updateUi(photoList.get(position), glide);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public Photo getPhoto(int position) {
        return this.photoList.get(position);
    }


    public void updatePhotoData(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }
}
