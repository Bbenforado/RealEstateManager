package com.example.realestatemanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.realestatemanager.converters.DateTypeConverter;
import com.example.realestatemanager.database.dao.AddressDao;
import com.example.realestatemanager.database.dao.InterestDao;
import com.example.realestatemanager.database.dao.PhotoDao;
import com.example.realestatemanager.database.dao.PlaceDao;
import com.example.realestatemanager.database.dao.RawDao;
import com.example.realestatemanager.models.Address;
import com.example.realestatemanager.models.Interest;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;

@Database(entities = {Address.class, Interest.class, Place.class, Photo.class}, version = 1,
exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    //SINGLETON
    private static volatile RealEstateManagerDatabase INSTANCE;

    //DAO
    public abstract PlaceDao placeDao();
    public abstract AddressDao addressDao();
    public abstract InterestDao interestDao();
    public abstract PhotoDao photoDao();
    public abstract RawDao rawDao();

    //INSTANCE
    public static RealEstateManagerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE== null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    RealEstateManagerDatabase.class, "MyDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
