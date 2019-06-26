package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverHome extends AppCompatActivity implements Serializable{

    private ListView mListView;
    private CustomListAdapter mAdapter;
    private View inflateView;
    private LayoutInflater minflater;

    List<TripInformation> postedTripsList = new ArrayList<TripInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        mListView = findViewById(R.id.list_view);
        //setContentView(R.layout.activity_driver_home);

        //minflater = this.getLayoutInflater();
        //inflateView = minflater.inflate(R.layout.activity_driver_home,null,true);
        //mListView = (ListView) inflateView.findViewById(R.id.list_view);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Trips");

        class MyValueEventListener implements ValueEventListener, Serializable {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //Iterate through phone numbers
                for(DataSnapshot child: children){

                    Log.d("testing1", "Inside first for loop");

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

                            postedTripsList.add(tripInfo);
                            //Log.d("Testinglist in Homeloop", postedTripsList.toString());

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

    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){}*/
}
