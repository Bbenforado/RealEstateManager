package com.example.realestatemanager;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.realestatemanager.activities.MapActivity;
import com.example.realestatemanager.activities.SearchActivity;
import com.example.realestatemanager.activities.SettingsActivity;
import com.example.realestatemanager.fragments.DetailFragment;
import com.example.realestatemanager.fragments.ListFragment;
import com.example.realestatemanager.models.Place;
import com.facebook.stetho.Stetho;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.realestatemanager.utils.Utils.isInternetAvailable;
import static com.example.realestatemanager.utils.Utils.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //-----------------------------------------
    //BIND VIEWS
    //----------------------------------------
    @Nullable
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    //--------------------------------------------
    //
    //-----------------------------------------------
    private ListFragment listFragment;
    private DetailFragment detailFragment;
    private SharedPreferences preferences;
    //-------------------------------------------------
    //
    //----------------------------------------------------
    public static final String APP_PREFERENCES = "appPreferences";
    //public static final String USER_NAME = "userName";
    private static final String PLACE_ID = "placeId";
    public static final String APP_MODE = "appMode";
    public static final String KEY_RESULTS_ACTIVITY = "keyResultActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ButterKnife.bind(this);
        preferences.edit().putLong(PLACE_ID, -1).apply();
        Stetho.initializeWithDefaults(this);

        //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        configureToolbar();
        configureNavigationView();
        configureDrawerLayout();
        configureAndShowListFragment();
        configureAndShowDetailFragment();
        getAppMode();

    }

    @Override
    public void onBackPressed() {
         if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * launchs activity depending on item clicked
     * @param menuItem
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_search:
                Intent searchActivityIntent = new Intent(this, SearchActivity.class);
                startActivity(searchActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void getAppMode() {
        if (findViewById(R.id.frame_layout_main_detail) != null) {
            preferences.edit().putString(APP_MODE, getString(R.string.app_mode_tablet)).apply();
        } else {
            preferences.edit().putString(APP_MODE, getString(R.string.app_mode_phone)).apply();
        }
    }

    /*private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        //this.textViewQuantity.setText(quantity);
        this.textViewQuantity.setText(Integer.toString(quantity));
    }*/

    /**
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.settings:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.map:
                if (isNetworkAvailable(this)) {
                    Intent mapIntent = new Intent(this, MapActivity.class);
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, getString(R.string.toast_message_no_internet_cant_access_to_map), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //----------------------------------------
    //CONFIGURATION
    //-------------------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void configureAndShowListFragment(){
        preferences.edit().putInt(KEY_RESULTS_ACTIVITY, -1).apply();
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main_list);
        if (listFragment == null) {
            listFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main_list, listFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailFragment(){

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main_detail);

        if (detailFragment == null && findViewById(R.id.frame_layout_main_detail) != null) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main_detail, detailFragment)
                    .commit();
        }
    }

    public void refreshFragmentInfo() {
        detailFragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout_main_detail, detailFragment)
                .commit();
    }

}
