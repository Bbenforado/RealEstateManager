package com.example.realestatemanager;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.example.realestatemanager.activities.AddFormActivity;
import com.example.realestatemanager.activities.DetailActivity;
import com.example.realestatemanager.activities.SettingsActivity;
import com.example.realestatemanager.fragments.DetailFragment;
import com.example.realestatemanager.fragments.ListFragment;
import com.facebook.stetho.Stetho;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar) Toolbar toolbar;
    private ListFragment listFragment;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            Stetho.initializeWithDefaults(this);

            //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
            configureToolbar();
            configureAndShowListFragment();
            configureAndShowDetailFragment();
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
            case R.id.menu_add:
                Intent intent = new Intent(this, AddFormActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_edit:
                Intent editIntent = new Intent(this, DetailActivity.class);
                startActivity(editIntent);
                return true;
            case R.id.menu_search:
                return true;
            case R.id.menu_settings:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
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

    private void configureToolbar() {
        setSupportActionBar(toolbar);
    }

    private void configureAndShowListFragment(){
        System.out.println("and here");
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);
        if (listFragment == null) {
            System.out.println("hi");
            listFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, listFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailFragment(){
        System.out.println("and here too?");
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (detailFragment == null && findViewById(R.id.frame_layout_detail) != null) {
            System.out.println("hello");
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail, detailFragment)
                    .commit();
        }
    }
}
