package com.example.realestatemanager.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestatemanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    public static final String KEY_POSITION_MAP = "positionMap";

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(int position) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION_MAP, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

}
