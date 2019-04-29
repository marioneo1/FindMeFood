package com.example.findmefood;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class LocationHandler implements LocationListener{
    private static final String TAG = LocationHandler.class.getName();
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final String[] LOCATION_PERMISSIONS =
            {FINE_LOCATION, COARSE_LOCATION};
    private static final int REQUEST_CODE = 77;
    private static Double latitude;
    private static Double longitude;
    private static LocationManager mLocationManager;
    private static Location mLocation;
    private static LocationListener mLocationListener;
    private Criteria criteria;
    private String provider;
    private static Context mContext;

    public LocationHandler(Context context){
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = mLocationManager.getBestProvider(criteria,true); //Find best provider
        if (ContextCompat.checkSelfPermission(mContext, FINE_LOCATION) != GRANTED){
            Toast.makeText(mContext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(mContext), LOCATION_PERMISSIONS, REQUEST_CODE);
        }
        Toast.makeText(mContext, "Success! Running GPS", Toast.LENGTH_SHORT).show();

        /*Set location to last known*/
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.d(TAG, "last known lat: " + location.getLatitude()
                    + " lon: " + location.getLongitude());
            Toast.makeText(mContext, "Found Location", Toast.LENGTH_SHORT).show();
            mLocation = location;
        }

        Log.d(TAG, "requesting updates");

        mLocationManager.requestLocationUpdates(provider,1,2,this);
//        startLocationUpdates();
    }

    public static Double getLat() {
        latitude = (mLocation != null) ? mLocation.getLatitude() : 0;
        return latitude;
    }

    public static Double getLon() {
        longitude = (mLocation != null) ? mLocation.getLongitude()  : 0;
        return longitude;
    }

    /*Context to Activity converter from stackoverflow https://stackoverflow.com/questions/9891360/getting-activity-from-context-in-android*/
    public static Activity getActivity(Context context){
        {
            if (context == null)
            {
                return null;
            }
            else if (context instanceof ContextWrapper)
            {
                if (context instanceof Activity)
                {
                    return (Activity) context;
                }
                else
                {
                    return getActivity(((ContextWrapper) context).getBaseContext());
                }
            }
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(mContext, "onLocationChanged", Toast.LENGTH_SHORT).show();
        mLocation = location; //Set location to new location if changed
        Log.d(TAG, " loc changed, new lat: " + mLocation.getLatitude()
                + " lon: " + mLocation.getLongitude());
    }

    /*Required methods. Not used for anything*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {
    }
}
