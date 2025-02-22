package com.dryver.driverBooking.TripTabsActivity;

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
import com.dryver.driverBooking.TripTabsActivity.AssignedTrips.ViewAssignedTrips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryTabFragment extends Fragment {

    //get current user
    private String phone_Number;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private ListView mListView;
    private CustomListAdapter mAdapter;
    private View inflateView, mNoHistoryLayout;

    private List<String> tripsList = new ArrayList<String>();
    private List<Offer> offerObjects = new ArrayList<Offer>();

    private Offer m_offer;
    private TripInformation m_trip;

    private List<TripInformation> completedTripsList = new ArrayList<TripInformation>();

    private DatabaseReference mRef;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(inflateView==null){
            inflateView = inflater.inflate(R.layout.activity_history_list,container,false);
            mListView =  inflateView.findViewById(R.id.history_list_view);
            mNoHistoryLayout = inflateView.findViewById(R.id.noHistoryLayout);
        }

        phone_Number = user.getPhoneNumber();

        mRef = FirebaseDatabase.getInstance().getReference().child("Driver/"+phone_Number+"/tripsCompleted");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ViewAssignedTrips.class);
                //Log.d("testing list index", Integer.toString(position));
                TripInformation tripSelected = completedTripsList.get(position);
                intent.putExtra("TRIP_SELECTED", tripSelected);
                startActivity(intent);
            }
        });

        return inflateView;
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

            tripsList.clear();
            offerObjects.clear();
            completedTripsList.clear();

            if(!isVisibleToUser(mListView)){
                mListView.setVisibility(View.VISIBLE);
                mNoHistoryLayout.setVisibility(View.GONE);
            }

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child: children){
                tripsList.add(child.getValue().toString());
            }

            if(tripsList.size()>0){
                mAdapter = new CustomListAdapter(getActivity(),R.layout.offers_list_item, completedTripsList);
                mAdapter.notifyDataSetChanged();
                offersCallback();
            }
            else{
                if(!isVisibleToUser(mNoHistoryLayout)){
                    mNoHistoryLayout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
            }

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private ValueEventListener listener = new MyValueEventListener();

    @Override
    public void onStart(){
        super.onStart();
        mRef.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRef.removeEventListener(listener);
    }

    private boolean isVisibleToUser(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private class MyOfferValueEventListener implements ValueEventListener, Serializable {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            m_offer = dataSnapshot.getValue(Offer.class);
            Log.d("HistoryTabFragment", "offer from database: " + m_offer.toString());

            if (m_offer.getAcceptanceStatus().equals("completed")) {
                offerObjects.add(m_offer);
            }

            if (offerObjects.size() == tripsList.size()) {
                tripsCallback();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void offersCallback(){
        for(String offer: tripsList){
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
                        completedTripsList.add(m_trip);
                    }


                    if (completedTripsList.size() == offerObjects.size()) {
                        Log.d("TAG", "calling adapter");
                        mListView.setAdapter(mAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
