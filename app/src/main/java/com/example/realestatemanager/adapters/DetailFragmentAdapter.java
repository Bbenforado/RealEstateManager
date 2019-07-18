package com.example.realestatemanager.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.realestatemanager.fragments.InformationFragment;
import com.example.realestatemanager.fragments.MapFragment;

import java.nio.charset.Charset;

public class DetailFragmentAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public DetailFragmentAdapter(FragmentManager fragmentManager, String[] titles) {
        super(fragmentManager);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InformationFragment.newInstance(position);
            case 1:
                return MapFragment.newInstance(position);
            default:
                return InformationFragment.newInstance(position);
        }
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
