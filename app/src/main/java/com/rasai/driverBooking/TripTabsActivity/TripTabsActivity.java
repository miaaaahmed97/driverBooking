package com.rasai.driverBooking.TripTabsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */

    /**
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String tab = intent.getStringExtra("OPEN_TAB");

        if (tab!=null) {
            if(tab.equals("assigned")){
                viewPager.setCurrentItem(1);
            }
            else {
                viewPager.setCurrentItem(0);
            }
        }
        else {
            Log.d("TribTabsActivity", "inside else");
        }
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
