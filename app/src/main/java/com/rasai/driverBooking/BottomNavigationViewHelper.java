package com.rasai.driverBooking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rasai.driverBooking.MessageActivity.MainChat;
import com.rasai.driverBooking.TripTabsActivity.TripTabsActivity;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

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
                        if (!(context instanceof DriverHome)) {
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
