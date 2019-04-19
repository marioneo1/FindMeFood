package com.example.findmefood;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FoodPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public FoodPageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int index) {
        return this.fragments.get(index);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
