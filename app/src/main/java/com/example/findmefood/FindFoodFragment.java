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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;


public class FindFoodFragment extends Fragment implements FoodDialogFragment.OnInputSelected{
    private static final String TAG = OkHttpHandler.class.getName();
    private static final String TOKEN = "V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx";
    String response;
    List<Restaurant> restaurants;
    Button start_ffood_button;
    TextView mTextView;
    public static int index = 0;

    /*Interface from DialogFrag*/
    @Override
    public void yes(String input) {
        Log.d(TAG, "yes: Received input " + input);
        mTextView.setText(input);
    }
    @Override
    public void no(){
        Log.d(TAG, "no: Received input, Launching dialog again.");
        index++;
        findFoodAction(restaurants,index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findfood,container,false);
        start_ffood_button = view.findViewById(R.id.startffood);
        mTextView = view.findViewById(R.id.ffoodtextview);
        final Gson gson = new Gson();

        start_ffood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"FFoodFragment Start OKHTTP");
                int offset;

                OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
                    @Override
                    public void processFinished(Response response) {
                        try{
                            /* TODO Fill in the needed details
                             *  Category
                             *  Blacklist
                             *  Offset Issue
                             *  */
                            String body = response.body().string();
                            Log.d(TAG,"Response data: " + body);
                            SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
                            restaurants = searchRestaurants.getBusinesses();
                            Log.d(TAG,"Found : " + restaurants.size() + " results");
//                            Log.d(TAG, "Businesses: " + searchRestaurants.getBusinesses().toString());
//                            Log.d(TAG,"Restaurant[0]: " + searchRestaurants.getBusinesses().get(0).toString());
//                            Log.d(TAG,"Restaurant[0] Address: " + searchRestaurants.getBusinesses().get(0).getLocation().showAddress());
                            Log.d(TAG,"Categories of Restaurant[0]: " + searchRestaurants.getBusinesses().get(0).getCategories().get(0).getCategoryTitle());
                            mTextView.setText(searchRestaurants.getBusinesses().get(0).getCategories().get(0).getCategoryTitle());
                            findFoodAction(restaurants,index);
                        }
                        catch (IOException e){
                            Log.d(TAG,"IO EXCEPTION AT FRAGMENT PROCESS FINISHED");
                        }
                    }
                });
                okHttpHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "TODO");
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

    public void findFoodAction(List<Restaurant> restaurants, int i){
        String restaurantTitle = restaurants.get(i).getCategories().get(0).getCategoryTitle();
        String restaurantCategory = restaurants.get(i).getCategories().get(0).getCategoryTitle();
        openDialogFragment(restaurantTitle,restaurantCategory);
    }
}
