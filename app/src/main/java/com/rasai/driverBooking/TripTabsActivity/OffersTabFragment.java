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
import com.rasai.driverBooking.MakeOffer;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OffersTabFragment extends Fragment {

    //get current user
    private String phone_Number;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private ListView mListView;
    private CustomListAdapter mAdapter;
    private View inflateView;
    private LayoutInflater minflater;

    List<String> offersList = new ArrayList<String>();
    List<Offer> offerObjects = new ArrayList<Offer>();
    List<TripInformation> list = new ArrayList<TripInformation>();
    Offer m_offer;
    TripInformation m_trip;

    List<TripInformation> offeredTripsList = new ArrayList<TripInformation>();


    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        minflater = OffersTabFragment.this.getLayoutInflater();
        inflateView = minflater.inflate(R.layout.activity_offer_list,null,true);
        mListView = (ListView) inflateView.findViewById(R.id.list_view);

        phone_Number = user.getPhoneNumber();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent intent = new Intent(getActivity(), MakeOffer.class);
                //Log.d("testing list index", Integer.toString(position));
                final TripInformation offerSelected = offeredTripsList.get(position);

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Offer/"+
                        offerSelected.getDatabaseId()+"/"+phone_Number);
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("acceptanceStatus").getValue().equals("unavailable")) {
                            intent.putExtra("OFFER_AVAILABLE", "unavailable");
                        }

                        intent.putExtra("OFFER_SELECTED", offerSelected);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver/"+phone_Number+"/offersMade");
        class MyValueEventListener implements ValueEventListener, Serializable {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offeredTripsList.clear();

                //get all the unconfirmed offers made by the driver
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children){
                    offersList.add(child.getValue().toString());
                }

               offersCallback();
                Log.d("TAG", "after offers callback");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }

        mRef.addValueEventListener(new MyValueEventListener());

        return inflateView;
    }

    class MyOfferValueEventListener implements ValueEventListener, Serializable {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            m_offer = dataSnapshot.getValue(Offer.class);
            Log.d("testing3", m_offer.getAcceptanceStatus());

            if (m_offer.getAcceptanceStatus().equals("unconfirmed") ||
                    m_offer.getAcceptanceStatus().equals("unavailable")) {

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
                    /*if (m_trip.getConfirmed() == false ) {
                        offeredTripsList.add(m_trip);
                    }*/
                    offeredTripsList.add(m_trip);


                    if (offeredTripsList.size() == offerObjects.size()) {
                        Log.d("TAG", "calling adapter");

                        mAdapter = new CustomListAdapter(getActivity(),R.layout.fragment_one, offeredTripsList);
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
