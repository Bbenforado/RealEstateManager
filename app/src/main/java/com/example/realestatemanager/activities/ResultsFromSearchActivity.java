package com.example.realestatemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.realestatemanager.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultsFromSearchActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private SharedPreferences preferences;
    private List<Long> ids;
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String PLACE_IDS = "placeIds";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_from_search);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        //listFragment = new ListFragment();
        //showFragment(listFragment);
        ids = retrievedIds();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout_results_from_search_list, fragment)
                .commit();
    }

    private List<Long> retrievedIds() {
        List<Long> idList;
        Gson gson = new Gson();

        String json = preferences.getString(PLACE_IDS, null);

        Type type = new TypeToken<List<Long>>() {
        }.getType();

        idList = gson.fromJson(json, type);
        return idList;
    }
}
