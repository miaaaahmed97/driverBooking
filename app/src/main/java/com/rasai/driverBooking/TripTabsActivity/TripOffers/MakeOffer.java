package com.rasai.driverBooking.TripTabsActivity.TripOffers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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
import com.rasai.driverBooking.TripTabsActivity.TripTabsActivity;

public class MakeOffer extends AppCompatActivity {

    private Button mDone, mChangeBudget, mMakeOffer;

    //cardview widgets
    TextView mFrom;
    TextView mTo;
    TextView mSDate;
    TextView mSTime;
    TextView mEDate;
    TextView mETime;
    TextView mMinBudget;
    TextView mMaxBudget;
    TextView mTripType;
    TextView mSeats;
    TextView mIsReturn;
    TextView mExtraDetails;

    //Budget managing widgets
    TextInputEditText mBudgetField;

    TripInformation tripInfo;
    String availability;;
    Offer offer;

    TripInformation mOffer;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    //Delete offer widgets
    Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        //set activity title
        assert getSupportActionBar() != null;   //null check
        setTitle("MY OFFER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        Intent i = getIntent();
        tripInfo = (TripInformation) i.getSerializableExtra("TRIP_SELECTED");
        availability = (String) i.getSerializableExtra("OFFER_AVAILABLE");


        mOffer = (TripInformation) i.getSerializableExtra("OFFER_SELECTED");
        if(mOffer!=null){
            tripInfo = mOffer;
        }

        //Start - Trip Details Display

        //view to hide
        TextView mToText = (TextView) findViewById(R.id.toText);

        mFrom = (TextView) findViewById(R.id.fromCd);
        mTo = (TextView) findViewById(R.id.toCd);
        mSDate = (TextView) findViewById(R.id.startDateCd);
        mSTime = (TextView) findViewById(R.id.startTimeCd);
        mEDate = (TextView) findViewById(R.id.endDateCd);
        mETime = (TextView) findViewById(R.id.endTimeCd);
        mMinBudget= (TextView) findViewById(R.id.budgetMinCd);
        mMaxBudget = (TextView) findViewById(R.id.budgetMaxCd);
        mTripType = (TextView) findViewById(R.id.familyOrFriendsCd);
        mSeats = (TextView) findViewById(R.id.numSeatsCd);
        mIsReturn = (TextView) findViewById(R.id.returnCd);
        mExtraDetails = (TextView) findViewById(R.id.additionalDetailsCd);

        mFrom.setText(tripInfo.getFrom());
        mTo.setText(tripInfo.getTo());
        mSDate.setText(tripInfo.getStartDate());
        mSTime.setText(tripInfo.getStartTime());
        mMinBudget.setText(tripInfo.getMinBudget());
        mMaxBudget.setText(tripInfo.getMaxBudget());
        mTripType.setText(tripInfo.getTripType());
        mSeats.setText(tripInfo.getSeats());
        mIsReturn.setText(tripInfo.getIsReturn());
        mExtraDetails.setText(tripInfo.getExtraDetails());

        if(tripInfo.getEndDate().length() > 0){
            mEDate.setText(tripInfo.getEndDate());
            mETime.setText(tripInfo.getEndTime());
        }
        else{
            mETime.setText("");
            mETime.setVisibility(TextView.GONE);
            mETime.setVisibility(TextView.GONE);
            mToText.setVisibility(TextView.GONE);
        }
        //End - Trip Details Display

        //Start - Check if trip is still available

        if (availability!=null) {
            if(availability.equals("unavailable")){

                LinearLayout makeOfferLayout = findViewById(R.id.makeOfferLinearLayout);
                makeOfferLayout.setVisibility(View.GONE);


                LinearLayout messageLayout = findViewById(R.id.messageLinearLayout);
                messageLayout.setVisibility(View.VISIBLE);

            }
        }
        //End - Check if trip is still available

        //Start - Budget Management
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mBudgetField = (TextInputEditText) findViewById(R.id.offeredBudget);


        mMakeOffer = findViewById(R.id.makeOfferButton);
        mChangeBudget = findViewById(R.id.changeBudgetButton);
        mDone = findViewById(R.id.doneBudgetButton);
        //hide change budget and done buttons
        mChangeBudget.setVisibility(View.GONE);
        mDone.setVisibility(View.GONE);

        offer = new Offer();

        mMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBudgetField.getText().length()>0){
                    //show change budget and done button and hide make offer.
                    mChangeBudget.setVisibility(View.VISIBLE);
                    mDone.setVisibility(View.VISIBLE);
                    mMakeOffer.setVisibility(View.GONE);

                    //disable budget field
                    mBudgetField.setEnabled(false);
                    mBudgetField.setFocusable(false);

                    //store the value to tripInfo object
                    offer.setToken_id(tripInfo.getCustomerToken());
                    Log.d("MakeOffer", "CustomerToken: " + tripInfo.getCustomerToken());
                    offer.setAmount(mBudgetField.getText().toString());
                    offer.setTripID(tripInfo.getDatabaseId());
                    offer.setCustomerPhoneNumber(tripInfo.getPhoneNumber());
                    offer.setDriverPhoneNumber(user.getPhoneNumber());

                    final Boolean[] added = {false};

                    DatabaseReference mRef = FirebaseDatabase.getInstance().
                            getReference().child("Driver/"+offer.getDriverPhoneNumber()+"/offersMade");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() == null){

                                offer.makeOffer(myRef);
                                added[0] = true;
                            }

                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for(DataSnapshot child: children){

                                if( child.getValue().equals(offer.getTripID())){

                                    offer.changeOffer(myRef);
                                    added[0] = true;
                                    break;
                                }

                            }
                            if(!added[0])
                            {
                                offer.makeOffer(myRef);}
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(MakeOffer.this, "Offer field cannot be empty", Toast.LENGTH_LONG).show();

                }

            }
        });

        //Start -Delete Trip
        mDeleteButton = (Button) findViewById(R.id.deleteOfferButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ///remove from driver node
                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().
                        child("Driver").child(user.getPhoneNumber()).child("offersMade");
                driverRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children){

                            if(child.getValue().toString().equals(tripInfo.getDatabaseId())){
                                child.getRef().removeValue();
                            }
                        }

                        //remove from offers
                        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference()
                                .child("Offer").child(tripInfo.getDatabaseId()).child(user.getPhoneNumber());
                        offersRef.removeValue();

                        Intent intent = new Intent(MakeOffer.this, TripTabsActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        });

        //End - Delete Trip
    }

    //on clicking back button finish activity and go back
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void changeBudgetOffer(View v) {
        if (v.getId() == R.id.changeBudgetButton) {
            //show done button
            mChangeBudget.setVisibility(View.GONE);
            mDone.setVisibility(View.GONE);
            mMakeOffer.setVisibility(View.VISIBLE);

            //enable budget field
            mBudgetField.setEnabled(true);
            mBudgetField.setFocusable(true);
            mBudgetField.setFocusableInTouchMode(true);

        }
    }

}
