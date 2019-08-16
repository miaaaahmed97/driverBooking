package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.CustomObject.TripInformation;
import com.rasai.driverBooking.TripTabsActivity.TripOffers.MakeOffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverHome extends AppCompatActivity implements Serializable{

    private static final String TAG = "DriverHome";
    private static final int ACTIVITY_NUM = 0;
    private ListView mListView;
    private CustomListAdapter mAdapter;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private List<TripInformation> postedTripsList = new ArrayList<TripInformation>();
    private List<String> offersMade = new ArrayList<String>();

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        //Set tht title
        assert getSupportActionBar() != null;   //null check
        setTitle("HOME");

        mListView = findViewById(R.id.history_list_view);

        mAdapter = new CustomListAdapter(DriverHome.this, R.layout.driver_home_list_item, postedTripsList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(DriverHome.this, MakeOffer.class);
                //Log.d("testing list index", Integer.toString(position));
                TripInformation tripSelected = postedTripsList.get(position);
                intent.putExtra("TRIP_SELECTED", tripSelected);
                startActivity(intent);
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference().child("Trips");


        //Log.d("Testing list in Home", postedTripsList.toString());

        //creating bottom navigation view
        setupBottomNavigationView();

    }

    class MyValueEventListener implements ValueEventListener, Serializable {


        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            postedTripsList.clear();

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

            //Iterate through phone numbers
            for(DataSnapshot child: children){

                if (!child.getKey().equals(user.getPhoneNumber())) {
                    Iterable<DataSnapshot> inner_children = child.getChildren();

                    //Iterate through the trips of a given phone number
                    for(DataSnapshot inner_child: inner_children){

                        try {

                            Log.d("DriverHome", "compare: "+ offersMade.contains(inner_child.child("databaseId").getValue(String.class)));

                            if (!inner_child.child("confirmed").getValue(Boolean.class) &&
                                    !(offersMade.contains(inner_child.child("databaseId").getValue(String.class)))) {
                                TripInformation tripInfo = new TripInformation();

                                tripInfo.setFrom(inner_child.child("from").getValue(String.class));
                                tripInfo.setTo(inner_child.child("to").getValue(String.class));
                                tripInfo.setStartDate(inner_child.child("startDate").getValue(String.class));
                                tripInfo.setStartTime(inner_child.child("startTime").getValue(String.class));
                                tripInfo.setEndDate(inner_child.child("endDate").getValue(String.class));
                                tripInfo.setEndTime(inner_child.child("endTime").getValue(String.class));
                                tripInfo.setMinBudget(inner_child.child("minBudget").getValue(String.class));
                                tripInfo.setMaxBudget(inner_child.child("maxBudget").getValue(String.class));
                                tripInfo.setTripType(inner_child.child("tripType").getValue(String.class));
                                tripInfo.setSeats(inner_child.child("seats").getValue(String.class));
                                tripInfo.setExtraDetails(inner_child.child("extraDetails").getValue(String.class));
                                tripInfo.setPhoneNumber(inner_child.child("phoneNumber").getValue(String.class));
                                tripInfo.setIsReturn(inner_child.child("isReturn").getValue(String.class));
                                tripInfo.setDatabaseId(inner_child.child("databaseId").getValue(String.class));
                                tripInfo.setCustomerToken(inner_child.child("customerToken").getValue(String.class));

                                postedTripsList.add(tripInfo);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                }
            }


        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private ValueEventListener listener = new MyValueEventListener();

    //cannot go back to previous activity, closes down app to background
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(DriverHome.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DriverHome", "inside onStart()");

        //Call separate thread to avoid application doing too much work on its main thread
        new Thread(){
            @Override
            public void run(){
                displayList();
            }
        }.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mRef.removeEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DriverHome", "inside onStop()");
        mRef.removeEventListener(listener);

    }

    private void displayList(){
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference()
                .child("Driver").child(user.getPhoneNumber()).child("offersMade");
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("DriverHome", "datasnapshot: "+dataSnapshot.toString());
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children){
                    Log.d("DriverHome", "child: "+child.getValue().toString());
                    offersMade.add(child.getValue().toString());
                }

                mRef.addValueEventListener(listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
