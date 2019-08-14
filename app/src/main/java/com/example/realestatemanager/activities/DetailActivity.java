package com.example.realestatemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";
    private static final String APP_PREFERENCES = "appPreferences";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        DetailFragment detailFragment = new DetailFragment();
        showFragment(detailFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout_detail, fragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getIntent().getExtras().getInt("result", -1) == 1) {
            preferences.edit().putInt(KEY_RESULTS_ACTIVITY, 1).apply();
        }
    }
}
