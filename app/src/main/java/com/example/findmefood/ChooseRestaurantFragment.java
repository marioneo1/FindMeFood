package com.example.findmefood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findmefood.models.Coordinates;
import com.example.findmefood.models.Restaurant;

import java.util.List;


public class ChooseRestaurantFragment extends Fragment{
    private static final String TAG = ChooseRestaurantFragment.class.getName();
    public static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    public static final String COORDINATES = "COORDINATES";

    public static final ChooseRestaurantFragment newInstance(Restaurant restaurant){
        ChooseRestaurantFragment mFrag = new ChooseRestaurantFragment();
        String name = restaurant.getName();
        Coordinates coordinates = restaurant.getCoordinates();
        Bundle bundle = new Bundle();
        bundle.putString(RESTAURANT_NAME, name);
        bundle.putSerializable(COORDINATES,coordinates);
        Log.d(TAG,"New Instance");
        mFrag.setArguments(bundle);
        return mFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String name =  getArguments().getString(RESTAURANT_NAME);
        Coordinates coordinates = (Coordinates)getArguments().getSerializable(COORDINATES);
        View v = inflater.inflate(R.layout.fragment_ff_chooserestaurant,container,false);
        TextView mTextView = (TextView)v.findViewById(R.id.cr_textview);
        Log.d(TAG,"Restaurant name: " + name);
        Log.d(TAG,"Coordinates" + coordinates.toString());
        mTextView.setText(name);

        return v;
    }
}
