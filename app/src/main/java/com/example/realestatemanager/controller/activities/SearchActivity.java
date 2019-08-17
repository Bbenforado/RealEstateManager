package com.example.realestatemanager.controller.activities;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.PlaceAddressesPhotosAndInterests;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class SearchActivity extends AppCompatActivity {
    //-----------------------------------------------
    //BIND VIEWS
    //---------------------------------------------------
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.material_button_type_of_place_search_activity) MaterialButton buttonTypeOfPlace;
    @BindView(R.id.material_button_search_search_activity) MaterialButton buttonSearch;
    @BindView(R.id.text_edit_min_price_search_activity) TextInputEditText editTextMinPrice;
    @BindView(R.id.text_edit_max_price_search_activity) TextInputEditText editTextMaxPrice;
    @BindView(R.id.text_edit_min_surface_search_activity) TextInputEditText editTextMinSurface;
    @BindView(R.id.text_edit_max_surface_search_activity) TextInputEditText editTextMaxSurface;
    @BindView(R.id.text_edit_min_nbr_rooms_search_activity) TextInputEditText editTextMinNbrRooms;
    @BindView(R.id.text_edit_max_nbr_rooms_search_activity) TextInputEditText editTextMaxNbrRooms;
    @BindView(R.id.text_edit_min_nbr_bedrooms_search_activity) TextInputEditText editTextMinNbrbedrooms;
    @BindView(R.id.text_edit_max_nbr_bedrooms_search_activity) TextInputEditText editTextMaxNbrbedrooms;
    @BindView(R.id.text_edit_max_nbr_bathrooms_search_activity) TextInputEditText editTextMaxNbrBathrooms;
    @BindView(R.id.text_edit_min_nbr_bathrooms_search_activity) TextInputEditText editTextMinNbrBathrooms;
    @BindView(R.id.material_button_status_search_activity) MaterialButton buttonStatus;
    @BindView(R.id.spinner_button_creation_date_min_search_activity) Button buttonCreationDateMin;
    @BindView(R.id.spinner_button_creation_date_max_search_activity) Button buttonCreationDateMax;
    @BindView(R.id.spinner_button_sale_date_min_search_activity) Button buttonSaleDateMin;
    @BindView(R.id.spinner_button_sale_date_max_search_activity) Button buttonSaleDateMax;
    @BindView(R.id.checkboxMarketPlace) CheckBox checkBoxMarketPlace;
    @BindView(R.id.checkboxPark) CheckBox checkBoxPark;
    @BindView(R.id.checkboxHospital) CheckBox checkBoxHospital;
    @BindView(R.id.checkboxCinema) CheckBox checkBoxCinema;
    @BindView(R.id.checkboxTheater) CheckBox checkBoxTheater;
    @BindView(R.id.checkboxSchool) CheckBox checkBoxSchool;
    @BindView(R.id.text_edit_min_nbr_photos_search_activity) TextInputEditText editTextNbrOfPhotos;
    @BindView(R.id.text_edit_city_search_activity) TextInputEditText editTextCity;
    //--------------------------------------------------
    //-----------------------------------------------------
    private String[] typesOfPlace = {"Loft", "Mansion", "Penthouse", "Duplex"};
    private String[] status = {"Available", "Sold"};
    private SharedPreferences preferences;
    private List<CheckBox> checkBoxes;
    private List<String> interests;
    private PlaceViewModel viewModel;
    private SimpleDateFormat formatter;
    private String strQuery;
    private String strInterests;
    @State String textToSaveTypeOfPlaceButton;
    @State String textToSaveCreationDateMin;
    @State String textToSaveCreationDateMax;
    @State String textToSaveSaleDateMin;
    @State String textToSaveSaleDateMax;
    @State String getTextToSaveStatus;
    //-------------------------------------------------------------
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String CREATION_DATE_MIN = "creationDateMin";
    public static final String CREATION_DATE_MAX = "creationDateMax";
    public static final String DATE_OF_SALE_MIN = "dateSaleMin";
    public static final String DATE_OF_SALE_MAX = "dateSaleMax";
    public static final String QUERRIED_PLACES = "querriedPlaces";
    public static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        Icepick.restoreInstanceState(this, savedInstanceState);
        restoreData();
        preferences.edit().putString(CREATION_DATE_MIN, null).apply();
        preferences.edit().putString(CREATION_DATE_MAX, null).apply();
        preferences.edit().putString(DATE_OF_SALE_MIN, null).apply();
        preferences.edit().putString(DATE_OF_SALE_MAX, null).apply();

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        interests = new ArrayList<>();
        checkBoxes = Arrays.asList(checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater);

        configureToolbar();
        configureViewModel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (preferences.getString(CREATION_DATE_MIN, null) != null) {
            textToSaveCreationDateMin = preferences.getString(CREATION_DATE_MIN, null);
        }
        if(preferences.getString(CREATION_DATE_MAX, null) != null) {
            textToSaveCreationDateMax = preferences.getString(CREATION_DATE_MAX, null);
        }
        if (preferences.getString(DATE_OF_SALE_MIN, null) != null) {
            textToSaveSaleDateMin = preferences.getString(DATE_OF_SALE_MIN, null);
        }
        if (preferences.getString(DATE_OF_SALE_MAX, null) != null) {
            textToSaveSaleDateMax = preferences.getString(DATE_OF_SALE_MAX, null);
        }
        if (buttonTypeOfPlace.getText() != "Type of place") {
            textToSaveTypeOfPlaceButton = buttonTypeOfPlace.getText().toString();
        }
        if (buttonStatus.getText() != "Status") {
            getTextToSaveStatus = buttonStatus.getText().toString();
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    //-----------------------------------------------
    //CONFIGURATION
    //------------------------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }
    //---------------------------------------------------
    //ACTIONS
    //------------------------------------------------------
    @OnClick(R.id.material_button_type_of_place_search_activity)
    public void selectTypeOfPlace() {
        displayDialog(typesOfPlace, buttonTypeOfPlace, getString(R.string.title_dialog_choose_type_of_place));
    }

    @OnClick(R.id.material_button_search_search_activity)
    public void searchPlacesDependingOnFieldsContent() {
        if (areFieldsCorrectlyFilled()) {
            queryResults();
        } else {
            Toast.makeText(this, getString(R.string.toast_message_field_value_missing), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.material_button_status_search_activity)
    public void selectStatus() {
        displayDialog(status, buttonStatus, getString(R.string.dialog_title_select_status));
    }

    @OnClick(R.id.spinner_button_creation_date_min_search_activity)
    public void creationDateMinClick() {
        displayDatePicker(buttonCreationDateMin, CREATION_DATE_MIN);
    }

    @OnClick(R.id.spinner_button_creation_date_max_search_activity)
    public void creationDateMaxClick() {
        displayDatePicker(buttonCreationDateMax, CREATION_DATE_MAX);
    }

    @OnClick(R.id.spinner_button_sale_date_min_search_activity)
    public void saleDateMinClick() {
        displayDatePicker(buttonSaleDateMin, DATE_OF_SALE_MIN);
    }

    @OnClick(R.id.spinner_button_sale_date_max_search_activity)
    public void saleDateMaxClick() {
        displayDatePicker(buttonSaleDateMax, DATE_OF_SALE_MAX);
    }

    //----------------------------------------------
    //METHODS
    //------------------------------------------------
    private void restoreData() {
        setRestoredText(buttonCreationDateMin, textToSaveCreationDateMin);
        setRestoredText(buttonCreationDateMax, textToSaveCreationDateMax);
        setRestoredText(buttonSaleDateMin, textToSaveSaleDateMin);
        setRestoredText(buttonSaleDateMax, textToSaveSaleDateMax);
        setRestoredText(buttonTypeOfPlace, textToSaveTypeOfPlaceButton);
        setRestoredText(buttonStatus, getTextToSaveStatus);
    }

    private void setRestoredText(Button button, String text) {
        if (text != null) {
            button.setText(text);
        }
    }

    public boolean areFieldsCorrectlyFilled() {
        return !buttonTypeOfPlace.getText().equals("Type of place");
    }

    private boolean areInterestsChecked() {
        for (int i = 0; i<checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                return true;
            }
        }
        return false;
    }

    private void queryResults() {
        if (areFieldsCorrectlyFilled()) {
            String strQuery = buildQueryDependingOnCriteria();

            SimpleSQLiteQuery query = new SimpleSQLiteQuery(strQuery);
            viewModel.getPlacesAndData(query).observe(this, new Observer<List<PlaceAddressesPhotosAndInterests>>() {
                @Override
                public void onChanged(List<PlaceAddressesPhotosAndInterests> placeAddressesPhotosAndInterests) {
                    if (placeAddressesPhotosAndInterests.size() > 0) {
                        for (int i = 0; i < placeAddressesPhotosAndInterests.size(); i++) {
                            savePlaces(placeAddressesPhotosAndInterests);
                            launchResultsActivity();
                        }
                    } else {
                        displayDialogNoResultsFound();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_message_field_value_missing), Toast.LENGTH_SHORT).show();
        }
    }
    //----------------------------------------
    //BUILD QUERY
    //------------------------------------------
    private String buildQueryDependingOnCriteria() {
        strQuery = "SELECT *, count(*) FROM places, interests, addresses, photos";
        strInterests = null;
        if (areInterestsChecked()) {
            strInterests = getSelectedInterests();
        }
        int nbrOfPhotos = 0;
        if (!TextUtils.isEmpty(editTextNbrOfPhotos.getText().toString())) {
            nbrOfPhotos = Integer.parseInt(editTextNbrOfPhotos.getText().toString());
        }
        String city = null;
        if (!TextUtils.isEmpty(editTextCity.getText().toString())) {
            city = editTextCity.getText().toString();
        }
        String type = buttonTypeOfPlace.getText().toString();
        strQuery = strQuery + " WHERE places.idAddress = addresses.addressId AND places.id = interests.idPlace AND places.id = photos.placeId AND places.type = '" + type + "'";

        if (!buttonStatus.getText().equals(getString(R.string.status_available)) && !buttonStatus.getText().equals("Status")) {
            strQuery = strQuery + " AND places.dateOfSale IS NOT NULL";
        }
        buildQueryForParam(editTextMinPrice, "places.price", ">= ");
        buildQueryForParam(editTextMaxPrice, "places.price", "<= ");
        buildQueryForParam(editTextMinSurface, "places.surface", ">= ");
        buildQueryForParam(editTextMaxSurface, "places.surface", "<= ");
        buildQueryForParam(editTextMinNbrRooms, "places.nbrOfRooms", ">= ");
        buildQueryForParam(editTextMaxNbrRooms, "places.nbrOfRooms", "<= ");
        buildQueryForParam(editTextMinNbrbedrooms, "places.nbrOfBedrooms", ">= ");
        buildQueryForParam(editTextMaxNbrbedrooms, "places.nbrOfBedrooms", "<= ");
        buildQueryForParam(editTextMinNbrBathrooms, "places.nbrOfBathrooms", ">= ");
        buildQueryForParam(editTextMaxNbrBathrooms, "places.nbrOfBathrooms", "<= ");
        buildQueryForDates(CREATION_DATE_MIN, "creationDate", " > ");
        buildQueryForDates(CREATION_DATE_MAX, "creationDate", " <= ");
        buildQueryForDates(DATE_OF_SALE_MIN, "dateOfSale", " >= ");
        buildQueryForDates(DATE_OF_SALE_MAX, "dateOfSale", " <= ");

        if (areInterestsChecked()) {
            strQuery = strQuery + "  AND interests.interestType = '" + strInterests + "'";
        }
        if (city != null) {
            strQuery = strQuery + " AND UPPER(addresses.city) = UPPER('" + city + "')";
        }
        if (!TextUtils.isEmpty(editTextNbrOfPhotos.getText().toString())) {
            strQuery = strQuery + " AND places.id IN (SELECT photos.placeId FROM photos GROUP BY photos.placeId HAVING COUNT(photos.placeId) >= " + nbrOfPhotos + ")";
        }
        strQuery = strQuery + " GROUP BY places.id;";
        return strQuery;
    }

    private String getSelectedInterests() {
        interests.clear();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                interests.add(checkBoxes.get(i).getText().toString());
            }
        }
        if (interests.size()>1) {
            for (int i = 0; i < interests.size(); i++) {
                if (i == 0) {
                    strInterests =  interests.get(i);
                } else {
                    strInterests = strInterests + "' AND interests.type = '" + interests.get(i);
                }
            }
            return strInterests;
        }
        strInterests = interests.get(0);
        return strInterests;
    }

    private void buildQueryForParam(TextView textView, String columnName, String operator) {
        if (!TextUtils.isEmpty(textView.getText().toString())) {
            int nbr;
            nbr = Integer.parseInt(textView.getText().toString());
            strQuery = strQuery + " AND " + columnName + " " + operator + "'" + nbr + "'";
        }
    }

    private void buildQueryForDates(final String KEY, String columnName, String operator) {
        if (preferences.getString(KEY, null) != null) {
            String dateStr = preferences.getString(KEY, null);
            Date date = null;
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dateLong = date.getTime();
            strQuery = strQuery + " AND places." + columnName + operator + " '" + dateLong + "'";
        }
    }
    //------------------------------------
    //SAVE PLACES USING GSON
    //-------------------------------------
    private void savePlaces(List<PlaceAddressesPhotosAndInterests> places) {
        Gson gson = new Gson();
        preferences.edit().putString(QUERRIED_PLACES, gson.toJson(places)).apply();
    }
    //-----------------------------------------------
    //DIALOGS METHODS
    //--------------------------------------------------
    private void displayDialogNoResultsFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_no_results_found)
                .setMessage(R.string.dialog_message_try_again)
                .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayDialog(String[] choices, MaterialButton button, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = choices[which];
                button.setText(type);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayDatePicker(Button button, final String KEY) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH)+1;
                int currentYear = calendar.get(Calendar.YEAR);
                int newMonth = month + 1;
                String  day = Utils.addZeroToDate(String.valueOf(dayOfMonth));
                String formattedMonth = Utils.addZeroToDate(String.valueOf(newMonth));

                Utils.checkIfDateIsPassedOrCurrent(getApplicationContext(), day, formattedMonth, year, currentDay, currentMonth, currentYear,
                        button, preferences, KEY);

            }
        }, year, month, day);
        datePickerDialog.show();
    }
    //------------------------------------------
    //LAUNCH ACTIVITY
    //---------------------------------------------
    private void launchResultsActivity() {
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, 1).apply();
        Intent resultsActivity = new Intent(this, MainActivity.class);
        startActivity(resultsActivity);
    }
}
