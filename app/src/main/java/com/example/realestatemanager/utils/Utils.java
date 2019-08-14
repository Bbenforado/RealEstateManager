package com.example.realestatemanager.utils;


import android.content.Context;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.InterestRecyclerViewAdapter;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

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
        return (int) Math.round(dollars * 0.905352);
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

    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 1.10454);
    }
    //---------------------------------------------------------------------------------------------------------------------
    //APP
    //--------------------------
    /**
     * verify if there is a internet connection
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //------------------------------------
    //ADD FORM / SEARCH ACTIVITY
    //------------------------------------
    public static String addZeroToDate(String string) {
        if (string.length() == 1) {
            string = "0" + string;
            return string;
        }
        return string;
    }
    //-----------------------------------
    //SEARCH ACTIVITY
    //--------------------------------------
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
    //------------------------------------------
    //ADD FORM ACTIVITY
    //------------------------------------------
    public static String checkIfDateIsPassedOrCurrentAndReturnString(Context context, String selectedDay, String selectedMonth, int selectedYear, int currentDay,
                                                    int currentMonth, int currentYear, Button button) {
        if (selectedYear < currentYear) {
            button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
            return selectedDay + "/" + selectedMonth + "/" + selectedYear;
        } else if(selectedYear == currentYear) {
            if (Integer.parseInt(selectedMonth) < currentMonth) {
                button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                return selectedDay + "/" + selectedMonth + "/" + selectedYear;
            } else if (Integer.parseInt(selectedMonth) == currentMonth) {
                if (Integer.parseInt(selectedDay) <= currentDay) {
                    button.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                    return selectedDay + "/" + selectedMonth + "/" + selectedYear;
                } else {
                    Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else {
                Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            Toast.makeText(context, context.getString(R.string.toast_message_date_not_correct), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //------------------------------------
    //MAP ACTIVITY / MAP FRAGMENT / UTILS ADD FORM ACTIVITY
    //-----------------------------------------
    public static String getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng;
        String latLngOfAddress = null;
        try {
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
    //--------------------------------
    public static String getParenthesesContent(String str){
        return str.substring(str.indexOf('(')+1,str.indexOf(')'));
    }

    private static void displayStatusOfPlace(Context context, Place place, TextView statusTextView,
                                             TextView dateOfSaleTextView, LinearLayout layoutDateOfSale) {
        if (place.getDateOfSale() == null) {
            layoutDateOfSale.setVisibility(View.GONE);
            statusTextView.setText(context.getString(R.string.status_available));
            statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            layoutDateOfSale.setVisibility(View.VISIBLE);
            dateOfSaleTextView.setText(new SimpleDateFormat("dd/MM/yyyy").format(place.getDateOfSale()));
            statusTextView.setText(context.getString(R.string.status_sold));
            statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    private static void setInformationOnTextView(Context context, int number, TextView textView) {
        if (number != 0) {
            textView.setText(String.valueOf(number));
        } else {
            textView.setText(context.getString(R.string.not_informed_yet));
        }
    }
    //-------------------------------------
    //DETAIL FRAGMENT / MAP ACTIVITY / MAP FRAGMENT
    //------------------------------------------------
    public static LatLng getLatLngOfPlace(String latlng) {
        String[] retrievedLatLng = latlng.split(",");
        double latitude = Double.parseDouble(retrievedLatLng[0]);
        double longitude = Double.parseDouble(retrievedLatLng[1]);
        return new LatLng(latitude, longitude);
    }
    //--------------------------------------------
    //DETAIL FRAGMENT / INFORMATION FRAGMENT
    //----------------------------------------------
    public static void updateUiPlace(Context context, Place place, TextView managerTextView, TextView creationDateTextView,
                                     TextView priceTextView, TextView descriptionTextView,
                                     TextView surfaceTextView, TextView nbrRoomsTextView,
                                     TextView nbrBedroomsTextView, TextView nbrBathroomsTextView,
                                     TextView statusTextView, TextView dateOfSaleTextView, LinearLayout layoutDateOfSale) {
        managerTextView.setText(place.getAuthor());
        displayStatusOfPlace(context, place, statusTextView, dateOfSaleTextView, layoutDateOfSale);
        creationDateTextView.setText(new SimpleDateFormat("dd/MM/yyyy").format(place.getCreationDate()));
        String price = place.getPrice() + " $";
        priceTextView.setText(price);

        if (place.getDescription() != null) {
            descriptionTextView.setText(place.getDescription());
        } else {
            descriptionTextView.setText(context.getString(R.string.not_informed_yet));
        }
        if (place.getSurface() != 0) {
            String surface = place.getSurface() + " m²";
            surfaceTextView.setText(surface);
        } else {
            surfaceTextView.setText(context.getString(R.string.not_informed_yet));
        }
        setInformationOnTextView(context, place.getNbrOfRooms(), nbrRoomsTextView);
        setInformationOnTextView(context, place.getNbrOfBedrooms(), nbrBedroomsTextView);
        setInformationOnTextView(context, place.getNbrOfBathrooms(), nbrBathroomsTextView);
    }

    public static void updateUiAddress(Context context, Place place, PlaceViewModel viewModel, LifecycleOwner owner,
                                       TextView textViewStreet, TextView textViewComplement,
                                       TextView textViewPostalCodeAndCity, TextView textViewCountry) {
        viewModel.getAddressOfAPlace(place.getIdAddress()).observe(owner, new Observer<com.example.realestatemanager.models.Address>() {
            @Override
            public void onChanged(com.example.realestatemanager.models.Address address) {
                textViewStreet.setText(address.getStreetNumber() + " " + address.getStreetName());
                if (address.getComplement() != null) {
                    if (!address.getComplement().equals(context.getString(R.string.not_informed))) {
                        textViewComplement.setText(address.getComplement());
                    } else {
                        textViewComplement.setVisibility(View.GONE);
                    }
                } else {
                    textViewComplement.setVisibility(View.GONE);
                }
                textViewPostalCodeAndCity.setText(address.getPostalCode() + " " + setFirstLetterUpperCase(address.getCity()));
                textViewCountry.setText(setFirstLetterUpperCase(address.getCountry()));
            }
        });
    }

    public static void convertPrice(Context context, MaterialButton button, TextView priceTextView, long price) {
        if (button.getText().toString().equals(context.getString(R.string.button_text_convert_to_euros))) {
            int priceInEuros = convertDollarToEuro((int)price);
            String priceEuro = priceInEuros + " €";
            priceTextView.setText(priceEuro);
            button.setText(context.getString(R.string.button_text_convert_to_dollars));
        } else if (button.getText().toString().equals(context.getString(R.string.button_text_convert_to_dollars))) {
            String priceDollars = price + " $";
            priceTextView.setText(priceDollars);
            button.setText(context.getString(R.string.button_text_convert_to_euros));
        }
    }
    //----------------------------------------
    //LIST PLACE VIEW HOLDER
    //-------------------------------------------
    public static String setFirstLetterUpperCase(String str) {
        String stringToFormat = str;
        if (!Character.isUpperCase(stringToFormat.codePointAt(0))) {
            stringToFormat = str.substring(0, 1).toUpperCase() + str.substring(1);
            return stringToFormat;
        }
        return stringToFormat;
    }
}
