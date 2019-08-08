package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderPlace extends RecyclerView.ViewHolder {

    @BindView(R.id.type_of_place_text_view)
    TextView typeOfPlaceTextView;
    @BindView(R.id.status_text_view) TextView statusTextView;
    @BindView(R.id.city_text_view) TextView cityTextView;
    @BindView(R.id.price_text_view) TextView priceTextView;
    @BindView(R.id.fragment_list_item_image)
    ImageView imageView;
    @BindView(R.id.relative_layout_item_list)
    RelativeLayout relativeLayout;


    public ViewHolderPlace(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateUi(PlaceAddressesPhotosAndInterests placeAddressesPhotosAndInterests,
                         Context context, RequestManager glide) {
        this.typeOfPlaceTextView.setText(placeAddressesPhotosAndInterests.getPlace().getType());
        String price = placeAddressesPhotosAndInterests.getPlace().getPrice() + " $";
        this.priceTextView.setText(price);
        this.cityTextView.setText(placeAddressesPhotosAndInterests.getAddress().getCity());

        List<Photo> photoList = new ArrayList<>();
        if (placeAddressesPhotosAndInterests.getPhotos().size() != 0) {
            for (int i = 0; i<placeAddressesPhotosAndInterests.getPhotos().size(); i++ ) {
                if (placeAddressesPhotosAndInterests.getPhotos().get(i).getPlaceId() == placeAddressesPhotosAndInterests.getPlace().getId()) {
                    photoList.add(placeAddressesPhotosAndInterests.getPhotos().get(i));
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

        if (placeAddressesPhotosAndInterests.getPlace().getDateOfSale() != null) {
            statusTextView.setText(context.getString(R.string.status_sold));
            statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            statusTextView.setText(context.getString(R.string.status_available));
            statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        }
    }
}
