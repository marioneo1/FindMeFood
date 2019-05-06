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
import android.widget.ImageView;
import android.widget.TextView;
import com.example.findmefood.models.Coordinates;
import com.example.findmefood.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ChooseRestaurantFragment extends Fragment{
    Button mActionNavigate, mActionInformation;
    private static ImageView imageView;
    private static final String TAG = ChooseRestaurantFragment.class.getName();
    public static final String RESTAURANT = "RESTAURANT";
    private static final String URL = "URL";
    private static final int ONE_MILE = 1609;
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
        final String name =  restaurant.getName(), url = restaurant.getUrl(), price = restaurant.getPrice();
        final String image_url = restaurant.getImage_url();
        final Coordinates coordinates = restaurant.getCoordinates();
        final List<String> address_list = restaurant.getLocation().showAddress();
        final Double rating = restaurant.getRating(), distance = restaurant.getDistance()/ONE_MILE;
        String address = "";
        StringBuilder sb = new StringBuilder();
        for(String address_item: address_list){
            sb.append(address_item + " ");
        }
        address = sb.toString();

        Log.d(TAG, "Address : " + address);

        View v = inflater.inflate(R.layout.fragment_ff_chooserestaurant,container,false);
        TextView restaurantNameTextView = (TextView)v.findViewById(R.id.cr_textview), restaurantPriceTextView = (TextView)v.findViewById(R.id.cr_text_price),
                distanceTextView = (TextView)v.findViewById(R.id.cr_text_distance), addressTextView = (TextView)v.findViewById(R.id.cr_text_address),
                ratingTextView = (TextView)v.findViewById(R.id.cr_text_rating);
        imageView = (ImageView)v.findViewById(R.id.cr_imageview);
        mActionNavigate = v.findViewById(R.id.cr_action_navigate);
        mActionInformation = v.findViewById(R.id.cr_action_information);
        Log.d(TAG,"Restaurant name: " + name);
        Log.d(TAG,"Coordinates" + coordinates.toString());
        restaurantNameTextView.setText(name);
        if(image_url != null && !(image_url.isEmpty())){
            Picasso.get().load(image_url).error(R.mipmap.ic_launcher).placeholder(R.drawable.cr_placeholder).fit().centerCrop().into(imageView);
        }
        restaurantPriceTextView.setText(price);
        ratingTextView.setText(String.format("%.2f stars",rating));
        addressTextView.setText(address);
        distanceTextView.setText(String.format("%.2f mi",distance));

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
                Intent yelpSiteIntent = new Intent(getActivity().getBaseContext(), WebViewActivity.class);
                yelpSiteIntent.putExtra(URL,url);
                startActivity(yelpSiteIntent);
            }
        });


        return v;
    }
}
