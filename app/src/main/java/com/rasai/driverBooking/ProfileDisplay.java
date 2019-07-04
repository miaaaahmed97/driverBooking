package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rasai.driverBooking.TripTabsActivity.TripTabsActivity;


public class ProfileDisplay extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int ACTIVITY_NUM = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        //creating bottom navigation view
        setupBottomNavigationView();
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d("checking4", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.enableNavigation(ProfileDisplay.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_house:
                Intent intent0 = new Intent(ProfileDisplay.this, DriverHome.class);//ACTIVITY_NUM = 0
                startActivity(intent0);
                break;
            case R.id.ic_car:
                Intent intent1 = new Intent(ProfileDisplay.this, TripTabsActivity.class);//ACTIVITY_NUM = 1
                startActivity(intent1);
                break;
            case R.id.ic_chat:
                Intent intent2 = new Intent(ProfileDisplay.this, DriverHome.class);//ACTIVITY_NUM = 2
                startActivity(intent2);
                break;
            case R.id.ic_avatar:
                Intent intent3 = new Intent(ProfileDisplay.this, ProfileDisplay.class);//ACTIVITY_NUM = 3
                startActivity(intent3);
                break;
        }
        return false;
    }
}
