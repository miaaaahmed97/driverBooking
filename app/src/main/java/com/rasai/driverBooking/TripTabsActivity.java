package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

public class TripTabsActivity extends AppCompatActivity implements Serializable {

    private TabAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_tabs);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mListView = (ListView) findViewById(R.id.list_view);

        adapter = new TabAdapter(getSupportFragmentManager());

        OffersTabFragment tab1 = new OffersTabFragment();
        AssisgnedTripsTabFragment tab2 = new AssisgnedTripsTabFragment();
        HistoryTabFragment tab3 = new HistoryTabFragment();

        adapter.addFragment(tab1, "Offers");
        adapter.addFragment(tab2, "Assigned Trips");
        adapter.addFragment(tab3, "History");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
