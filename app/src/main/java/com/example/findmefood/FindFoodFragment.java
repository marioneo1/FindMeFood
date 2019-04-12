package com.example.findmefood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.findmefood.models.Restaurant;
import com.example.findmefood.models.SearchRestaurantsResults;
import com.example.findmefood.models.YelpCategory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import okhttp3.Response;


public class FindFoodFragment extends Fragment implements FoodDialogFragment.OnInputSelected{
    private static final String TAG = OkHttpHandler.class.getName();
    private static final String TOKEN = "V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx";
    Button start_ffood_button;
    TextView mTextView;
    public static int index = 0;
    ArrayList<String> restaurant_categories;
    ArrayList<String> restaurant_category_titles;

    /*Interface from DialogFrag*/

    /*If answered yes on Dialog frag, Find closest restaurants of the specific category.*/
    @Override
    public void yes(String input) {
        Log.d(TAG, "yes: Received input " + input);
        mTextView.setText(input);
        String term = input;
        Double latitude, longitude;
        latitude = 37.72856;
        longitude = -122.47688;
        Integer offset = 0;

        OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response output) {
                Log.d(TAG,"ENDPOINT");
                //TODO Make new DialogFrag w/ all the needed stuff. Details, Option to navigate,
            }
        });
        okHttpHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, term,latitude.toString(),longitude.toString(),offset.toString());
    }

    /*If answered no on Dialog frag, iterate index and search again.*/
    @Override
    public void no(){
        Log.d(TAG, "no: Received input, Launching dialog again.");
        index++;
        if(index < restaurant_categories.size()){
            selectFoodCategory(restaurant_category_titles,restaurant_categories,index);
        }
        else{

        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findfood,container,false);
        start_ffood_button = view.findViewById(R.id.startffood);
        mTextView = view.findViewById(R.id.ffoodtextview);
        final LinkedHashSet<String> found_category_titles = new LinkedHashSet<String>();
        final LinkedHashSet<String> found_categories = new LinkedHashSet<String>();
        final Gson gson = new Gson();


        start_ffood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"FFoodFragment Start OKHTTP");
                Integer offset = 0;
                Double latitude, longitude;
                String term = "restaurants";

                latitude = 37.72856;
                longitude = -122.47688;


                OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
                    @Override
                    public void processFinished(Response response) {
                        try{
                            /* TODO Fill in the needed details
                             *  Blacklist
                             *  Offset Issue
                             *  */
                            String body = response.body().string();
                            Log.d(TAG,"Response data: " + body);
                            SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
                            List<Restaurant> restaurants = searchRestaurants.getBusinesses();
                            for(Restaurant restaurant:restaurants){


                                for(YelpCategory category:restaurant.getCategories()){
                                    found_categories.add(category.getCategoryAlias());
                                }
                                for(YelpCategory title:restaurant.getCategories()){
                                    found_category_titles.add(title.getCategoryTitle());
                                    System.out.println(title.getCategoryTitle());
                                }
                                restaurant_categories = new ArrayList<String>(found_categories);
                                restaurant_category_titles = new ArrayList<String>(found_category_titles);
                            }
                            selectFoodCategory(restaurant_category_titles,restaurant_categories,index);
                        }
                        catch (IOException e){
                            Log.d(TAG,"IO EXCEPTION AT FRAGMENT PROCESS FINISHED");
                        }
                    }
                });
                okHttpHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, term,latitude.toString(),longitude.toString(),offset.toString());
            }
        });

        return view;
    }


    /*Function to call Dialog Fragment*/
    public void openDialogFragment(String title, String category){
        FragmentManager fm = getFragmentManager();

        /*Create Dialog Fragment*/
        FoodDialogFragment newFragment = new FoodDialogFragment();
        /*To pass value to DialogFragment*/
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("category",category);
        newFragment.setArguments(args);
        newFragment.setTargetFragment(FindFoodFragment.this,1);
        newFragment.show(fm,"FoodDialogFrag");
    }


    public void selectFoodCategory(ArrayList<String> rest_titles,ArrayList<String> rest_categories, int i){
        String restaurantTitle = rest_titles.get(i);
        System.out.println("Title " + restaurantTitle);
        String restaurantCategory = rest_categories.get(i);
        openDialogFragment(restaurantTitle,restaurantCategory);
    }



}
