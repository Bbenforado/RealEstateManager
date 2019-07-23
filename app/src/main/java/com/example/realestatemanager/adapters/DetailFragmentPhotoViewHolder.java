package com.example.realestatemanager.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragmentPhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_view_detail_photo)
    ImageView imageView;
    @BindView(R.id.text_view_photo_description)
    TextView textViewDescription;

    public DetailFragmentPhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateUi(Photo photo, RequestManager glide) {
        String url = photo.getUri();
        if (photo.getDescription() != null) {
            textViewDescription.setText(photo.getDescription());
        } else {
            textViewDescription.setText("");
        }
        glide.load(url).apply(RequestOptions.noTransformation()).into(imageView);
    }
}
