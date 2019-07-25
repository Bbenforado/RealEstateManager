package com.example.realestatemanager.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo_recycler_view_item_image)
    ImageView imageView;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateUi(Photo photo, RequestManager glide) {

        System.out.println("update ui holder photo");

        String image = photo.getUri();
        glide.load(image).apply(RequestOptions.noTransformation()).into(imageView);
    }
}
