package com.dryver.driverBooking;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.dryver.driverBooking.MessageActivity.MainChat;
import com.dryver.driverBooking.TripTabsActivity.TripTabsActivity;

public class BottomNavigationViewHelper {

    // --Commented out by Inspection (8/16/2019 6:29 PM):private static final String TAG = "BottomNavigationViewHel";

    public static void enableNavigation(final Context context, BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_house:
                        if (!(context instanceof DriverHome)){
                            Intent intent0 = new Intent(context, DriverHome.class);//ACTIVITY_NUM = 0
                            context.startActivity(intent0);
                        }
                        break;
                    case R.id.ic_car:
                        if (!(context instanceof TripTabsActivity)) {
                            Intent intent1 = new Intent(context, TripTabsActivity.class);//ACTIVITY_NUM = 1
                            context.startActivity(intent1);
                        }
                        break;
                    case R.id.ic_chat:
                        if (!(context instanceof MainChat)) {
                            Intent intent2 = new Intent(context, MainChat.class);//ACTIVITY_NUM = 2
                            context.startActivity(intent2);
                        }
                        break;
                    case R.id.ic_avatar:
                        //not needed unless profile display has navigattion bar
                        //if (!(context instanceof ProfileDisplay)) {
                            Intent intent3 = new Intent(context, ProfileDisplay.class);//ACTIVITY_NUM = 3
                            context.startActivity(intent3);
                        //}
                        break;
                }
                return false;
            }
        });
    }

}
