package com.example.realestatemanager;

import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.activities.SearchActivity;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Place;
import com.example.realestatemanager.utils.Utils;
import com.example.realestatemanager.utils.UtilsAddFormActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //---------------------------------------------
    //FOR UTILS CLASS
    //---------------------------------------------------
    @Test
    public void addZeroBehindOneNumber() {
        String string = "5";
        string = Utils.addZeroToDate(string);
        assertEquals("05", string);
    }

    @Test
    public void dontAddZeroBehindSeveralNumbers() {
        String str = "52";
        str = Utils.addZeroToDate(str);
        assertNotEquals("052", str);
        assertEquals("52", str);
    }

    @Test
    public void getTodayDateTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String formattedDate = formatter.format(date);
        String currentDate = Utils.getTodayDate();
        assertNotNull(currentDate);
        assertEquals(formattedDate, currentDate);
    }

    @Test
    public void convertEurosToDollarsTest() {
        int euros = 60;
        int dollars = Utils.convertEuroToDollar(euros);
        assertNotNull(dollars);
        assertEquals(66, dollars);
    }

    @Test
    public void convertDollarsToEurosTest() {
        int dollars = 50;
        int euros = Utils.convertDollarToEuro(dollars);
        assertNotNull(euros);
        assertEquals(45, euros);
    }

    @Test
    public void getParenthesisContentTest() {
        String str = "This is (example for test)";
        String newStr = Utils.getParenthesesContent(str);
        assertNotNull(newStr);
        assertEquals("example for test", newStr);
    }

    @Test
    public void getLatLngOfPlaceTest() {
        String latLngStr = "21.02,2.03";
        LatLng latLng = Utils.getLatLngOfPlace(latLngStr);
        assertNotNull(latLng);
        String latLngContent = Utils.getParenthesesContent(latLng.toString());
        assertEquals(latLngStr, latLngContent);
    }

    //-----------------------------------------------
    //ADD PLACE FORM ACTIVITY
    //---------------------------------------------------
    @Test
    public void allPhotosHaveDescriptionTest() {
        List<Photo> photos = new ArrayList<>();
        Photo photo1 = new Photo();
        photo1.setDescriptionPhoto("some description");
        Photo photo2 = new Photo();
        photos.add(photo1);
        photos.add(photo2);
        boolean hasPhotoDescription = UtilsAddFormActivity.allPhotosHaveDescription(photos);
        assertNotNull(hasPhotoDescription);
        assertFalse(hasPhotoDescription);
    }

    //------------------------------------------------
    //SEARCH ACTIVITY
    //-------------------------------------------------


}