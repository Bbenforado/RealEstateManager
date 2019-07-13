package com.example.realestatemanager.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.type_of_place_text_view) TextView typeOfPlaceTextView;
    @BindView(R.id.status_text_view) TextView statusTextView;
    @BindView(R.id.city_text_view) TextView cityTextView;
    @BindView(R.id.price_text_view) TextView priceTextView;



    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void updateUi(Place place, Address address, Context context) {
        this.typeOfPlaceTextView.setText(place.getType());
        this.priceTextView.setText(String.valueOf(place.getPrice()));
        if (address.getIdPlace() == place.getId()) {
            this.cityTextView.setText(address.getCity());
        }
        if (place.getDateOfSale() != null) {
            statusTextView.setText("Sold");
            statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            statusTextView.setText("Available");
            statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        }
    }

}
