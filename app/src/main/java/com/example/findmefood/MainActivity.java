package com.example.findmefood;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                /*Fragment selection*/
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.d(TAG, "OnCreate: Home");
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_findfood:
                        Log.d(TAG, "OnCreate: Find Food");
                        fragment = new FindFoodFragment();
                        break;
                    case R.id.action_map:
                        Log.d(TAG, "OnCreate: Maps");
                        fragment = new MapFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    /*Method to call to load the fragment.*/
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

}
