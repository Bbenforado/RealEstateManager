package com.example.realestatemanager.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFormActivity extends AppCompatActivity {


    //----------------------------------
    //BIND VIEWS
    //------------------------------------
    @BindView(R.id.material_type_of_place_button) MaterialButton typeOfPlaceButton;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_add_pictures) MaterialButton addPicturesButton;
    @BindView(R.id.spinner_button_sale_date) Button saleDateButton;
    @BindView(R.id.text_edit_price) TextInputEditText editTextPrice;
    @BindView(R.id.text_edit_surface) TextInputEditText editTextSurface;
    @BindView(R.id.text_edit_nbr_of_rooms) TextInputEditText editTextNbrOfRooms;
    @BindView(R.id.text_edit_nbr_of_bathrooms) TextInputEditText editTextNbrOfBathrooms;
    @BindView(R.id.text_edit_nbr_of_bedrooms) TextInputEditText editTextNbrOfBedrooms;
    @BindView(R.id.text_edit_description) TextInputEditText editTextDescription;
    @BindView(R.id.text_edit_street_nbr) TextInputEditText editTextStreetNbr;
    @BindView(R.id.edit_text_street_name) TextInputEditText editTextStreetName;
    @BindView(R.id.edit_text_complement) TextInputEditText editTextComplement;
    @BindView(R.id.text_edit_postal_code) TextInputEditText editTextPostalCode;
    @BindView(R.id.text_edit_city) TextInputEditText editTextCity;
    @BindView(R.id.text_edit_country) TextInputEditText editTextCountry;
    @BindView(R.id.swith_button_available) Switch switchButtonAvailable;
    @BindView(R.id.text_edit_author) TextInputEditText editTextAuthor;
    @BindView(R.id.checkboxSchool) CheckBox checkBoxSchool;
    @BindView(R.id.checkboxMarketPlace) CheckBox checkBoxMarketPlace;
    @BindView(R.id.checkboxPark) CheckBox checkBoxPark;
    @BindView(R.id.checkboxHospital) CheckBox checkBoxHospital;
    @BindView(R.id.checkboxCinema) CheckBox checkBoxCinema;
    @BindView(R.id.checkboxTheater) CheckBox checkBoxTheater;
    //---------------------------------------------------------------------------------------
    private String[] typesOfPlace = {"Loft", "Mansion", "Penthouse", "Duplex"};
    private String type;
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String SWITCH_BUTTON_MODE = "switchButtonMode";
    public static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    private final int NOTIFICATION_ID = 100;
    private final String NOTIFICATION_TAG = "realEstateManager";
    private static final String PLACE_ID = "placeId";
    private PlaceViewModel placeViewModel;
    private int status;
    private long placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        configureToolbar();
        configureViewModel();
        setSwitchListener();

        status = preferences.getInt(STATUS_FORM_ACTIVITY, -1);
        System.out.println("status = " + status);
        //if it s to edit one existing place
        if (status == 1) {
            placeId = preferences.getLong(PLACE_ID, -1);
            System.out.println("id = " + placeId);
            //update ui
            placeViewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    completeFormWithData(place);
                }
            });
            placeViewModel.getAddress(placeId).observe(this, new Observer<Address>() {
                @Override
                public void onChanged(Address address) {
                    completeAddressFormWithData(address);
                }
            });
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_add_place_activity, menu);
        return true;
    }

    /**
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_save_place:
                //if it s to save a new place
                if (status != 1) {
                    if (paramsAreOk()) {
                        //create place
                        long id = createPlace();
                        System.out.println("id = " + id);
                        CheckBox[] checkBoxes = {checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater};
                        //create interest
                        for (CheckBox checkBox : checkBoxes) {
                            if (checkBox.isChecked()) {
                                Interest interest = new Interest(checkBox.getText().toString(), id);
                                placeViewModel.createInterest(interest);
                            }
                        }
                        //create address
                        createAddress(id);
                        sendNotification("Place created");
                        launchMainActivity();
                    } else {
                        Toast.makeText(this, "You have to enter at least a price, a type of place, an address and the real estate manager in charge of this place!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    //if it s to update an existing place
                    //get existing place
                    placeViewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                        @Override
                        public void onChanged(Place place) {
                            updatePlace(place);

                        }
                    });
                    placeViewModel.getAddress(placeId).observe(this, new Observer<Address>() {
                        @Override
                        public void onChanged(Address address) {
                            updateAddress(address);
                        }
                    });
                    sendNotification("Place updated");
                    //return to main activity
                    launchMainActivity();

                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    //-----------------------------------
    //CONFIGURATION
    //--------------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a new place");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        placeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }
    //-------------------------------------
    //ACTIONS
    //----------------------------------
    @OnClick(R.id.material_type_of_place_button)
    public void chooseATypeOfPlace() {
        displayDialog();
    }

    @OnClick(R.id.button_add_pictures)
    public void addPictures() {
        String[] wayToGetPicture = {"Pick from gallery", "Take from camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(wayToGetPicture, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String string = "You clicked on " + wayToGetPicture[which];
                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.spinner_button_sale_date)
    public void selectSaleDate(View view) {
        createDatePickerDialog(view);
    }

    //----------------------------------------------
    //UPDATE UI
    //-----------------------------------------------
    private void completeFormWithData(Place place) {
        typeOfPlaceButton.setText(place.getType());
        editTextPrice.setText(String.valueOf(place.getPrice()));
        editTextAuthor.setText(place.getAuthor());

        if (place.getDescription() != null) {
            editTextDescription.setText(place.getDescription());
        } else {
            editTextDescription.setText("Not informed yet");
        }
        if (place.getSurface() != 0) {
            editTextSurface.setText(String.valueOf(place.getSurface()));
        }else {
            editTextSurface.setText("Not informed yet");
        }
        if (place.getNbrOfRooms() != 0) {
            editTextNbrOfRooms.setText(String.valueOf(place.getNbrOfRooms()));
        } else {
            editTextNbrOfRooms.setText("Not informed yet");
        }
        if (place.getNbrOfBathrooms() != 0) {
            editTextNbrOfBathrooms.setText(String.valueOf(place.getNbrOfBathrooms()));
        } else {
            editTextNbrOfBathrooms.setText("Not informed yet");
        }
        if (place.getNbrOfBedrooms() != 0) {
            editTextNbrOfBedrooms.setText(String.valueOf(place.getNbrOfBedrooms()));
        } else {
            editTextNbrOfBedrooms.setText("Not informed yet");
        }


    }

    private void completeAddressFormWithData(Address address) {
        editTextStreetNbr.setText(String.valueOf(address.getStreetNumber()));
        editTextStreetName.setText(address.getStreetName());
        editTextPostalCode.setText(String.valueOf(address.getPostalCode()));
        editTextCity.setText(address.getCity());
        editTextCountry.setText(address.getCountry());
        if (address.getComplement() != null) {
            editTextComplement.setText(address.getComplement());
        } else {
            editTextComplement.setText("Not informed");
        }

    }

    //--------------------------------------------
    //METHODS
    //---------------------------------------------
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, AddFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Notification sent");
        inboxStyle.addLine(message);

        String channelId = "fcm_default_channel";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.house)
                        .setContentTitle("Real estate manager")
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de mon appli";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a type of place: ");

        builder.setItems(typesOfPlace, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = typesOfPlace[which];
                typeOfPlaceButton.setText(type);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createDatePickerDialog(final View v) {
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
                //check if selected date is passed or not
                if (year< currentYear) {
                } else if(year == currentYear) {
                    if ((month+1) < currentMonth) {
                        Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                        saleDateButton.setText(dayOfMonth + "/" + newMonth + "/" + year);
                    } else if ((month+1) == currentMonth) {
                        if (dayOfMonth <= currentDay) {
                            saleDateButton.setText(dayOfMonth + "/" + newMonth + "/" + year);
                        } else {
                            Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setSwitchListener() {
        switchButtonAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putInt(SWITCH_BUTTON_MODE, 1).apply();
                } else {
                    preferences.edit().putInt(SWITCH_BUTTON_MODE, 0).apply();
                }
            }
        });
    }

    private boolean paramsAreOk() {
        return !TextUtils.isEmpty(editTextPrice.getText().toString()) && typeOfPlaceButton.getText().toString() != "Type of place" &&
                !TextUtils.isEmpty(editTextAuthor.getText().toString()) && !TextUtils.isEmpty(editTextStreetNbr.getText().toString()) &&
                !TextUtils.isEmpty(editTextStreetName.getText().toString()) && !TextUtils.isEmpty(editTextPostalCode.getText().toString()) &&
                !TextUtils.isEmpty(editTextCity.getText().toString()) && !TextUtils.isEmpty(editTextCountry.getText().toString());
    }

    //-------------------------------------------------------------------------
    //CREATE IN DATABASE

    private long createPlace() {
        //FOR PLACE
        long surface = 0;
        int nbrOfRooms = 0;
        int nbrOfBathrooms = 0;
        int nbrOfBedrooms= 0;
        String description = null;
        String type = typeOfPlaceButton.getText().toString();
        long price = Long.parseLong(editTextPrice.getText().toString());
        if (!TextUtils.isEmpty(editTextSurface.getText().toString())) {
            surface = Long.parseLong(editTextSurface.getText().toString());
        }
        if (!TextUtils.isEmpty(editTextNbrOfRooms.getText().toString())) {
            nbrOfRooms = Integer.parseInt(editTextNbrOfRooms.getText().toString());
        }
        if (!TextUtils.isEmpty(editTextNbrOfBathrooms.getText().toString())) {
            nbrOfBathrooms = Integer.parseInt(editTextNbrOfBathrooms.getText().toString());
        }
        if (!TextUtils.isEmpty(editTextNbrOfBedrooms.getText().toString())) {
            nbrOfBedrooms = Integer.parseInt(editTextNbrOfBedrooms.getText().toString());
        }
        if (!TextUtils.isEmpty(editTextDescription.getText().toString())) {
            description = editTextDescription.getText().toString();
        }
        int status = preferences.getInt(SWITCH_BUTTON_MODE, -1);
        String author = editTextAuthor.getText().toString();
        String date = new Date().toString();

        Place place = new Place(nbrOfRooms, nbrOfBathrooms, nbrOfBedrooms, type, price, status, date, author, description, surface);

        return placeViewModel.createPlace(place);
    }

    private void updatePlace(Place place) {
        //FOR PLACE
        long surface;
        int nbrOfRooms;
        int nbrOfBathrooms;
        int nbrOfBedrooms;
        String description;
        String type = typeOfPlaceButton.getText().toString();
        place.setType(type);
        long price = Long.parseLong(editTextPrice.getText().toString());
        place.setPrice(price);

        if (!TextUtils.isEmpty(editTextSurface.getText().toString())) {
            surface = Long.parseLong(editTextSurface.getText().toString());
            place.setSurface(surface);
        }
        if (!TextUtils.isEmpty(editTextNbrOfRooms.getText().toString())) {
            nbrOfRooms = Integer.parseInt(editTextNbrOfRooms.getText().toString());
            place.setNbrOfRooms(nbrOfRooms);
        }
        if (!TextUtils.isEmpty(editTextNbrOfBathrooms.getText().toString())) {
            nbrOfBathrooms = Integer.parseInt(editTextNbrOfBathrooms.getText().toString());
            place.setNbrOfBathrooms(nbrOfBathrooms);
        }
        if (!TextUtils.isEmpty(editTextNbrOfBedrooms.getText().toString())) {
            nbrOfBedrooms = Integer.parseInt(editTextNbrOfBedrooms.getText().toString());
            place.setNbrOfBedrooms(nbrOfBedrooms);
        }
        if (!TextUtils.isEmpty(editTextDescription.getText().toString())) {
            description = editTextDescription.getText().toString();
            place.setDescription(description);
        }
        int status = preferences.getInt(SWITCH_BUTTON_MODE, -1);
        place.setStatus(status);
        String author = editTextAuthor.getText().toString();
        place.setAuthor(author);
        String date = new Date().toString();
        place.setCreationDate(date);
        placeViewModel.updatePlace(place);
    }

    private void createAddress(long placeId) {
        //FOR ADDRESS
        String complement = null;
        int streetNumber = Integer.parseInt(editTextStreetNbr.getText().toString());
        String streetName = editTextStreetName.getText().toString();
        if (!TextUtils.isEmpty(editTextComplement.getText().toString())) {
            complement = editTextComplement.getText().toString();
        }
        String postalCode = editTextPostalCode.getText().toString();
        String city = editTextCity.getText().toString();
        String country = editTextCountry.getText().toString();

        Address address = new Address(placeId, streetNumber, streetName, complement, postalCode, city, country);
        placeViewModel.createAddress(address);
    }

    private void updateAddress(Address address) {
        String complement;
        int streetNumber = Integer.parseInt(editTextStreetNbr.getText().toString());
        address.setStreetNumber(streetNumber);
        String streetName = editTextStreetName.getText().toString();
        address.setStreetName(streetName);
        if (!TextUtils.isEmpty(editTextComplement.getText().toString())) {
            complement = editTextComplement.getText().toString();
            address.setComplement(complement);
        }
        String postalCode = editTextPostalCode.getText().toString();
        address.setPostalCode(postalCode);
        String city = editTextCity.getText().toString();
        address.setCity(city);
        String country = editTextCountry.getText().toString();
        address.setCountry(country);

        placeViewModel.updateAddress(address);
    }

    //----------------------------------------------------
    //METHODS
    //----------------------------------------------------


}
