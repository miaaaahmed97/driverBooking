package com.rasai.driverBooking.TripTabsActivity;

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
import com.rasai.driverBooking.CustomObject.Offer;
import com.rasai.driverBooking.CustomObject.TripInformation;
import com.rasai.driverBooking.R;
import com.rasai.driverBooking.TripTabsActivity.AssignedTrips.ViewAssignedTrips;

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
    private View inflateView;

    DatabaseReference mRef;
    List<String> tripsList = new ArrayList<String>();
    List<Offer> offerObjects = new ArrayList<Offer>();
    List<TripInformation> list = new ArrayList<TripInformation>();
    Offer m_offer;
    TripInformation m_trip;

    List<TripInformation> completedTripsList = new ArrayList<TripInformation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_Number = user.getPhoneNumber();
        Log.d("fragment","in history oncreate");

        mRef = FirebaseDatabase.getInstance().
                getReference().child("Driver/"+phone_Number+"/tripsCompleted");

        //mRef.addValueEventListener(new MyValueEventListener());

    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //minflater = HistoryTabFragment.this.getLayoutInflater();
        if(inflateView==null){
            Log.d("fragment","in history oncreateview");
            inflateView = inflater.inflate(R.layout.activity_history_list,container,false);
            mListView = (ListView) inflateView.findViewById(R.id.history_list_view);
        }

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
    public void onStart(){
        super.onStart();
        Log.d("fragment","in history start");

        mRef.addValueEventListener(new MyValueEventListener());
    }

    @Override
    public void onDestroyView() {
        if (inflateView.getParent() != null) {
            ((ViewGroup)inflateView.getParent()).removeView(inflateView);
            Log.d("fragment","in history destroy");

        }
        super.onDestroyView();
    }

    private class MyValueEventListener implements ValueEventListener, Serializable {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            completedTripsList.clear();

            //Log.d("testing1", dataSnapshot.getValue().toString());
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child: children){
                tripsList.add(child.getValue().toString());
            }

            offersCallback();
            Log.d("testing0 HistoryTab", "after offers callback");
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private class MyOfferValueEventListener implements ValueEventListener, Serializable {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            m_offer = dataSnapshot.getValue(Offer.class);

            Log.d("testing1 HistoryTabs", m_offer.getAcceptanceStatus());
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
            Log.d("fragment","in history offerscallback");
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
            Log.d("fragment","in history tripscallback");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    m_trip= dataSnapshot.getValue(TripInformation.class);
                    m_trip.setDriverOffer(m_offer.getAmount());
                    //TODO ye kya hai? shouldnt it be completed
                    if (m_trip.getConfirmed()) {
                        completedTripsList.add(m_trip);
                    }


                    if (completedTripsList.size() == offerObjects.size()) {
                        Log.d("TAG", "calling adapter");
                        mAdapter = new CustomListAdapter(getActivity(),R.layout.offers_list_item, completedTripsList);
                        mListView.setAdapter(mAdapter);
                        //mAdapter.notifyDataSetChanged();
                        Log.d("fragment","in history tripscallbackvalue ofrdtrpslst = ofrobjcts");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
