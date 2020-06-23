package com.dryver.driverBooking.TripTabsActivity.AssignedTrips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dryver.driverBooking.CustomObject.Offer;
import com.dryver.driverBooking.CustomObject.TripInformation;
import com.dryver.driverBooking.R;
import com.dryver.driverBooking.TripTabsActivity.CustomListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AssignedTripsTabFragment extends Fragment {

    //get current user
    private String phone_Number;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private ListView mListView;
    private CustomListAdapter mAdapter;
    private View inflateView, mNoAssignedTrips;

    private List<String> offersList = new ArrayList<String>();
    private List<Offer> offerObjects = new ArrayList<Offer>();
    private List<TripInformation> assignedTripsList = new ArrayList<TripInformation>();

    private Offer m_offer;
    private TripInformation m_trip;

    private DatabaseReference mRef;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(inflateView==null){
            inflateView = inflater.inflate(R.layout.activity_assigned_trips,container,false);
            mListView = inflateView.findViewById(R.id.assigned_list_view);
            mNoAssignedTrips = inflateView.findViewById(R.id.noAssignedTripsLayout);
        }

        phone_Number = user.getPhoneNumber();

        mRef = FirebaseDatabase.getInstance().getReference().child("Driver/"+phone_Number+"/offerConfirmed");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ViewAssignedTrips.class);
                //Log.d("testing list index", Integer.toString(position));
                TripInformation tripSelected = assignedTripsList.get(position);
                intent.putExtra("TRIP_SELECTED", tripSelected);
                startActivity(intent);
            }
        });

        return inflateView;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("AssignedTrips", "inside onStart()");
        mRef.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("AssignedTrips", "inside onStop()");
        mRef.removeEventListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("AssignedTrips", "inside onPause()");
        mRef.removeEventListener(listener);
    }

    @Override
    public void onDestroyView() {
        if (inflateView.getParent() != null) {
            ((ViewGroup)inflateView.getParent()).removeView(inflateView);
        }
        super.onDestroyView();
    }

    private class MyValueEventListener implements ValueEventListener, Serializable {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Log.d("AssignedTrips", "datasnapshot: "+dataSnapshot);

            offersList.clear();
            offerObjects.clear();
            assignedTripsList.clear();

            if(!isVisibleToUser(mListView)){
                mListView.setVisibility(View.VISIBLE);
                mNoAssignedTrips.setVisibility(View.GONE);
            }

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child: children){
                Log.d("AssignedTrips", "cihld: "+child);
                offersList.add(child.getValue().toString());
            }

            Log.d("AssignedTrips", "offersList: "+offersList);

            if(offersList.size()>0){
                Log.d("AssignedTrips", "offersList.size() is: "+offersList.size());
                Log.d("AssignedTrips", "inside if. make listview visible");
                mListView.setVisibility(View.VISIBLE);
                mNoAssignedTrips.setVisibility(View.GONE);
                offersCallback();
            }
            else{
                mNoAssignedTrips.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private ValueEventListener listener = new MyValueEventListener();

    private boolean isVisibleToUser(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    private class MyOfferValueEventListener implements ValueEventListener, Serializable {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            m_offer = dataSnapshot.getValue(Offer.class);
            Log.d("testing3", m_offer.getAcceptanceStatus());
            if (m_offer.getAcceptanceStatus().equals("confirmed")) {
                offerObjects.add(m_offer);
            }

            if (offerObjects.size() == offersList.size()) {
                tripsCallback();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void offersCallback(){
        for(String offer: offersList){
            m_offer = new Offer();
            Log.d("testing2 in databasefor", "inside");
            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Offer/"+offer+"/"+phone_Number+"/");
            myRef.addValueEventListener(new MyOfferValueEventListener());

        }
    }

    private void tripsCallback(){
        for (Offer offer: offerObjects){
            final Offer m_offer = offer; //might be useless
            m_trip = new TripInformation();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().
                    child("Trips/"+offer.getCustomerPhoneNumber()+"/"+offer.getTripID()+"/");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    m_trip= dataSnapshot.getValue(TripInformation.class);
                    m_trip.setDriverOffer(m_offer.getAmount());
                    if (m_trip.getConfirmed()) {
                        assignedTripsList.add(m_trip);
                        Log.d("AssignedTrips", "assignedTripsList.size() is: "+assignedTripsList.size());

                    }


                    if (assignedTripsList.size() == offerObjects.size()) {
                        Log.d("TAG", "calling adapter");
                        mAdapter = new CustomListAdapter(getActivity(),R.layout.offers_list_item, assignedTripsList);
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
