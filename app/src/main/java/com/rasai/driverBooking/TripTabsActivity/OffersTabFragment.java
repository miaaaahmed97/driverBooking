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
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Offer;
import com.rasai.driverBooking.CustomObject.TripInformation;
import com.rasai.driverBooking.DriverHome;
import com.rasai.driverBooking.MakeOffer;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver/"+phone_Number+"/offersMade");
        class MyValueEventListener implements ValueEventListener, Serializable {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offersList.clear();

                Log.d("testing", dataSnapshot.getValue().toString());
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children){
                    offersList.add(child.getValue().toString());
                    //Log.d("testing trips", offersList.toString());
                }

               offersCallback();
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
            Log.d("testing offers", dataSnapshot.toString());
            m_offer = dataSnapshot.getValue(Offer.class);
            offerObjects.add(m_offer);

            tripsCallback();

            mAdapter = new CustomListAdapter(getActivity(),R.layout.fragment_one, offeredTripsList);
            mListView.setAdapter(mAdapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void offersCallback(){
        Log.d("pleasee", offersList.toString());
        for(String offer: offersList){
            m_offer = new Offer();
            Log.d("testing in databasefor", "inside");
            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Offer/"+offer+"/"+phone_Number+"/");
            myRef.addValueEventListener(new MyOfferValueEventListener());

        }
    }

    private void tripsCallback(){
        Log.d("TAG", "trips callback");
        for (final Offer offer: offerObjects){
            m_trip = new TripInformation();
            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().
                    child("Trips/"+offer.getCustomerPhoneNumber()+"/"+offer.getTripID()+"/");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Log.d("testing_for", dataSnapshot.toString());

                    m_trip= dataSnapshot.getValue(TripInformation.class);
                    m_trip.setDriverOffer(offer.getAmount());
                    offeredTripsList.add(m_trip);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
