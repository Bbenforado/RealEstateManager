package com.example.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realestatemanager.database.RealEstateManagerDatabase;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.google.android.gms.dynamic.IFragmentWrapper;
/*
public class PlaceContentProvider extends ContentProvider {


    public static final String AUTHORITY = "com.example.realestatemanager.provider";
    public static final String TABLE_PLACE = Place.class.getSimpleName();
    public static final String TABLE_ADDRESS = Address.class.getSimpleName();
    public static final String TABLE_PHOTO = Photo.class.getSimpleName();
    public static final String TABLE_INTEREST = Interest.class.getSimpleName();
    public static final Uri URI_PLACE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PLACE);
    public static final String PATH = "content://" + AUTHORITY + "/" + TABLE_PLACE;
    public static final Uri URI_ADDRESS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ADDRESS);
    public static final Uri URI_PHOTO = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PHOTO);
    public static final Uri URI_INTEREST = Uri.parse("content://" + AUTHORITY + "/" + TABLE_INTEREST);
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, URI_PLACE.getPath(), 1);
        uriMatcher.addURI(AUTHORITY, TABLE_ADDRESS, 2);
        uriMatcher.addURI(AUTHORITY, TABLE_PHOTO, 3);
        uriMatcher.addURI(AUTHORITY, TABLE_INTEREST, 4);
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {
            System.out.println("uri = " + uri.toString());
            System.out.println("uri matcher = " + uriMatcher.toString());
            switch (uriMatcher.match(uri)) {
                //FOR TABLE PLACE
                case 1:
                    long idPlace = ContentUris.parseId(uri);
                    final Cursor cursorPlace =
                            RealEstateManagerDatabase.getInstance(getContext()).placeDao().getPlacesWithCursor(idPlace);
                    cursorPlace.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorPlace;
                //FOR ADDRESS TABLE
                case 2:
                    long idAddress = ContentUris.parseId(uri);
                    final Cursor cursorAddress =
                            RealEstateManagerDatabase.getInstance(getContext()).addressDao().getAddressWithCursor(idAddress);
                    cursorAddress.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorAddress;
                //FOR PHOTO TABLE
                case 3:
                    long idPhoto = ContentUris.parseId(uri);
                    final Cursor cursorPhoto =
                            RealEstateManagerDatabase.getInstance(getContext()).photoDao().getPhotosWithCursor(idPhoto);
                    cursorPhoto.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorPhoto;
                //FOR INTEREST TABLE
                case 4:
                    long idInterest = ContentUris.parseId(uri);
                    final Cursor cursorInterest =
                            RealEstateManagerDatabase.getInstance(getContext()).interestDao().getInterestsWithCursor(idInterest);
                    cursorInterest.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorInterest;
                default:
                    throw new IllegalArgumentException("failed");
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.place/" + AUTHORITY + "." + TABLE_PLACE;
            case 2:
                return "vnd.android.cursor.address/" + AUTHORITY + "." + TABLE_ADDRESS;
            case 3:
                return "vnd.android.cursor.photo/" + AUTHORITY + "." + TABLE_PHOTO;
            case 4:
                return "vnd.android.cursor.interest/" + AUTHORITY + "." + TABLE_INTEREST;
                default:
                    throw new IllegalArgumentException("failed");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (getContext() != null) {
            switch (uriMatcher.match(uri)) {
                case 1:
                    final long idPlace =
                            RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                            .insertPlace(Place.fromContentValues(values));
                    if (idPlace != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idPlace);
                    }
                case 2:
                    final long idAddress =
                            RealEstateManagerDatabase.getInstance(getContext()).addressDao()
                            .insertAddress(Address.fromContentValues(values));
                    if (idAddress != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idAddress);
                    }
                case 3:
                    final long idPhoto =
                            RealEstateManagerDatabase.getInstance(getContext()).photoDao()
                            .insertPhoto(Photo.fromContentValues(values));
                    if (idPhoto != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idPhoto);
                    }
                case 4:
                    final long idInterest =
                            RealEstateManagerDatabase.getInstance(getContext()).interestDao()
                            .insertInterest(Interest.fromContentValues(values));
                    if (idInterest != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idInterest);
                    }
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            switch (uriMatcher.match(uri)) {
                case 1:
                    final int countPlace =
                            RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                                    .deletePlace(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countPlace;
                case 2:
                    final int countAddress =
                            RealEstateManagerDatabase.getInstance(getContext()).addressDao()
                                    .deleteAddress(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countAddress;
                case 3:
                    final int countPhoto =
                            RealEstateManagerDatabase.getInstance(getContext()).photoDao()
                                    .deletePhoto(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countPhoto;
                case 4:
                    final int countInterest =
                            RealEstateManagerDatabase.getInstance(getContext()).interestDao()
                                    .deleteInterests(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countInterest;
            }
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            switch (uriMatcher.match(uri)) {
                case 1:
                    final int countPlace =
                            RealEstateManagerDatabase.getInstance(getContext()).placeDao()
                                    .updatePlace(Place.fromContentValues(values));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countPlace;
                case 2:
                    final int countAddress =
                            RealEstateManagerDatabase.getInstance(getContext()).addressDao()
                                    .updateAddress(Address.fromContentValues(values));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countAddress;
                case 3:
                    final int countPhoto =
                            RealEstateManagerDatabase.getInstance(getContext()).photoDao()
                                    .updatePhoto(Photo.fromContentValues(values));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countPhoto;
                case 4:
                    final int countInterest =
                            RealEstateManagerDatabase.getInstance(getContext()).interestDao()
                                    .updateInterest(Interest.fromContentValues(values));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countInterest;
            }
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}*/
