package com.example.realestatemanager.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Place;

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

    public void updateUi(Place place) {
        //this.callbackWeakRef = new WeakReference<PlaceRecyclerViewAdapter.Listener>(callback);
        this.typeOfPlaceTextView.setText(place.getType());
        this.priceTextView.setText(String.valueOf(place.getPrice()));
    }

    /*@Override
    public void onClick(View view) {
        PlaceRecyclerViewAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }*/
}
