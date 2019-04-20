package com.example.findmefood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.findmefood.models.Coordinates;
import com.example.findmefood.models.Restaurant;

import okhttp3.HttpUrl;


public class ChooseRestaurantFragment extends Fragment{
    private static final String TAG = ChooseRestaurantFragment.class.getName();
    public static final String RESTAURANT = "RESTAURANT";
    public enum Mode {

        DRIVE("d"),BIKE("b"),MOTOR("l"),WALK("w");

        private String mode;

            Mode(String theMode){
                this.mode = theMode;
            }

            private String getMode(){
                return mode;
            }
    }

    public static final ChooseRestaurantFragment newInstance(Restaurant restaurant){
        ChooseRestaurantFragment mFrag = new ChooseRestaurantFragment();
        String name = restaurant.getName();
        Coordinates coordinates = restaurant.getCoordinates();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESTAURANT,restaurant);
        Log.d(TAG,"New Instance");
        mFrag.setArguments(bundle);
        return mFrag;
    }

    Button mActionNavigate, mActionInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Restaurant restaurant = (Restaurant) getArguments().getSerializable(RESTAURANT);
        final String name =  restaurant.getName();
        final Coordinates coordinates = restaurant.getCoordinates();
        View v = inflater.inflate(R.layout.fragment_ff_chooserestaurant,container,false);
        TextView mTextView = (TextView)v.findViewById(R.id.cr_textview);
        mActionNavigate = v.findViewById(R.id.cr_action_navigate);
        mActionInformation = v.findViewById(R.id.cr_action_information);
        Log.d(TAG,"Restaurant name: " + name);
        Log.d(TAG,"Coordinates" + coordinates.toString());
        mTextView.setText(name);


        mActionNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Navigate Activity
                Log.d(TAG, "Restaurant : " + name + " with " + coordinates.toString());
                Double lat = coordinates.getLatitude();
                Double lon = coordinates.getLongitude();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +lat.toString()+ "," + lon.toString()
                        + "&mode="+ Mode.DRIVE.getMode());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        mActionInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Information Activity
                //TODO
            }
        });


        return v;
    }
}
