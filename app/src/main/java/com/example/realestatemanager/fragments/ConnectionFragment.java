package com.example.realestatemanager.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestatemanager.R;
import com.facebook.stetho.Stetho;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {


    @BindView(R.id.material_button_save_user)
    MaterialButton buttonSaveUser;
    @BindView(R.id.user_first_name_text_edit)
    TextInputEditText firstNameEditText;
    @BindView(R.id.user_last_name_text_edit) TextInputEditText lastNameEditText;

    SharedPreferences preferences;
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String USER_NAME = "userName";

    public ConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_connection, container, false);
    }

    @OnClick(R.id.material_button_save_user)
    public void launchActivity() {
    }

}
