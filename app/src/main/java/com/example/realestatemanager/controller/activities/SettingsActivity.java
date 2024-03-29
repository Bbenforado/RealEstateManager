package com.example.realestatemanager.controller.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.realestatemanager.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.user_name_text_view) TextView userNameTextView;
    @BindView(R.id.material_change_user_name_button) MaterialButton changeUserNameButton;
    //-------------------------------------------------------------
    private SharedPreferences preferences;
    //-------------------------------------------------------------
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String USER_NAME = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        configureToolbar();
        if (preferences.getString(USER_NAME, null) != null) {
            displayInfo();
        } else {
            displayDialogAddYourName();
        }
    }

    //----------------------------------------
    //ACTIONS
    //-----------------------------------------
    @OnClick(R.id.material_change_user_name_button)
    public void changeUserName() {
        displayChangeUserNameDialog();
    }

    //-------------------------------------
    //CONFIGURATION
    //---------------------------------
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //------------------------------------------
    private void displayInfo() {
        userNameTextView.setText(preferences.getString(USER_NAME, null));
    }

    private void displayChangeUserNameDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_change_user_name, null);
        final TextInputEditText lastName = dialogLayout.findViewById(R.id.text_edit_dialog_last_name);
        TextInputEditText firstName = dialogLayout.findViewById(R.id.text_edit_dialog_first_name);

        dialog.setMessage(getString(R.string.dialog_title_enter_new_name))
                .setView(dialogLayout)
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userLastName = lastName.getText().toString();
                        String userFirstName = firstName.getText().toString();
                        preferences.edit().putString(USER_NAME, userFirstName + " " + userLastName).apply();
                        displayInfo();
                    }})
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void displayDialogAddYourName() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_save_your_name)
                .setMessage(R.string.dialog_message_save_your_name)
                .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
