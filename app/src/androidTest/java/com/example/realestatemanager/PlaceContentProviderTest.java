package com.example.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static com.example.realestatemanager.provider.PlaceContentProvider.AUTHORITY;
import static com.example.realestatemanager.provider.PlaceContentProvider.TABLE_ADDRESS;
import static com.example.realestatemanager.provider.PlaceContentProvider.TABLE_INTEREST;
import static com.example.realestatemanager.provider.PlaceContentProvider.TABLE_PHOTO;
import static com.example.realestatemanager.provider.PlaceContentProvider.TABLE_PLACE;
import static com.example.realestatemanager.provider.PlaceContentProvider.URI_ADDRESS;
import static com.example.realestatemanager.provider.PlaceContentProvider.URI_INTEREST;
import static com.example.realestatemanager.provider.PlaceContentProvider.URI_PHOTO;
import static com.example.realestatemanager.provider.PlaceContentProvider.URI_PLACE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.example.realestatemanager.controller.activities.MainActivity;
import com.example.realestatemanager.database.RealEstateManagerDatabase;
import com.example.realestatemanager.provider.PlaceContentProvider;
import com.example.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class PlaceContentProviderTest {

    private ContentResolver contentResolver;
   private static final Uri URI_PLACE_UPDATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PLACE + "/1");
    private static final Uri URI_ADDRESS_UPDATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ADDRESS + "/1");
    private static final Uri URI_PHOTO_UPDATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PHOTO + "/1");
    private static final Uri URI_INTEREST_UPDATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_INTEREST + "/1");
    private static long id = 1;
    public static final String ID_PLACE_FOR_CONTENT_PROVIDER = "idPlaceContentProvider";
    public static final String APP_PREFERENCES = "appPreferences";
    private SharedPreferences preferences;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    //---------------------------------------
    //INSERT DATA
    //------------------------------------------
    @Test
    public void insertAndGetPlaceAddressInterest() {
        //insert address
        contentResolver.insert(PlaceContentProvider.URI_ADDRESS, generateAddress());
        //insert place
        contentResolver.insert(PlaceContentProvider.URI_PLACE, generatePlace());
        //insert interest
        contentResolver.insert(PlaceContentProvider.URI_INTEREST, generateInterest());
        //insert photo
        String url = "https://www.livingspaces.com/globalassets/images/inspiration/transitional_livingroom_244463_2.jpg";
        String description = "this is description";
        contentResolver.insert(PlaceContentProvider.URI_PHOTO, generatePhoto(url, description));

        //get place
        preferences = activityTestRule.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long idPlace = preferences.getLong(ID_PLACE_FOR_CONTENT_PROVIDER, -1);

        final Cursor cursorPlace = contentResolver.query(ContentUris
                        .withAppendedId(PlaceContentProvider.URI_PLACE, idPlace),
                null, null, null, null);

        assertThat(cursorPlace, notNullValue());
        assertThat(cursorPlace.getCount(), is(1));
        assertThat(cursorPlace.moveToFirst(), is(true));
        assertThat(cursorPlace.getString(cursorPlace.getColumnIndexOrThrow("type")), is("Loft"));
        cursorPlace.close();

        //get address
        final Cursor cursorAddress = contentResolver.query(ContentUris.withAppendedId(PlaceContentProvider.URI_ADDRESS, idPlace),
                null, null, null, null);
        assertThat(cursorAddress, notNullValue());
        assertThat(cursorAddress.getCount(), is(1));
        cursorAddress.close();

        //get interest
        final Cursor cursorInterest = contentResolver.query(ContentUris.withAppendedId(URI_INTEREST, idPlace),
                null, null, null, null);
        assertThat(cursorInterest.getCount(), is(1));
        cursorInterest.close();

        //get photo
        final Cursor cursorPhoto = contentResolver.query(ContentUris.withAppendedId(URI_PHOTO, idPlace),
                null, null, null, null);
        assertThat(cursorPhoto.getCount(), is(1));
        cursorPhoto.close();

    }

    //------------------------------------
    //UPDATE DATA
    //----------------------------------------
    @Test
    public void updatePlace() {
        preferences = activityTestRule.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long idPlace = preferences.getLong(ID_PLACE_FOR_CONTENT_PROVIDER, -1);

        contentResolver.update(URI_PLACE, generateUpdatedPlace(idPlace, idPlace), null, null);

        final Cursor cursorUpdatedPlace = contentResolver.query(ContentUris.withAppendedId(URI_PLACE, idPlace),
                null, null, null, null);
        assertThat(cursorUpdatedPlace, notNullValue());
        assertThat(cursorUpdatedPlace.getCount(), is(1));
        assertThat(cursorUpdatedPlace.getString(cursorUpdatedPlace.getColumnIndex("author")), is("michelle"));
        cursorUpdatedPlace.close();
    }

    @Test
    public void updateAddress() {
        preferences = activityTestRule.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long idPlace = preferences.getLong(ID_PLACE_FOR_CONTENT_PROVIDER, -1);

        contentResolver.update(URI_ADDRESS_UPDATE, generateUpdatedAddress(idPlace), null, null);
        final Cursor cursorUpdatedAddress = contentResolver.query(ContentUris.withAppendedId(URI_ADDRESS, idPlace),
                null, null, null, null);
        assertThat(cursorUpdatedAddress, notNullValue());
        assertThat(cursorUpdatedAddress.getString(cursorUpdatedAddress.getColumnIndex("streetName")), is("rue stephenson"));
        cursorUpdatedAddress.close();
    }

    @Test
    public void updatePhoto() {
        preferences = activityTestRule.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long idPlace = preferences.getLong(ID_PLACE_FOR_CONTENT_PROVIDER, -1);

        String url = "https://ksassets.timeincuk.net/wp/uploads/sites/56/2019/07/Ideal-Home-July-19-Country-bathroom-620x620.jpg";
        String description = "New description";
        contentResolver.update(URI_PHOTO_UPDATE, generateUpdatedPhoto(url, description, idPlace), null, null);
        final Cursor cursorUpdatedPhoto = contentResolver.query(ContentUris.withAppendedId(URI_PHOTO, idPlace),
                null, null, null, null);
        assertThat(cursorUpdatedPhoto, notNullValue());
        assertThat(cursorUpdatedPhoto.getString(cursorUpdatedPhoto.getColumnIndex("descriptionPhoto")), is("New description"));
        cursorUpdatedPhoto.close();
    }

    @Test
    public void updateInterest() {
        preferences = activityTestRule.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long idPlace = preferences.getLong(ID_PLACE_FOR_CONTENT_PROVIDER, -1);

        contentResolver.update(URI_INTEREST_UPDATE, generateInterestForUpdate(idPlace, idPlace), null, null);
        final Cursor cursorUpdatedInterest = contentResolver.query(ContentUris.withAppendedId(URI_INTEREST, idPlace),
                null, null, null, null);
        assertThat(cursorUpdatedInterest, notNullValue());
        assertThat(cursorUpdatedInterest.getString(cursorUpdatedInterest.getColumnIndex("interestType")), is("Park"));
        cursorUpdatedInterest.close();
    }
    //------------------------------------
    //GENERATE DATA OBJECTS METHODS
    //---------------------------------------
    private ContentValues generateAddress(){
        final ContentValues values = new ContentValues();
        values.put("streetNumber", "30");
        values.put("streetName", "rue riquet");
        values.put("complement", "rdc");
        values.put("postalCode", "75019");
        values.put("city", "paris");
        values.put("country", "france");
        values.put("latLng", "48.888732, 2.373055");

        return values;
    }

    private ContentValues generatePlace(){
        final ContentValues values = new ContentValues();
        values.put("type", "Loft");
        values.put("price", "200");
        values.put("surface", "200");
        values.put("nbrOfRooms", "1");
        values.put("nbrOfBedrooms", "2");
        values.put("nbrOfBathrooms", "1");
        values.put("author", "michel");
        values.put("description", "this is description");

        return values;
    }
    private ContentValues generateUpdatedPlace(long id, long idAddress) {
        final ContentValues values = new ContentValues();
        values.put("type", "Mansion");
        values.put("price", "200000");
        values.put("id", id);
        values.put("idAddress", idAddress);
        values.put("surface", "300");
        values.put("nbrOfRooms", "4");
        values.put("nbrOfBedrooms", "2");
        values.put("nbrOfBathrooms", "2");
        values.put("author", "michelle");
        values.put("description", "this is new description");
        return values;
    }

    private ContentValues generateUpdatedAddress(long id) {
        final ContentValues values = new ContentValues();
        values.put("addressId", id);
        values.put("streetNumber", "5");
        values.put("streetName", "rue stephenson");
        values.put("complement", "");
        values.put("postalCode", "75018");
        values.put("city", "paris");
        values.put("country", "france");
        values.put("latLng", "48.885223, 2.356729");
        return values;
    }

    private ContentValues generateInterest(){
        final ContentValues values = new ContentValues();
        values.put("interestType", "Hospital");

        return values;
    }

    private ContentValues generateInterestForUpdate(long id, long idPlace) {
        final ContentValues values = new ContentValues();
        values.put("interestType", "Park");
        values.put("idInterest", id);
        values.put("idPlace", idPlace);
        return values;
    }

    private ContentValues generatePhoto(String url, String description) {
        final ContentValues values = new ContentValues();
        values.put("uri", url);
        values.put("descriptionPhoto", description);
        return values;
    }

    private ContentValues generateUpdatedPhoto(String url, String description, long id) {
        final ContentValues values = new ContentValues();
        values.put("idPhoto", id);
        values.put("uri", url);
        values.put("descriptionPhoto", description);
        values.put("placeId", id);
        return values;
    }
}
