package com.rasai.driverBooking.TripTabsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.rasai.driverBooking.BottomNavigationViewHelper;
import com.rasai.driverBooking.R;
import com.rasai.driverBooking.TripTabsActivity.AssignedTrips.AssignedTripsTabFragment;
import com.rasai.driverBooking.TripTabsActivity.TripOffers.OffersTabFragment;

import java.io.Serializable;

public class TripTabsActivity extends AppCompatActivity implements Serializable {

    private TabAdapter adapter;
    private static final int ACTIVITY_NUM = 1;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_tabs);

        //hide the top bar
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mListView = (ListView) findViewById(R.id.history_list_view);

        adapter = new TabAdapter(getSupportFragmentManager());

        OffersTabFragment tab1 = new OffersTabFragment();
        AssignedTripsTabFragment tab2 = new AssignedTripsTabFragment();
        HistoryTabFragment tab3 = new HistoryTabFragment();

        adapter.addFragment(tab1, "Offers");
        adapter.addFragment(tab2, "Assigned Trips");
        adapter.addFragment(tab3, "History");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //creating bottom navigation view
        setupBottomNavigationView();
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d("TripTabs", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(TripTabsActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
