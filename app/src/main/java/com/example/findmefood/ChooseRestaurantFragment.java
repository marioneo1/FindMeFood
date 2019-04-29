package com.example.findmefood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.findmefood.models.Coordinates;
import com.example.findmefood.models.Restaurant;
import com.squareup.picasso.Picasso;


public class ChooseRestaurantFragment extends Fragment{
    Button mActionNavigate, mActionInformation;
    private static ImageView imageView;
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
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESTAURANT,restaurant);
        Log.d(TAG,"New Instance");
        mFrag.setArguments(bundle);
        return mFrag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mActivity = getActivity();
        final Restaurant restaurant = (Restaurant) getArguments().getSerializable(RESTAURANT);
        final String name =  restaurant.getName();
        final String url = restaurant.getUrl();
        final String image_url = restaurant.getImage_url();
        final Coordinates coordinates = restaurant.getCoordinates();
        View v = inflater.inflate(R.layout.fragment_ff_chooserestaurant,container,false);
        TextView mTextView = (TextView)v.findViewById(R.id.cr_textview);
        imageView = (ImageView)v.findViewById(R.id.cr_imageview);
        mActionNavigate = v.findViewById(R.id.cr_action_navigate);
        mActionInformation = v.findViewById(R.id.cr_action_information);
        Log.d(TAG,"Restaurant name: " + name);
        Log.d(TAG,"Coordinates" + coordinates.toString());
        mTextView.setText(name);
        Picasso.get().load(image_url).fit().into(imageView);
        //Todo: Add more information in screen


        mActionNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch Navigate Activity
                Log.d(TAG, "Restaurant : " + name + " with " + coordinates.toString());
                Double lat = coordinates.getLatitude();
                Double lon = coordinates.getLongitude();

                //Make uri, and call Google Maps, passing the uri in.
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
                Intent yelpSiteIntent = new Intent(Intent.ACTION_VIEW);
                yelpSiteIntent.setData(Uri.parse(url));
                startActivity(yelpSiteIntent);
            }
        });


        return v;
    }
}
