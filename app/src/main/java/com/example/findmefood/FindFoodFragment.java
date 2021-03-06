package com.example.findmefood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.findmefood.models.Restaurant;
import com.example.findmefood.models.SearchRestaurantsResults;
import com.example.findmefood.models.YelpCategory;
import com.example.findmefood.utility.LocationHandler;
import com.example.findmefood.utility.OkHttpHandler;
import com.example.findmefood.utility.TextToSpeechHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import okhttp3.Response;


public class FindFoodFragment extends Fragment implements FoodDialogFragment.OnInputSelected{
    private static final String TAG = FindFoodFragment.class.getName(), ARRAY_NAME_ENTRIES = "Entries", ARRAY_NAME_VALUES = "Values";
    private static final int OFFSET_LIMIT = 20;
    private static Button start_ffood_button, blacklist_button;
    private static ProgressBar mProgressBar;
    public static int index = 0;
    private static OkHttpHandler okHttpHandler;
    private static ArrayList<String> restaurant_categories, restaurant_category_titles;
    private static Double lon, lat;
    private static LocationHandler locationHandler;
    private static Gson gson;
    private static LinkedHashSet<String> found_category_titles, found_categories;
    private static int offset_calls;
    private static String CATEGORY_SEARCH_FLAG = "0", RESTAURANT_SEARCH_FLAG = "1", FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION, COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String[] LOCATION_PERMISSIONS =
            {FINE_LOCATION, COARSE_LOCATION};
    private static final int REQUEST_CODE = 87;
    private static Context mContext;
    private static int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static LinkedHashSet<String> blacklist = new LinkedHashSet<String>(), blacklist_values = new LinkedHashSet<String>();
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findfood,container,false);
        start_ffood_button = view.findViewById(R.id.startffood);
        mProgressBar = view.findViewById(R.id.ff_progressBar);
        mContext = getContext();
        gson = new Gson();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();
        initializeBlacklist();


        if (ContextCompat.checkSelfPermission(mContext, FINE_LOCATION) != GRANTED){
            Toast.makeText(mContext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), LOCATION_PERMISSIONS, REQUEST_CODE);
        }
        else{
            locationHandler = new LocationHandler(getActivity());
            lat = locationHandler.getLat();
            lon = locationHandler.getLon();
        }

        start_ffood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If internet is available
                if(isConnectedToNetwork(mContext)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    start_ffood_button.setClickable(false);
                    //Reset values back.
                    index=0;
                    offset_calls=0;
                    restaurant_categories = new ArrayList<String>();
                    restaurant_category_titles = new ArrayList<String>();
                    found_categories = new LinkedHashSet<String>();
                    found_category_titles = new LinkedHashSet<String>();
                    //Request call
                    requestFoodCategory();
                }
                else{ //if there's no internet
                    Toast.makeText(getActivity(),"Please ensure that you're connected to an internet source", Toast.LENGTH_LONG).show();
                    Log.d(TAG,"No network connection found");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeBlacklist();
    }

    private void initializeBlacklist(){
        try{
            blacklist = new LinkedHashSet<>();
            blacklist_values = new LinkedHashSet<>();
            JSONArray json_array_blacklist = new JSONArray(prefs.getString(ARRAY_NAME_ENTRIES,"[]"));
            JSONArray json_array_blacklist_values = new JSONArray(prefs.getString(ARRAY_NAME_VALUES,"[]"));
            for (int i = 0; i < json_array_blacklist.length(); i++){
                blacklist.add((String)json_array_blacklist.get(i));
                blacklist_values.add((String)json_array_blacklist_values.get(i));
            }
            if(blacklist.size() > 0){
                Log.d(TAG, "Initialize blacklist with : " + blacklist.toString());
                Log.d(TAG, "Initialize blacklist values with : " + blacklist_values.toString());
            }
        }
        catch (JSONException e){
            Log.d(TAG, e.toString());
        }
    }

    private void clearBlacklist(){
        editor.clear().commit();
    }

    private void addToBlacklist(String title, String category){
        blacklist.add(title);
        blacklist_values.add(category);
        JSONArray json_array_blacklist = new JSONArray(blacklist);
        JSONArray json_array_blacklist_values = new JSONArray(blacklist_values);
        Log.d(TAG, "Json:" + json_array_blacklist.toString());
        editor.putString(ARRAY_NAME_ENTRIES,json_array_blacklist.toString());
        editor.putString(ARRAY_NAME_VALUES,json_array_blacklist_values.toString());
        editor.commit();
    }

    private void requestFoodCategory(){
        Integer offset = (50)*offset_calls++;
        String term = "restaurants";
        lat = locationHandler.getLat();
        lon = locationHandler.getLon();
        System.out.println("Latitude2 " +lat + "Longitude " + lon);

         okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try{
                    /* TODO Blacklist*/
                    if (response != null){
                        String body = response.body().string();
                        Log.d(TAG,"Response data: " + body);
                        if(!body.isEmpty()) {
                            parseCategoryResponseBody(body);
                            handleCategory();
                        }
                    }
                    else{
                        Log.d(TAG,"Null body");
                        Toast.makeText(mContext,"Couldn't find nearby locations, please make sure you're connected to a reliable internet source",Toast.LENGTH_LONG).show();
                        start_ffood_button.setClickable(true);
                    }
                }
                catch (IOException e){
                    Log.e(TAG,e.toString());
                }
            }
        });
        okHttpHandler.execute(CATEGORY_SEARCH_FLAG,term,lat.toString(),lon.toString(),offset.toString());
    }

    private void parseCategoryResponseBody(String body){
        SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
        List<Restaurant> restaurants = searchRestaurants.getBusinesses();
        for(Restaurant restaurant:restaurants){
            //Add categories to a LinkedHashSet to remove dupes and retain order.
            for(YelpCategory category:restaurant.getCategories()){
                if(!(blacklist_values.contains(category.getCategoryAlias()))){
                    found_categories.add(category.getCategoryAlias());
                }
            }

            for(YelpCategory title:restaurant.getCategories()){
                if(!(blacklist.contains(title.getCategoryTitle()))){
                    found_category_titles.add(title.getCategoryTitle());
                    System.out.println(title.getCategoryTitle());
                }
            }
        }
        restaurant_categories = new ArrayList<>(found_categories);
        restaurant_category_titles = new ArrayList<>(found_category_titles);
        Log.d(TAG,"Restaurant Category size" + restaurant_categories.size());
        Log.d(TAG,"Restaurant Title size" + restaurant_categories.size());
    }

    private void handleCategory(){
        if(index < restaurant_categories.size()){
            selectFoodCategory(restaurant_category_titles,restaurant_categories,index);
        }
        else{
            Log.d(TAG,"Offset calls" + offset_calls);
            if(offset_calls >= OFFSET_LIMIT){ //Api Limitation
                Toast.makeText(getActivity(), "End of category. Find Food again?", Toast.LENGTH_SHORT).show();
                start_ffood_button.setClickable(true);
            }
            else{
                requestFoodCategory();
            }
        }
    }

    private void selectFoodCategory(ArrayList<String> rest_titles,ArrayList<String> rest_categories, int i){
        String restaurantTitle = rest_titles.get(i);
        System.out.println("Title " + restaurantTitle);
        String restaurantCategory = rest_categories.get(i);
//
        if(index == 0) { //Only say this on the first instance
            TextToSpeechHandler ttsh = new TextToSpeechHandler(getContext(), "Are you interested in...");
            ttsh.speak();
        }
        callDialogFragment(restaurantTitle,restaurantCategory);
    }

    private boolean isConnectedToNetwork(Context c){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            connected = true;
        }
        return connected;
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
    public void yes(String category, Boolean blacklisted, String title) {
        Log.d(TAG, "yes: Received input " + category);
        String term = category;
        if(blacklisted == true){
            addToBlacklist(title, category);
        }
        Integer offset = 0;
        start_ffood_button.setClickable(true);

         okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response response) {
                try{
                    String body = response.body().string();
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
        //The category is used both as a term and a category for the call.
        okHttpHandler.execute(RESTAURANT_SEARCH_FLAG,term,lat.toString(),lon.toString(),offset.toString(),term);
    }

    /*If answered no on Dialog frag, iterate index and search again.*/
    @Override
    public void no(String category, Boolean blacklisted, String title){
        if(blacklisted == true){
            addToBlacklist(title, category);
        }
        Log.d(TAG, "no: Received input, Launching dialog again.");

        //If incrementing index results to out of bounds request for more categories.
        if(index < restaurant_categories.size()){
            index++;
            Log.d(TAG,"Index: " + index);
        }
        handleCategory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressBar.setVisibility(View.INVISIBLE);
        start_ffood_button.setClickable(true);
        if(okHttpHandler != null && okHttpHandler.getStatus() == AsyncTask.Status.RUNNING) {
            okHttpHandler.cancel(true);
            Log.d(TAG, "Cancelled OkHTTPAsync");
        }

    }
}
