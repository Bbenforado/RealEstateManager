package com.example.realestatemanager.utils;

import android.content.Context;

import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Photo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UtilsAddFormActivity {

    //ADD FORM ACTIVITY
    /**
     * format date, pattern : dd/MM/yyyy
     * @param date the date to format
     * @return formatted date
     */
    public static Date formatDate(String date) {
        Date formattedDate = null;
        try {
            formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    /**
     * get the latLng of an address and set it to the address
     * @param address the address of the place
     */
    public static void getAndSetLatLngOfPlace(Context context, Address address) {
        String finalAddress = address.getStreetNumber() + " " + address.getStreetName() + "," +
                address.getCity() + "," + address.getPostalCode() + " " +
                address.getCountry();

        String latLng = Utils.getLocationFromAddress(context, finalAddress);
        address.setLatLng(latLng);
    }

    public static boolean allPhotosHaveDescription(List<Photo> photos) {
        for (Photo photo : photos) {
            if (photo.getDescriptionPhoto() == null) {
                return false;
            }
        }
        return true;
    }
}
