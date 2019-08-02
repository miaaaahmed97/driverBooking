package com.rasai.driverBooking.TripTabsActivity.AssignedTrips;

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
import com.rasai.driverBooking.TripTabsActivity.CustomListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AssignedTripsTabFragment extends Fragment {

    //get current user
    private String phone_Number;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private ListView mListView;
    private View mNoAssignedLayout;
    private CustomListAdapter mAdapter;
    private View inflateView;
    //private LayoutInflater minflater;

    private List<String> offersList = new ArrayList<String>();
    private List<Offer> offerObjects = new ArrayList<Offer>();
    List<TripInformation> list = new ArrayList<TripInformation>();
    private Offer m_offer;
    private TripInformation m_trip;
    private DatabaseReference mRef;
    private List<TripInformation> assignedTripsList = new ArrayList<TripInformation>();


    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //minflater = AssignedTripsTabFragment.this.getLayoutInflater();
        if(inflateView==null){
            inflateView = inflater.inflate(R.layout.activity_assigned_trips,container,false);
            mListView = (ListView) inflateView.findViewById(R.id.assigned_list_view);
            mNoAssignedLayout = inflateView.findViewById(R.id.noAssignedTripsLayout);
        }

        phone_Number = user.getPhoneNumber();

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

        mRef = FirebaseDatabase.getInstance().getReference().child("Driver/"+phone_Number+"/offerConfirmed");

        mRef.addValueEventListener(new MyValueEventListener());

        return inflateView;
    }

    /*@Override
    public void onStart(){
        super.onStart();
        mRef.addValueEventListener(new MyValueEventListener());
    }*/

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

            assignedTripsList.clear();

            //Log.d("testing1", dataSnapshot.getValue().toString());
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child: children){
                offersList.add(child.getValue().toString());
            }

            if(offersList.size()>0){
                offersCallback();
                Log.d("TAG", "after offers callback");
            }
            else{
                mNoAssignedLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
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
                    if (m_trip.getConfirmed() == true ) {
                        assignedTripsList.add(m_trip);
                    }


                    if (assignedTripsList.size() == offerObjects.size()) {
                        Log.d("TAG", "calling adapter");
                        mAdapter = new CustomListAdapter(getActivity(),R.layout.offers_list_item, assignedTripsList);
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
