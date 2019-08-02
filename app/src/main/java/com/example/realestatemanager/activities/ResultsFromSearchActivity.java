package com.example.realestatemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.ListFragment;


public class ResultsFromSearchActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "appPreferences";
    public static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_from_search);
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ListFragment listFragment = new ListFragment();
        showFragment(listFragment);
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, 1).apply();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout_results_from_search_list, fragment)
                .commit();
    }

}
