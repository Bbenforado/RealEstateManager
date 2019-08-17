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
import android.view.LayoutInflater;
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
import com.example.realestatemanager.adapters.PhotoFormAndTabletModeRecyclerViewAdapter;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.utils.ItemClickSupport;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.utils.UtilsAddFormActivity;
import com.example.realestatemanager.viewModels.PlaceViewModel;
import com.example.realestatemanager.R;
import com.example.realestatemanager.injections.Injection;
import com.example.realestatemanager.injections.ViewModelFactory;
import com.example.realestatemanager.models.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.realestatemanager.utils.UtilsAddFormActivity.allPhotosHaveDescription;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.formatDate;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.getIntFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.getLongFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.getStringFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueIntToEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueLongToEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueTextToEditText;

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
    private String[] longClickFunctionality = {"Delete photo", "Add description to photo"};
    private String type;
    private SharedPreferences preferences;
    private PlaceViewModel placeViewModel;
    private int status;
    private long placeId;
    private List<CheckBox> checkBoxes;
    private List<Photo> photoList;
    private String currentPhotoPath;
    private PhotoFormAndTabletModeRecyclerViewAdapter adapter;
    private List<Photo> allPhotos;
    private List<Long> deletedPhotosId;
    private List<Photo> updatedPhoto;
    @State String buttonTypeText;
    @State String dateSaleText;
    private long addressId;
    //------------------------------------------------------------------------
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String STATUS_FORM_ACTIVITY = "statusFormActivity";
    public static final String DATE_OF_SALE = "dateOfSale";
    public static final String USER_NAME = "userName";
    private static final String PLACE_ID = "placeId";
    public static final String ADDRESS_ID = "addressId";
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final String CODE_DESCRIPTION = "codeDescription";
    public static final String CODE_FORM = "codeForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        Icepick.restoreInstanceState(this, savedInstanceState);
        restoreState();
        saleDateButton.setHint(Utils.getTodayDate());
        status = preferences.getInt(STATUS_FORM_ACTIVITY, -1);
        preferences.edit().putInt(CODE_DESCRIPTION, -1).apply();
        preferences.edit().putString(CODE_FORM, "form").apply();

        configureToolbar();
        initializeArrayLists();
        configureViewModel();
        //if it s to edit one existing place
        if (status == 1) {
            placeId = preferences.getLong(PLACE_ID, -1);
            addressId = preferences.getLong(ADDRESS_ID, -1);
            configureRecyclerView(allPhotos);
            completeFormWithAllData();
        } else {
            //if it s to add one new place
            configureRecyclerView(photoList);
            displayRealEstateManagerName();
        }
        configureOnClickRecyclerView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences.edit().putString(DATE_OF_SALE, null).apply();
        preferences.edit().putString(CODE_FORM, null).apply();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_add_place_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_save_place) {
            //if it s to save a new place
            if (status != 1) {
                if (fieldsAreCorrectlyFilled()) {
                    if (photoList.size() > 0 && allPhotosHaveDescription(photoList)) {
                        createPlaceInterestsPhotosAndAddressInDatabase();
                        sendNotification(this.getString(R.string.place_created));
                        launchMainActivity();
                    } else {
                        Toast.makeText(this, getString(R.string.description_photo_missing), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.field_missing), Toast.LENGTH_LONG).show();
                }
            } else {
                //if it s to update an existing place
                if (fieldsAreCorrectlyFilled()) {
                    if (allPhotos.size() > 0 && allPhotosHaveDescription(allPhotos)) {
                        updateDataInDatabase();
                        sendNotification(this.getString(R.string.place_updated));
                        launchMainActivity();
                    } else {
                        Toast.makeText(this, getString(R.string.description_photo_missing), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.field_missing), Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleResponseForGallery(requestCode, resultCode, data);
        handleResponseForCamera(requestCode, resultCode, data);
    }
    //-----------------------------------
    //CONFIGURATION
    //--------------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        if (status == 1) {
            getSupportActionBar().setTitle(getString(R.string.toolbar_title_edit));
        } else {
            getSupportActionBar().setTitle(getString(R.string.toolbar_title_add));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureRecyclerView(List<Photo> photos) {
        adapter = new PhotoFormAndTabletModeRecyclerViewAdapter(photos, Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.photo_recycler_view_item)
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    Photo photo = adapter.getItem(position);
                    displayLongClickDialog(photo);
                    return false;
                });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        placeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel.class);
    }

    private void initializeArrayLists() {
        photoList = new ArrayList<>();
        allPhotos = new ArrayList<>();
        deletedPhotosId = new ArrayList<>();
        updatedPhoto = new ArrayList<>();
        checkBoxes = Arrays.asList(checkBoxSchool, checkBoxMarketPlace, checkBoxPark, checkBoxHospital, checkBoxCinema, checkBoxTheater);
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
            //Toast.makeText(this, getString(R.string.permissions_granted), Toast.LENGTH_SHORT).show();
            return;
        }
        String[] wayToGetPicture = {getString(R.string.way_to_get_photos_gallery), getString(R.string.way_to_get_photos_camera)};
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
    //UPDATE UI WITH DATA
    //-----------------------------------------------
    private void completeFormWithAllData() {
        placeViewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                completeFormWithPlaceData(place);
            }
        });
        placeViewModel.getAddressOfAPlace(addressId).observe(this, new Observer<Address>() {
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

        placeViewModel.getPhotosForAPlace(placeId).observe(this, photos -> {
            // update UI if it s not called from add description
            if (preferences.getInt(CODE_DESCRIPTION, -1) != 300) {
                allPhotos.addAll(photos);
                adapter.notifyDataSetChanged();
            }
            preferences.edit().putInt(CODE_DESCRIPTION, 0).apply();
        });
    }

    private void restoreState() {
        if (buttonTypeText != null) {
            typeOfPlaceButton.setText(buttonTypeText);
        }
        if (dateSaleText != null) {
            saleDateButton.setText(dateSaleText);
        }
    }
    private void displayRealEstateManagerName() {
        if (preferences.getString(USER_NAME, null) != null) {
            String realEstateManagerName = preferences.getString(USER_NAME, null);
            editTextAuthor.setText(realEstateManagerName);
        }
    }

    private void completeFormWithPlaceData(Place place) {
        typeOfPlaceButton.setText(place.getType());
        editTextPrice.setText(String.valueOf(place.getPrice()));
        editTextAuthor.setText(place.getAuthor());
        setDataValueTextToEditText(place.getDescription(), this, editTextDescription);
        setDataValueLongToEditText(place.getSurface(), this, editTextSurface);
        setDataValueIntToEditText(place.getNbrOfRooms(), this, editTextNbrOfRooms);
        setDataValueIntToEditText(place.getNbrOfBedrooms(), this, editTextNbrOfBedrooms);
        setDataValueIntToEditText(place.getNbrOfBathrooms(), this, editTextNbrOfBathrooms);
        if (place.getDateOfSale() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(place.getDateOfSale());
            saleDateButton.setText(strDate);
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
            editTextComplement.setText(getString(R.string.not_informed));
        }
    }

    private void completeInterestFormWithData(List<Interest> interests) {
        for (int i = 0; i < interests.size(); i++) {
            for (int j = 0; j< checkBoxes.size(); j++) {
                if (interests.get(i).getInterestType().equals(checkBoxes.get(j).getText().toString())) {
                    checkBoxes.get(j).setChecked(true);
                }
            }
        }
    }

    //--------------------------------------------
    //METHODS PHOTOS
    //---------------------------------------------
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, getString(R.string.toast_message_error_occurred), Toast.LENGTH_SHORT).show();
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

    //-------------------------------------------
    //NOTIFICATION
    //--------------------------------------------
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(message);

        String channelId = "fcm_default_channel";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.house)
                        .setContentTitle(getString(R.string.notification_title))
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

    private boolean fieldsAreCorrectlyFilled() {
        return !TextUtils.isEmpty(editTextPrice.getText().toString()) &&
                !typeOfPlaceButton.getText().toString().equals(getString(R.string.button_generic_text_type_of_place)) &&
                !TextUtils.isEmpty(editTextAuthor.getText().toString()) && !TextUtils.isEmpty(editTextStreetNbr.getText().toString()) &&
                !TextUtils.isEmpty(editTextStreetName.getText().toString()) && !TextUtils.isEmpty(editTextPostalCode.getText().toString()) &&
                !TextUtils.isEmpty(editTextCity.getText().toString()) && !TextUtils.isEmpty(editTextCountry.getText().toString());

    }

    //-------------------------------------------------
    //HANDLE RESPONSES FOR ON ACTIVITY RESULT
    //--------------------------------------------------
    private void handleResponseForGallery(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                Uri uri = data.getData();
                String path = getRealPathFromURI(uri);
                Photo photo = new Photo(path);
                photoList.add(photo);
                //ADD ACTIVITY
                if (status != 1) {
                    adapter.notifyDataSetChanged();
                } else {
                    //EDIT
                    allPhotos.add(photo);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, getString(R.string.toast_message_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleResponseForCamera(int requestCode, int resultCode, Intent data) {
        //WHEN TAKE PICTURE WITH CAMERA
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists())            {
                Photo photo = new Photo(currentPhotoPath);
                photoList.add(photo);
                if (status != 1) {
                    adapter.notifyDataSetChanged();
                } else {
                    allPhotos.add(photo);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    //----------------------------------------
    //LAUNCH ACTIVITY
    //-------------------------------------------
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //-----------------------------------------
    //DISPLAY DIALOGS METHODS
    //------------------------------------------------
    /**
     * displays dialog when user click long on the photo
     * if user click long on the photo, he/she can delete or add description to the photo
     * @param photo the clicked photo
     */
    private void displayLongClickDialog(Photo photo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(longClickFunctionality, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        long photoId = photo.getIdPhoto();
                        deletedPhotosId.add(photoId);
                        for (int i = 0; i < allPhotos.size(); i++) {
                            if (allPhotos.get(i).getIdPhoto() == photoId) {
                                allPhotos.remove(allPhotos.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        displayAddDescriptionDialog(photo);
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * displays dialog when click on type of place button
     * user can select the type of the place
     */
    private void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_choose_type_of_place));
        builder.setItems(typesOfPlace, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type = typesOfPlace[which];
                typeOfPlaceButton.setText(type);
                buttonTypeText = type;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * display date picker
     * save selected date
     * @param v the view
     */
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
                String  day = Utils.addZeroToDate(String.valueOf(dayOfMonth));
                String formattedMonth = Utils.addZeroToDate(String.valueOf(newMonth));

                dateSaleText = Utils.checkIfDateIsPassedOrCurrentAndReturnString(getApplicationContext(), day, formattedMonth, year,
                        currentDay, currentMonth, currentYear, saleDateButton);
                preferences.edit().putString(DATE_OF_SALE, dateSaleText).apply();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * displays dialog to add a description for the given photo
     * @param photo the photo where to add the description
     */
    private void displayAddDescriptionDialog(Photo photo) {
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_add_description, null);
        final TextInputEditText description = dialogLayout.findViewById(R.id.text_edit_description_dialog);
        if (photo.getDescriptionPhoto() != null) {
            description.setText(photo.getDescriptionPhoto());
        }
        dialog.setMessage(getString(R.string.title_dialog_add_description))
                .setView(dialogLayout)
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences.edit().putInt(CODE_DESCRIPTION, 300).apply();
                        String descriptionOfPlace = description.getText().toString();
                        //update photo with new description
                        photo.setDescriptionPhoto(descriptionOfPlace);
                        updatedPhoto.add(photo);

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_message_description_added), Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
    //-------------------------------------------------------------------------
    //CREATE IN DATABASE
    //-------------------------------------------------------------------------
    /**
     * save the newly added photos in database
     */
    private void createNewlyAddedPhotos() {
        for (Photo photo : photoList) {
            photo.setPlaceId(placeId);
            placeViewModel.createPhoto(photo);
        }
    }

    /**
     * create interest in database for one place
     * @param id the id of the place which has the interest
     */
    private void createInterestsForAPlace(long id) {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                Interest interest = new Interest(checkBox.getText().toString(), id);
                placeViewModel.createInterest(interest);
            }
        }
    }
    private void createPlaceInterestsPhotosAndAddressInDatabase() {
        long idAddress = createAddress();
        long idPlace = createPlace(idAddress);
        createInterestsForAPlace(idPlace);
        for (Photo photo : photoList) {
            photo.setPlaceId(idPlace);
            placeViewModel.createPhoto(photo);
        }
    }

    /**
     * gte all data that user entered and create a place in database
     * @param idAddress the id of the address of the place
     * @return the id of the created place
     */
    private long createPlace(long idAddress){
        String type = typeOfPlaceButton.getText().toString();
        long price = Long.parseLong(editTextPrice.getText().toString());
        long surface = getIntFromEditText(editTextSurface);
        int nbrOfRooms = getIntFromEditText(editTextNbrOfRooms);
        int nbrOfBathrooms = getIntFromEditText(editTextNbrOfBathrooms);
        int nbrOfBedrooms = getIntFromEditText(editTextNbrOfBedrooms);
        String description = getStringFromEditText(editTextDescription);
        String author = editTextAuthor.getText().toString();
        Date date = new Date();
        Place place;
        if (TextUtils.isEmpty(saleDateButton.getText())) {
            //if available
            place = new Place(nbrOfRooms, nbrOfBathrooms, nbrOfBedrooms, type, price, date, null, author, description, surface, idAddress);
        } else {
            //if sold
            String saleDateStr = preferences.getString(DATE_OF_SALE, null);
            Date saleDate = UtilsAddFormActivity.formatDate(saleDateStr);
            place = new Place(nbrOfRooms, nbrOfBathrooms, nbrOfBedrooms, type, price, date, saleDate, author, description, surface, idAddress);
        }
        return placeViewModel.createPlace(place);
    }

    /**
     * create address in database with data entered by user in form
     * @return the id of the created address
     */
    private long createAddress() {
        String complement = null;
        int streetNumber = Integer.parseInt(editTextStreetNbr.getText().toString());
        String streetName = editTextStreetName.getText().toString();
        if (!TextUtils.isEmpty(editTextComplement.getText().toString())) {
            complement = editTextComplement.getText().toString();
        }
        String postalCode = editTextPostalCode.getText().toString();
        String city = editTextCity.getText().toString();
        String country = editTextCountry.getText().toString();

        Address address = new Address(streetNumber, streetName, complement, postalCode, city, country);
        UtilsAddFormActivity.getAndSetLatLngOfPlace(this,address);
        return placeViewModel.createAddress(address);
    }
    //-----------------------------------------------
    //DELETE
    //-----------------------------------------------
    /**
     * delete the deleted photos in database
     */
    private void deletePhotos() {
        for (Long id : deletedPhotosId) {
            placeViewModel.deletePhoto(id);
        }
    }
    //------------------------------------------------
    //UPDATE
    //--------------------------------------------------
    /**
     * update the updated photos in database
     */
    private void updatePhotos() {
        for (Photo photo : updatedPhoto) {
            placeViewModel.updatePhoto(photo);
        }
    }

    /**
     * update address in database
     * @param address the address to update
     */
    private void updateAddress(Address address) {
        address.setStreetNumber(getIntFromEditText(editTextStreetNbr));
        address.setStreetName(editTextStreetName.getText().toString());
        address.setComplement(getStringFromEditText(editTextComplement));
        address.setPostalCode(editTextPostalCode.getText().toString());
        address.setCity(editTextCity.getText().toString());
        address.setCountry(editTextCountry.getText().toString());
        UtilsAddFormActivity.getAndSetLatLngOfPlace(this, address);
        placeViewModel.updateAddress(address);
    }

    /**
     * update a place in database
     * @param place the place to update
     */
    private void updatePlace(Place place) {
        String type = typeOfPlaceButton.getText().toString();
        place.setType(type);
        place.setPrice(getLongFromEditText(editTextPrice));
        place.setSurface(getLongFromEditText(editTextSurface));
        place.setNbrOfRooms(getIntFromEditText(editTextNbrOfRooms));
        place.setNbrOfBathrooms(getIntFromEditText(editTextNbrOfBathrooms));
        place.setNbrOfBedrooms(getIntFromEditText(editTextNbrOfBedrooms));
        place.setDescription(getStringFromEditText(editTextDescription));

        if (preferences.getString(DATE_OF_SALE, null) != null) {
            place.setDateOfSale(formatDate(preferences.getString(DATE_OF_SALE, null)));
        }

        String author = editTextAuthor.getText().toString();
        place.setAuthor(author);
        placeViewModel.updatePlace(place);
    }

    private void updateDataInDatabase() {
        placeViewModel.getPlace(placeId).observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                updatePlace(place);
            }
        });
        if (photoList.size() > 0) {
            createNewlyAddedPhotos();
        }
        if (deletedPhotosId.size() > 0) {
            deletePhotos();
        }
        if (updatedPhoto.size() > 0) {
            updatePhotos();
        }
        placeViewModel.deleteInterests(placeId);
        createInterestsForAPlace(placeId);
        placeViewModel.getAddressOfAPlace(addressId).observe(this, new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                updateAddress(address);
            }
        });
    }
}
