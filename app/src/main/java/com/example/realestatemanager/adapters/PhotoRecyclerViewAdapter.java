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
import com.example.realestatemanager.models.Photo;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private List<Photo> photoList;
    private RequestManager glide;
    private static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    public static final String APP_PREFERENCES = "appPreferences";
    private int statusActivity;

    public PhotoRecyclerViewAdapter(List<Photo> photos, RequestManager glide) {
        this.glide = glide;
        this.photoList = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.photo_recycler_view_item, parent, false);
        SharedPreferences preferences = view.getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        statusActivity = preferences.getInt(STATUS_FORM_ACTIVITY, -1);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        holder.updateUi(photoList.get(position), this.glide, this.statusActivity);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public Photo getItem(int position) {
        return photoList.get(position);
    }
}
