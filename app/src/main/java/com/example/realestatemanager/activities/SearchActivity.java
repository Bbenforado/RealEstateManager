package com.example.realestatemanager.activities;

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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.ListFragment;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String CREATION_DATE_MIN = "creationDateMin";
    public static final String CREATION_DATE_MAX = "creationDateMax";
    public static final String DATE_OF_SALE_MIN = "dateSaleMin";
    public static final String DATE_OF_SALE_MAX = "dateSaleMax";
    public static final String PLACE_IDS = "placeIds";
    private List<CheckBox> checkBoxes;
    private List<String> interests;
    private PlaceViewModel viewModel;
    private List<Long> placeIds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        interests = new ArrayList<>();
        placeIds = new ArrayList<>();
        checkBoxes = Arrays.asList(checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater);

        configureToolbar();
        configureViewModel();
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
        /*AlertDialog dialog = Utils.displayDialogForTypeOfPlace(getApplicationContext(), typesOfPlace, buttonTypeOfPlace);
        dialog.show();*/
        displayDialog(typesOfPlace, buttonTypeOfPlace, getString(R.string.title_dialog_choose_type_of_place));
    }

    @OnClick(R.id.material_button_search_search_activity)
    public void searchPlacesDependingOnFieldsContent() {
        if (areAllFieldsCorrectlyFilled()) {
            queryResults();
        } else {
            Toast.makeText(this, "Fields are not correctly filled", Toast.LENGTH_SHORT).show();
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

    private boolean areAllFieldsCorrectlyFilled() {
        return !TextUtils.isEmpty(editTextMinPrice.getText())
                && !TextUtils.isEmpty(editTextMinSurface.getText())
                && !TextUtils.isEmpty(editTextMinNbrRooms.getText())
                && !TextUtils.isEmpty(editTextMinNbrbedrooms.getText())
                && !TextUtils.isEmpty(editTextMinNbrBathrooms.getText())
                && preferences.getString(CREATION_DATE_MIN, null) != null;
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

    private boolean areInterestsChecked() {
        for (int i = 0; i<checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                return true;
            }
        }
        return false;
    }

    private String getSelectedInterests() {
        String strInterests = null;
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                interests.add(checkBoxes.get(i).getText().toString());
            }
        }
        if (interests.size()>1) {
            for (int i = 0; i < interests.size(); i++) {
                if (i == 1) {
                    strInterests =  interests.get(i)+ "'";
                } else {
                    strInterests = strInterests + " AND '" + interests.get(i) + "'";
                }
            }
            return strInterests;
        }
        strInterests = interests.get(0);
        return strInterests;
    }



    private void queryResults() {
        //getSelectedInterests();
        String type = buttonTypeOfPlace.getText().toString();

        String strQuery = "SELECT * FROM places WHERE type = '" + type + "'" ;


        if (areInterestsChecked()) {
            String strInterests = getSelectedInterests();

            strQuery = "SELECT DISTINCT idPlace FROM interests WHERE type = '" + strInterests +
                    "' AND idPlace IN ( SELECT id FROM places WHERE type = '" + type + "'";
        }

        int status;
        if (buttonStatus.getText().equals(getString(R.string.status_available))) {
            status = 0;

        } else {
            status = 1;
            strQuery = strQuery + " AND dateOfSale IS NOT NULL";
        }



        long minPrice = Long.parseLong(editTextMinPrice.getText().toString());

        strQuery = strQuery + " AND price >= '" + minPrice + "'";

        long maxPrice = 0;
        if (!TextUtils.isEmpty(editTextMaxPrice.getText().toString())) {
            maxPrice = Long.parseLong(editTextMaxPrice.getText().toString());
            strQuery = strQuery + " AND price <= '" + maxPrice + "'";
        }
        int minSurface = Integer.parseInt(editTextMinSurface.getText().toString());
        strQuery = strQuery + " AND surface >= '" + minSurface + "'";
        long maxSurface = 0;
        if (!TextUtils.isEmpty(editTextMaxSurface.getText().toString())) {
            maxSurface = Integer.parseInt(editTextMaxSurface.getText().toString());
            strQuery = strQuery + " AND surface <= '" + maxSurface + "'";
        }

        int minNbrOfRooms = Integer.parseInt(editTextMinNbrRooms.getText().toString());
        strQuery = strQuery + " AND nbrOfRooms >= '" + minNbrOfRooms + "'";
        int maxNbrOfRooms = 0;
        if (!TextUtils.isEmpty(editTextMaxNbrRooms.getText().toString())) {
            maxNbrOfRooms = Integer.parseInt(editTextMaxNbrRooms.getText().toString());
            strQuery = strQuery + " AND nbrOfRooms <= '" + maxNbrOfRooms + "'";
        }

        int minNbrOfBedrooms = Integer.parseInt(editTextMinNbrbedrooms.getText().toString());
        strQuery = strQuery + " AND nbrOfBedrooms >= '" + minNbrOfBedrooms + "'";
        int maxNbrOfBedrooms = 0;
        if (!TextUtils.isEmpty(editTextMaxNbrbedrooms.getText().toString())) {
            maxNbrOfRooms = Integer.parseInt(editTextMaxNbrbedrooms.getText().toString());
            strQuery = strQuery + " AND nbrOfBedrooms <= '" + maxNbrOfBedrooms + "'";
        }

        int minNbrOfBathrooms = Integer.parseInt(editTextMinNbrBathrooms.getText().toString());
        strQuery = strQuery + " AND nbrOfBathrooms >= '" + minNbrOfBathrooms + "'";
        int maxNbrOfBathrooms = 0;
        if (!TextUtils.isEmpty(editTextMaxNbrBathrooms.getText().toString())) {
            maxNbrOfRooms = Integer.parseInt(editTextMaxNbrBathrooms.getText().toString());
            strQuery = strQuery + " AND nbrOfBathrooms <= '" + maxNbrOfBathrooms + "'";
        }

        String creationDateMin = preferences.getString(CREATION_DATE_MIN, null);
        String creationDateMax = null;
        if (preferences.getString(CREATION_DATE_MAX, null) != null) {
            creationDateMax = preferences.getString(CREATION_DATE_MAX, null);
        }

        String dateOfSaleMin = null;
        String dateOfSaleMax = null;
        if (preferences.getString(DATE_OF_SALE_MIN, null) != null) {
            dateOfSaleMin = preferences.getString(DATE_OF_SALE_MIN, null);
        }
        if (preferences.getString(DATE_OF_SALE_MAX, null) != null) {
            dateOfSaleMax = preferences.getString(DATE_OF_SALE_MAX, null);
        }





        String finalQuery = strQuery + ");";

        /*SimpleSQLiteQuery query = new SimpleSQLiteQuery(finalQuery);

        viewModel.getPlaceForGivenParameters(query).observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                System.out.println("places = " + places.size());
            }
        });*/

        SimpleSQLiteQuery query = new SimpleSQLiteQuery(finalQuery);
        System.out.println("final query = " + finalQuery);

        /*viewModel.getPlaceIdForGivenParameters(query).observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> placesId) {
                System.out.println("places = " + placesId.size());
                for (int i = 0; i<placesId.size(); i++) {
                    placeIdFromInterests.addAll(placesId);
                }
            }
        });*/

        if (!TextUtils.isEmpty(editTextNbrOfPhotos.getText().toString())) {
            int nbrOfPhotos = Integer.parseInt(editTextNbrOfPhotos.getText().toString());
            String queryFromPhoto = "SELECT DISTINCT placeId FROM photos GROUP BY placeId HAVING COUNT (placeId)>= " + nbrOfPhotos + ";";

            SimpleSQLiteQuery query1 = new SimpleSQLiteQuery(queryFromPhoto);

            viewModel.getPlaceIdForGivenParam(query1).observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> placesId) {
                System.out.println("places = " + placesId.size());

            }
        });

        }

        /*SELECT
        projet.id, projet.nom,
                COUNT(ticket.numero) AS "Nombre ticket"
        FROM projet
        JOIN ticket ON ticket.projet_id = projet.id
        WHERE projet.cloture = FALSE
        GROUP BY projet.id, projet.nom
        HAVING COUNT(ticket.numero) > 10;       -- Ne garder que les lignes avec count(...) > 10*/

        if (!TextUtils.isEmpty(editTextCity.getText().toString())) {
            String city = editTextCity.getText().toString();
            String queryFromAddresses = "SELECT DISTINCT idPlace FROM addresses WHERE city = '" + city + "';";

            SimpleSQLiteQuery queryAddress = new SimpleSQLiteQuery(queryFromAddresses);

            viewModel.getPlaceIdForGivenParamFromAddresses(queryAddress).observe(this, new Observer<List<Long>>() {
                @Override
                public void onChanged(List<Long> placesId) {
                    System.out.println("places = " + placesId.size());
                    placeIds.addAll(placesId);
                    saveArrayOfIds(placesId);
                }
            });
            launchResultsActivity();
        }
    }

    private void saveArrayOfIds(List<Long> ids) {
        Gson gson = new Gson();
        preferences.edit().putString(PLACE_IDS, gson.toJson(ids)).apply();
    }

    private void launchResultsActivity() {
        Intent resultsActivity = new Intent(this, ResultsFromSearchActivity.class);
        startActivity(resultsActivity);
    }
}
