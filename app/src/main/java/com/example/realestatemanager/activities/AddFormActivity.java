package com.example.realestatemanager.activities;

import android.Manifest;
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
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.adapters.PhotoRecyclerViewAdapter;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.utils.ItemClickSupport;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

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
    @BindView(R.id.text_edit_author) TextInputEditText editTextAuthor;
    @BindView(R.id.checkboxSchool) CheckBox checkBoxSchool;
    @BindView(R.id.checkboxMarketPlace) CheckBox checkBoxMarketPlace;
    @BindView(R.id.checkboxPark) CheckBox checkBoxPark;
    @BindView(R.id.checkboxHospital) CheckBox checkBoxHospital;
    @BindView(R.id.checkboxCinema) CheckBox checkBoxCinema;
    @BindView(R.id.checkboxTheater) CheckBox checkBoxTheater;
    @BindView(R.id.recycler_view_photo_add_form) RecyclerView recyclerView;
    //---------------------------------------------------------------------------------------
    private String[] typesOfPlace = {"Loft", "Mansion", "Penthouse", "Duplex"};
    private String type;
    private SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String SWITCH_BUTTON_MODE = "switchButtonMode";
    public static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    public static final String DATE_OF_SALE = "dateOfSale";
    public static final String USER_NAME = "userName";
    private static final String PLACE_ID = "placeId";
    private PlaceViewModel placeViewModel;
    private int status;
    private long placeId;
    private List<CheckBox> checkBoxes;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private List<Photo> photoList;
    private String currentPhotoPath;
    private PhotoRecyclerViewAdapter adapter;
    private List<Photo> photoListForThePlace;
    private String[] longClickFunctionality = {"Delete photo"};
    private List<Photo> allPhotos;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        status = preferences.getInt(STATUS_FORM_ACTIVITY, -1);
        configureToolbar();
        photoList = new ArrayList<>();
        configureOnClickRecyclerViewForAddActivity();
        configureOnClickRecyclerView();
        configureViewModel();
        checkBoxes = Arrays.asList(checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater);

        //if it s to edit one existing place
        if (status == 1) {
            placeId = preferences.getLong(PLACE_ID, -1);

            configureRecyclerView(placeId);
            configureOnClickRecyclerView();
            //update ui
            placeViewModel.getPlace(placeId).observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    completeFormWithData(place);
                }
            });
            //updateUi(allPhotos);
            /*placeViewModel.getPhotosForAPlace(placeId).observe(this, new Observer<List<Photo>>() {
                @Override
                public void onChanged(List<Photo> photos) {
                    updateUi(photos);
                }
            });*/
            placeViewModel.getAddress(placeId).observe(this, new Observer<Address>() {
                @Override
                public void onChanged(Address address) {
                    completeAddressFormWithData(address);
                }
            });
            placeViewModel.getInterests(placeId).observe(this, new Observer<List<Interest>>() {
                @Override
                public void onChanged(List<Interest> interests) {
                    completeInterestFormWithData(interests);
                }
            });
        } else {
            displayRealEstateManagerName();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences.edit().putString(DATE_OF_SALE, null).apply();
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
                        //CheckBox[] checkBoxes = {checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater};
                        //create interest
                        for (CheckBox checkBox : checkBoxes) {
                            if (checkBox.isChecked()) {
                                Interest interest = new Interest(checkBox.getText().toString(), id);
                                placeViewModel.createInterest(interest);
                            }
                        }
                        //create photo
                        for (Photo photo : photoList) {
                            photo.setPlaceId(id);
                            placeViewModel.createPhoto(photo);
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
                    //create photo
                    for (Photo photo : photoList) {
                        photo.setPlaceId(placeId);
                        placeViewModel.createPhoto(photo);
                    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 2 - Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 6 - Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);

        //WHEN TAKE PICTURE WITH CAMERA
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists())            {
                Toast.makeText(this, "file exists",Toast.LENGTH_SHORT).show();
                Photo photo = new Photo(currentPhotoPath);
                photoList.add(photo);
                System.out.println("photo list = " + photoList);
                if (status != 1) {
                    System.out.println("here for camera and add");
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("here for camera and edit");
                    //allPhotos.addAll(photoList);
                    allPhotos.add(photo);
                    adapter.notifyDataSetChanged();
                }

            }
        }
    }

    //-----------------------------------
    //CONFIGURATION
    //--------------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        if (status == 1) {
            getSupportActionBar().setTitle("Edit place");
        } else {
            getSupportActionBar().setTitle("Add a new place");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureRecyclerView(long placeId) {
        //final list of photos
        allPhotos = new ArrayList<>();

        //get the photos already saved for this place
        placeViewModel.getPhotosForAPlace(placeId).observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                photoListForThePlace = photos;
                //add the photos to the final list of photos
                allPhotos.addAll(photoListForThePlace);
            }
        });
        adapter = new PhotoRecyclerViewAdapter(allPhotos, Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void configureOnClickRecyclerViewForAddActivity() {
        adapter = new PhotoRecyclerViewAdapter(photoList, Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.photo_recycler_view_item)
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        Photo photo = adapter.getItem(position);
                        displayLongClickDialog(photo);
                        return false;
                    }
                });
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
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] wayToGetPicture = {"Pick from gallery", "Take from camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(wayToGetPicture, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        chooseImageFromPhone();
                        break;
                    case 1:
                        dispatchTakePictureIntent();
                        break;
                        default:
                            break;
                }
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
    /*private void updateUi(List<Photo> photosOThePlace) {
        //photoListForThePlace.addAll(photos);
        allPhotos.addAll(photosOThePlace);
        adapter.notifyDataSetChanged();
    }*/

    private void displayRealEstateManagerName() {
        if (preferences.getString(USER_NAME, null) != null) {
            String realEstateManagerName = preferences.getString(USER_NAME, null);
            editTextAuthor.setText(realEstateManagerName);
        }
    }

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

    private void completeInterestFormWithData(List<Interest> interests) {
        for (int i = 0; i < interests.size(); i++) {
            for (int j = 0; j< checkBoxes.size(); j++) {
                if (interests.get(i).getType().equals(checkBoxes.get(j).getText().toString())) {
                    checkBoxes.get(j).setChecked(true);
                }
            }
        }
    }

    //--------------------------------------------
    //METHODS
    //---------------------------------------------

    private void displayLongClickDialog(Photo photo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(longClickFunctionality, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placeViewModel.deletePhoto(photo.getId());
                long photoId = photo.getId();

                for (int i = 0; i<allPhotos.size(); i++) {
                    if (allPhotos.get(i).getId() == photoId) {
                        allPhotos.remove(allPhotos.get(i));
                    }
                }
               adapter.notifyDataSetChanged();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //save a file
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        System.out.println("dispatch picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.realestatemanager.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS

                Uri uri = data.getData();
                String path = getRealPathFromURI(uri);
                Photo photo = new Photo(path);
                photoList.add(photo);
                System.out.println("photo list here in gallery = " + photoList);
                if (status != 1) {
                    System.out.println("here for pick gallery and add");
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("here for pick gallery and edit");
                    //allPhotos.addAll(photoList);
                    allPhotos.add(photo);
                    adapter.notifyDataSetChanged();
                }

            } else {
                Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

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

            int NOTIFICATION_ID = 100;
            String NOTIFICATION_TAG = "realEstateManager";
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
                        preferences.edit().putString(DATE_OF_SALE, dayOfMonth +"/"+ newMonth +  "/" + year).apply();
                    } else if ((month+1) == currentMonth) {
                        if (dayOfMonth <= currentDay) {
                            saleDateButton.setText(dayOfMonth + "/" + newMonth + "/" + year);
                            preferences.edit().putString(DATE_OF_SALE, dayOfMonth +"/"+ newMonth +  "/" + year).apply();
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

    private boolean paramsAreOk() {
        return !TextUtils.isEmpty(editTextPrice.getText().toString()) && typeOfPlaceButton.getText().toString() != "Type of place" &&
                !TextUtils.isEmpty(editTextAuthor.getText().toString()) && !TextUtils.isEmpty(editTextStreetNbr.getText().toString()) &&
                !TextUtils.isEmpty(editTextStreetName.getText().toString()) && !TextUtils.isEmpty(editTextPostalCode.getText().toString()) &&
                !TextUtils.isEmpty(editTextCity.getText().toString()) && !TextUtils.isEmpty(editTextCountry.getText().toString());
    }

    //-------------------------------------------------------------------------
    //CREATE IN DATABASE
    //-------------------------------------------------------------------------

    private long createPlace() {
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
        //int status = preferences.getInt(SWITCH_BUTTON_MODE, -1);
        String author = editTextAuthor.getText().toString();
        String date = new Date().toString();
        Place place;
        if (TextUtils.isEmpty(saleDateButton.getText())) {
            //is available
            place = new Place(nbrOfRooms, nbrOfBathrooms, nbrOfBedrooms, type, price, date, null, author, description, surface);
        } else {
            //if sold
            String saleDate = preferences.getString(DATE_OF_SALE, null);
            place = new Place(nbrOfRooms, nbrOfBathrooms, nbrOfBedrooms, type, price, date, saleDate, author, description, surface);
        }
        return placeViewModel.createPlace(place);
    }

    private void updatePlace(Place place) {
        long surface;
        int nbrOfRooms;
        int nbrOfBathrooms;
        int nbrOfBedrooms;
        String description;
        String dateOfSale;
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
        /*int status = preferences.getInt(SWITCH_BUTTON_MODE, -1);
        place.setStatus(status);*/

        if (preferences.getString(DATE_OF_SALE, null) != null) {
            place.setDateOfSale(preferences.getString(DATE_OF_SALE, null));
        }

        String author = editTextAuthor.getText().toString();
        place.setAuthor(author);
        /*String date = new Date().toString();
        place.setCreationDate(date);*/
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

    /*private void updateInterests(long placeId) {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                if (placeViewModel.getInterest(placeId, ))
                Interest interest = new Interest(checkBox.getText().toString(), placeId);
                placeViewModel.updateInterest(interest);
            }
        }
    }*/

    //----------------------------------------------------
    //METHODS
    //----------------------------------------------------


}
