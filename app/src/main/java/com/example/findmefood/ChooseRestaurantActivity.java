package com.example.findmefood;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.findmefood.models.Restaurant;
import com.example.findmefood.models.SearchRestaurantsResults;

import java.util.ArrayList;
import java.util.List;

public class ChooseRestaurantActivity extends FragmentActivity {
    private static final String TAG = ChooseRestaurantActivity.class.getName();
    FoodPageAdapter foodPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_restaurant);
        Intent i = getIntent();
        SearchRestaurantsResults restaurantsResults = (SearchRestaurantsResults)i.getSerializableExtra("restaurantSearchResult");
        Log.d(TAG,"Search Restaurant results:" + restaurantsResults.toString());
        List<Restaurant> restaurants = restaurantsResults.getBusinesses();
        List<Fragment> fragments = getFragments(restaurants);
        foodPageAdapter = new FoodPageAdapter(getSupportFragmentManager(),fragments);
        ViewPager pager = findViewById(R.id.ff_viewpager);
        pager.setAdapter(foodPageAdapter);

    }

    private List<Fragment> getFragments(List<Restaurant> restaurantList){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (Restaurant restaurant:restaurantList){
            fragmentList.add(ChooseRestaurantFragment.newInstance(restaurant));
        }

        return fragmentList;
    }
}
