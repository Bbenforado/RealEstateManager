package com.example.realestatemanager.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;

import butterknife.BindView;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo_recycler_view_item_image)
    ImageView imageView;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateUi() {

    }
}
