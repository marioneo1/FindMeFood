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

        //Get incoming data.
        Intent i = getIntent();
        SearchRestaurantsResults restaurantsResults = (SearchRestaurantsResults)i.getSerializableExtra("restaurantSearchResult");
        Log.d(TAG,"Search Restaurant results:" + restaurantsResults.toString());
        List<Restaurant> restaurants = restaurantsResults.getBusinesses();

        //Initialize and get a list of fragments, assigning restaurant to each one.
        List<Fragment> fragments = getFragments(restaurants);

        //Add the list of fragments in the adapter.
        foodPageAdapter = new FoodPageAdapter(getSupportFragmentManager(),fragments);

        //Add to the view pager, which will display all the items inside the adapter
        ViewPager pager = findViewById(R.id.ff_viewpager);
        pager.setAdapter(foodPageAdapter);

    }

    private List<Fragment> getFragments(List<Restaurant> restaurantList){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (Restaurant restaurant:restaurantList){
            //Make a fragment, pass the restaurant data to the function inside the fragment
            //return fragment and add to list.
            fragmentList.add(ChooseRestaurantFragment.newInstance(restaurant));
        }

        return fragmentList;
    }
}
