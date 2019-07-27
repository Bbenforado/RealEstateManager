package com.example.realestatemanager.utils;


import android.content.Context;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.DetailRecyclerViewAdapter;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    //---------------------------------------------------------------------------------------------------------------------
    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 0.89);
    }

    public static String addZeroToDate(String string) {
        System.out.println("string add = " + string);
        if (string.length() == 1) {
            System.out.println("come here?");
            string = "0" + string;
            System.out.println("new string  = " + string);
            return string;
        }
        return string;
    }

    public static void checkIfDateIsPassedOrCurrent(Context context, String selectedDay, String selectedMonth, int selectedYear, int currentDay,
                                                    int currentMonth, int currentYear, Button button, SharedPreferences preferences,
                                                    final String KEY) {
        if (selectedYear < currentYear) {
            button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
            preferences.edit().putString(KEY, selectedDay +"/"+ selectedMonth +  "/" + selectedYear).apply();
        } else if(selectedYear == currentYear) {
            if (Integer.parseInt(selectedMonth) < currentMonth) {
                button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                preferences.edit().putString(KEY, selectedDay +"/"+ selectedMonth +  "/" + selectedYear).apply();
            } else if (Integer.parseInt(selectedMonth) == currentMonth) {
                if (Integer.parseInt(selectedDay) <= currentDay) {
                    button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                    preferences.edit().putString(KEY, selectedDay +"/"+ selectedMonth +  "/" + selectedYear).apply();
                } else {
                    Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
        }
    }


    /*public static String formatLocation(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }*/

    public static String getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        String latLngOfAddress = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() != 0) {
                Address location = address.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                latLngOfAddress = getParenthesesContent(latLng.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return latLngOfAddress;
    }

    private static String getParenthesesContent(String str){
        return str.substring(str.indexOf('(')+1,str.indexOf(')'));
    }

    public static LatLng getLatLngOfPlace(String latlng) {
        String[] retrievedLatLng = latlng.split(",");
        double latitude = Double.parseDouble(retrievedLatLng[0]);
        double longitude = Double.parseDouble(retrievedLatLng[1]);
        return new LatLng(latitude, longitude);
    }

    //for recycler view interests (detail frag tablet mode & information frag)

    public static void configureRecyclerViewForInterests(Context context, DetailRecyclerViewAdapter adapter, RecyclerView recyclerViewInterest) {
        //adapter = new DetailRecyclerViewAdapter();
        recyclerViewInterest.setAdapter(adapter);
        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(context));
    }

    /*public static void getInterests(long placeId, PlaceViewModel viewModel, LifecycleOwner owner, Context context, DetailRecyclerViewAdapter adapter) {
        viewModel.getInterests(placeId).observe(owner, new Observer<List<Interest>>() {
            @Override
            public void onChanged(List<Interest> interests) {
                if (interests.size()>0) {
                    updateInterestsList(interests, adapter);
                }
            }
        });
    }

    private static void updateInterestsList(List<Interest> interests, DetailRecyclerViewAdapter adapter) {
        if (interests.size()>0) {
            adapter.updateInterestData(interests);
        }
    }*/


}
