package com.example.findmefood;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.d(TAG, "OnCreate: Home");
                        break;
                    case R.id.action_findfood:
                        Log.d(TAG, "OnCreate: Find Food");
//                        Button fFoodButton = findViewById(R.id.findfoodbutton);
//                        fFoodButton.setVisibility(View.VISIBLE);
//                        fFoodButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Log.d(TAG, "Button pressed");
//                            }
//                        });
                        break;
                    case R.id.action_map:
                        Log.d(TAG, "OnCreate: Maps");
                        break;
                }
                return true;
            }
        });
    }
}
