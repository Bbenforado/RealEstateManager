package com.example.realestatemanager.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Interest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_interest) TextView interestTextView;
    @BindView(R.id.image_detail_interest) ImageView imageInterest;

    public InterestViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateInterestList(Interest interest, Context context) {
        interestTextView.setText(interest.getInterestType());
        String type = interest.getInterestType();
        switch (type) {
            case "Park":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_park));
                break;
            case "School":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_school));
                break;
            case "Market place":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_market_place));
                break;
            case "Hospital":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hospital));
                break;
            case "Cinema":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cinema));
                break;
            case "Theater":
                imageInterest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_theater));
                break;
                default:
                    break;
        }
    }
}
