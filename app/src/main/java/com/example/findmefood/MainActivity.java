package com.example.findmefood;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


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
                    case R.id.action_search:
                        Log.d(TAG, "OnCreate: Search");
                        fragment = new SearchFragment();
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
        /*Set Home fragment as default selected.*/
        bottomNavigationView.setSelectedItemId(R.id.action_findfood);
    }

    /*Method to call to load the fragment.*/
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,"CurrentFragment").commit();
            return true;
        }
        return false;
    }

}
