package com.example.realestatemanager.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.realestatemanager.fragments.InformationFragment;
import com.example.realestatemanager.fragments.MapFragment;

import java.nio.charset.Charset;
import java.util.Map;

public class DetailFragmentAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private InformationFragment informationFragment;
    private MapFragment mapFragment;

    public DetailFragmentAdapter(FragmentManager fragmentManager, String[] titles) {
        super(fragmentManager);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //return InformationFragment.newInstance(position);
                return new InformationFragment();
            case 1:
                //return MapFragment.newInstance(position);
                return new MapFragment();
            default:
                return InformationFragment.newInstance(position);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                informationFragment = (InformationFragment) createdFragment;
                break;
            case 1:
                mapFragment = (MapFragment) createdFragment;
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
