package com.example.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.realestatemanager.database.RealEstateManagerDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AddressContentProviderTest {
/*
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
    public void getAddressesWhenNoAddressInserted() {
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(
                        AddressContentProvider.URI_ADDRESS, id), null,
                        null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();

    }

    @Test
    public void insertAndGetAddress() {
        final Uri uri = contentResolver.insert(AddressContentProvider.URI_ADDRESS, generateAddress());

        final Cursor cursor = contentResolver.query(ContentUris
                        .withAppendedId(AddressContentProvider.URI_ADDRESS, id),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("streetNumber")), is("30"));

    }

    private ContentValues generateAddress(){
        final ContentValues values = new ContentValues();
        values.put("streetNumber", "30");
        values.put("streetName", "rue riquet");
        values.put("complement", "rdc");
        values.put("postalCode", "75019");
        values.put("city", "paris");
        values.put("country", "france");
        values.put("idPlace", "1");

        return values;
    }*/
}
