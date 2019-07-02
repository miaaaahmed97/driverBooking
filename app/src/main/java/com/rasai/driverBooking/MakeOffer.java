package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasai.driverBooking.CustomObject.Offer;
import com.rasai.driverBooking.CustomObject.TripInformation;
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
    Offer offer;

    TripInformation mOffer;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        Intent i = getIntent();
        tripInfo = (TripInformation) i.getSerializableExtra("TRIP_SELECTED");

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
                //show change budget and done button and hide make offer.
                mChangeBudget.setVisibility(View.VISIBLE);
                mDone.setVisibility(View.VISIBLE);
                mMakeOffer.setVisibility(View.GONE);

                //disable budget field
                mBudgetField.setEnabled(false);
                mBudgetField.setFocusable(false);

                //store the value to tripInfo object
                offer.setAmount(mBudgetField.getText().toString());
                offer.setTripID(tripInfo.getDatabaseId());
                offer.setCustomerPhoneNumber(tripInfo.getPhoneNumber());
                offer.setDriverPhoneNumber(user.getPhoneNumber());

                if(tripInfo.getDriverOffer().length() > 0){
                    offer.changeOffer(myRef);
                }
                else{
                    offer.makeOffer(myRef);
                }
            }
        });

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

    public void doneOffer(View v){
        //go to next page
        Intent intent = new Intent(MakeOffer.this, TripTabsActivity.class);
        startActivity(intent);
    }
}
