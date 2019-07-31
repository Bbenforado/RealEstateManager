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

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo_recycler_view_item_image)
    ImageView imageView;

    @BindView(R.id.text_view_description_detail_fragment_tablet_mode)
    TextView textViewDescription;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateUi(Photo photo, RequestManager glide, int statusActivity) {

        String image = photo.getUri();
        glide.load(image).apply(RequestOptions.noTransformation()).into(imageView);
        if (statusActivity == 1 || statusActivity == 0) {
            textViewDescription.setVisibility(View.GONE);
        } else {
            if (photo.getDescription() != null) {
                textViewDescription.setText(photo.getDescription());
            }
        }
    }
}
