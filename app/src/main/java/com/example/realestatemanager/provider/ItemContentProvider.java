package com.example.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realestatemanager.database.RealEstateManagerDatabase;
import com.example.realestatemanager.models.Place;
import com.google.android.gms.dynamic.IFragmentWrapper;

public class ItemContentProvider extends ContentProvider {


    public static final String AUTHORITY = "com.example.realestatemanager.provider";
    public static final String TABLE_NAME = Place.class.getSimpleName();
    public static final Uri URI_PLACE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {
            long id = ContentUris.parseId(uri);
            final Cursor cursor =
                    RealEstateManagerDatabase.getInstance(getContext()).placeDao().getPlacesWithCursor(id);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
        //return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.place/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (getContext() != null) {
            final long id =
                    RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                    .insertPlace(Place.fromContentValues(values));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count =
                    RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                    .deletePlace(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count =
                    RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                    .updatePlace(Place.fromContentValues(values));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
