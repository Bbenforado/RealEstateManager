package com.example.realestatemanager.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.type_of_place_text_view) TextView typeOfPlaceTextView;
    @BindView(R.id.status_text_view) TextView statusTextView;
    @BindView(R.id.city_text_view) TextView cityTextView;
    @BindView(R.id.price_text_view) TextView priceTextView;
    @BindView(R.id.fragment_list_item_image)
    ImageView imageView;
    @BindView(R.id.relative_layout_item_list)
    RelativeLayout relativeLayout;



    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void updateUi(Place place, Address address, List<Photo> photos,
                         Context context, RequestManager glide) {
        this.typeOfPlaceTextView.setText(place.getType());
        String price = place.getPrice() + " $";
        this.priceTextView.setText(price);
        /*if (address.getIdPlace() != -1) {
            if (address.getIdPlace() == place.getId()) {
                this.cityTextView.setText(address.getCity());
            }
        }*/
        if (place.getIdAddress() != -1) {
            if (place.getIdAddress() == address.getId()) {
                this.cityTextView.setText(address.getCity());
            }
        }

        List<Photo> photoList = new ArrayList<>();
        if (photos.size() != 0) {
            for (int i = 0; i<photos.size(); i++ ) {
                if (photos.get(i).getPlaceId() == place.getId()) {
                    photoList.add(photos.get(i));
                }
            }
            if (photoList.size() > 1) {
                Photo mainPhoto = photoList.get(0);
                String path = mainPhoto.getUri();
                glide.load(path).apply(RequestOptions.noTransformation()).into(imageView);
            } else if (photoList.size() == 1) {
                glide.load(photoList.get(0).getUri()).apply(RequestOptions.noTransformation()).into(imageView);
            } else {
                glide.load(R.drawable.no_image).apply(RequestOptions.noTransformation()).into(imageView);
            }

        }

        if (place.getDateOfSale() != null) {
            statusTextView.setText(context.getString(R.string.status_sold));
            statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            statusTextView.setText(context.getString(R.string.status_available));
            statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        }
    }
}
