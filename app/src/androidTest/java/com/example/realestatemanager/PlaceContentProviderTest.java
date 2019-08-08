package com.example.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.example.realestatemanager.database.RealEstateManagerDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/*
@RunWith(AndroidJUnit4.class)
public class PlaceContentProviderTest {

    private ContentResolver contentResolver;
    private static long id = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getPlacesWhenNoPlaceInserted() {
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(
                        PlaceContentProvider.URI_PLACE, id), null,
                        null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();

    }

    @Test
    public void insertAndGetPlace() {
        final Uri uri = contentResolver.insert(PlaceContentProvider.URI_PLACE, generatePlace());

        final Cursor cursor = contentResolver.query(ContentUris
                        .withAppendedId(PlaceContentProvider.URI_PLACE, id),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), is("Loft"));

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
}*/
