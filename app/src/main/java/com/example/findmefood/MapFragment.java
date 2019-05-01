package com.example.findmefood;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    MapView mMapView;
    View mView;
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final String[] LOCATION_PERMISSIONS =
            {FINE_LOCATION, COARSE_LOCATION};
    private static final int REQUEST_CODE = 100;
    double lat,lng;
    private Context mContext;
    private Activity mActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map,null);
        mContext = getContext();
        mActivity = getActivity();


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.mymap);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "Moving to current location\n" ,Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location\n" + location.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        If no permission, request one
        if (ContextCompat.checkSelfPermission(mView.getContext(), FINE_LOCATION) == GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(mActivity, LOCATION_PERMISSIONS, REQUEST_CODE);
        }
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        // Add a marker in Sydney and move the camera
//        LatLng myLocation = new LatLng(lat,lng);
//        mMap.addMarker(new MarkerOptions().position(sf).title("Marker near SF"));
        LatLng defaultLocation = new LatLng(37.6821, -122.487);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,15.5f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity,new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!= null){
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        LatLng myLocation = new LatLng(lat,lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    }
                }
            });


    }
}
