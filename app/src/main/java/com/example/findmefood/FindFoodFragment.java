package com.example.findmefood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmefood.models.Restaurant;
import com.example.findmefood.models.SearchRestaurantsResults;
import com.example.findmefood.models.YelpCategory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import okhttp3.Response;


public class FindFoodFragment extends Fragment implements FoodDialogFragment.OnInputSelected{
    private static final String TAG = OkHttpHandler.class.getName();
    private static final int OFFSET_LIMIT = 20;
    Button start_ffood_button;
    TextView mTextView;
    public static int index = 0;
    private static ArrayList<String> restaurant_categories;
    private static ArrayList<String> restaurant_category_titles;
    private static Double lon;
    private static Double lat;
    private static LocationHandler locationHandler;
    private static Gson gson;
    private static LinkedHashSet<String> found_category_titles;
    private static LinkedHashSet<String> found_categories;
    private static int offset_calls;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findfood,container,false);
        start_ffood_button = view.findViewById(R.id.startffood);
        mTextView = view.findViewById(R.id.ffoodtextview);
        gson = new Gson();
        locationHandler = new LocationHandler(getActivity());
        lat = locationHandler.getLat();
        lon = locationHandler.getLon();


        start_ffood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset values back.
                index=0;
                offset_calls=0;
                restaurant_categories = new ArrayList<String>();
                restaurant_category_titles = new ArrayList<String>();
                found_categories = new LinkedHashSet<String>();
                found_category_titles = new LinkedHashSet<String>();
                requestFoodCategory();
            }
        });
        return view;
    }

    private void requestFoodCategory(){
        Log.d(TAG,"FFoodFragment Start OKHTTP");
        Integer offset = (50)*offset_calls++;
        String term = "restaurants";
        lat = locationHandler.getLat();
        lon = locationHandler.getLon();
        System.out.println("Latitude2 " +lat + "Longitude " + lon);

        OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response response) {
                try{
                    /* TODO Fill in the needed details
                     *  Blacklist
                     *  Offset Issue */
                    String body = response.body().string();
                    Log.d(TAG,"Response data: " + body);
                    parseCategoryResponseBody(body);
                    handleCategory();
                }
                catch (IOException e){
                    Log.e(TAG,e.toString());
                }
            }
        });
        okHttpHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, term,lat.toString(),lon.toString(),offset.toString());
    }

    private void handleCategory(){
        if(index < restaurant_categories.size()){
            selectFoodCategory(restaurant_category_titles,restaurant_categories,index);
        }
        else{
            Log.d(TAG,"Offset calls" + offset_calls);
            if(offset_calls >= OFFSET_LIMIT){ //Api Limitation
                Toast.makeText(getActivity(), "End of category. Find Food again?", Toast.LENGTH_SHORT).show();
            }
            else{
                requestFoodCategory();
            }
        }
    }

    private void parseCategoryResponseBody(String body){
        SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
        List<Restaurant> restaurants = searchRestaurants.getBusinesses();
        for(Restaurant restaurant:restaurants){
            //Add categories to a LinkedHashSet to remove dupes and retain order.
            for(YelpCategory category:restaurant.getCategories()){
                found_categories.add(category.getCategoryAlias());
            }

            for(YelpCategory title:restaurant.getCategories()){
                found_category_titles.add(title.getCategoryTitle());
                System.out.println(title.getCategoryTitle());
            }
        }
        restaurant_categories = new ArrayList<>(found_categories);
        restaurant_category_titles = new ArrayList<>(found_category_titles);
        Log.d(TAG,"Restaurant Category size" + restaurant_categories.size());
        Log.d(TAG,"Restaurant Title size" + restaurant_categories.size());
    }

    private void selectFoodCategory(ArrayList<String> rest_titles,ArrayList<String> rest_categories, int i){
        String restaurantTitle = rest_titles.get(i);
        System.out.println("Title " + restaurantTitle);
        String restaurantCategory = rest_categories.get(i);
        callDialogFragment(restaurantTitle,restaurantCategory);
    }

    /*Function to call Dialog Fragment*/
    private void callDialogFragment(String title, String category){
        FragmentManager fm = getFragmentManager();

        /*Create Dialog Fragment*/
        FoodDialogFragment newFragment = new FoodDialogFragment();

        /*Pass value to DialogFragment*/
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("category",category);

        newFragment.setArguments(args);
        newFragment.setTargetFragment(FindFoodFragment.this,1);
        newFragment.show(fm,"FoodDialogFrag");
    }

    /*Interface from DialogFrag*/
    /*If answered yes on Dialog frag, Find closest restaurants of the specific category.*/
    @Override
    public void yes(String input) {
        Log.d(TAG, "yes: Received input " + input);
        mTextView.setText(input);
        String term = input;
        Integer offset = 0;

        OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response response) {
                Log.d(TAG,"ENDPOINT");
                //TODO Make w/ all the needed stuff. Details, Option to navigate,
                try{
                    String body = response.body().string();
//                    TODO make class to contain JSON data like below.
                    Log.d(TAG,"Response data: " + body);
                    SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
                    Intent intent = new Intent(getActivity().getBaseContext(),ChooseRestaurantActivity.class);
                    intent.putExtra("restaurantSearchResult",searchRestaurants);
                    startActivity(intent);

                }
                catch (IOException e){
                    Log.e(TAG,e.toString());
                }

            }
        });
        okHttpHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, term,lat.toString(),lon.toString(),offset.toString());
    }

    /*If answered no on Dialog frag, iterate index and search again.*/
    @Override
    public void no(){
        Log.d(TAG, "no: Received input, Launching dialog again.");

        //If incrementing index results to out of bounds request for more categories.
        if(index < restaurant_categories.size()){
            index++;
            Log.d(TAG,"Index: " + index);
        }
        handleCategory();
    }



}
