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


    List<TripInformation> postedTripsList = new ArrayList<TripInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        mListView = findViewById(R.id.list_view);

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

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Trips");

        class MyValueEventListener implements ValueEventListener, Serializable {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postedTripsList.clear();

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //Iterate through phone numbers
                for(DataSnapshot child: children){

                    Log.d("testing0", "Inside first for loop");

                    Log.d("testing1", child.toString());
                    Log.d("testing2", child.getKey());
                    Log.d("testing3", child.getValue().toString());

                    if (!child.getKey().equals(user.getPhoneNumber())) {
                        Iterable<DataSnapshot> inner_children = child.getChildren();

                        //Iterate through the trips of a given phone number
                        for(DataSnapshot inner_child: inner_children){

                            //Log.d("testing2", "Inside first for loop");

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

                            postedTripsList.add(tripInfo);
                            //Log.d("Testinglist in Homeloop", postedTripsList.toString());

                        }
                    }
                }
                mAdapter = new CustomListAdapter(DriverHome.this, R.layout.driver_home_list_item, postedTripsList);
                mAdapter.notifyDataSetChanged();
                mListView.setAdapter(mAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        }
        mRef.addValueEventListener(new MyValueEventListener());
        //Log.d("Testing list in Home", postedTripsList.toString());

        //creating bottom navigation view
        setupBottomNavigationView();

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
}
