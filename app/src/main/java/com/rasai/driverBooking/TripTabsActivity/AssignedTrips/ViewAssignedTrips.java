package com.dryver.driverBooking.TripTabsActivity.AssignedTrips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dryver.driverBooking.CustomObject.Review;
import com.dryver.driverBooking.CustomObject.TripInformation;
import com.dryver.driverBooking.R;
import com.dryver.driverBooking.TripTabsActivity.TripTabsActivity;

import java.util.ArrayList;
import java.util.List;


public class ViewAssignedTrips extends AppCompatActivity {

    //cardview widgets
    private TextView mFrom;
    private TextView mTo;
    private TextView mSDate;
    private TextView mSTime;
    private TextView mEDate;
    private TextView mETime;
    private TextView mOffer;
    private TextView mTripType;
    private TextView mSeats;
    private TextView mIsReturn;
    private TextView mExtraDetails;

    private Button mCompletedTrip, mSubmitReview;
    private View mReviewPopup, mBadReviewPopup;
    private RatingBar mRatingBar;
    private EditText mReviewDetails;

    //First Popup
    private Float ratingNumber;
    private String reviewDetails;

    //Second Popup
    private String badLanguage;
    private String paymentProblem;
    private String promiseBreaker;
    private String punctualProblem;
    private String otherProblemDetails;
    private List<String> problemsList = new ArrayList<>();

    private TripInformation tripInfo;
    private Review review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assigned_trip);

        //set title Bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        //Get trip info of the trip selected from previous activity
        Intent i = getIntent();
        tripInfo = (TripInformation) i.getSerializableExtra("TRIP_SELECTED");

        setWidgets();
        mCompletedTrip = findViewById(R.id.completedTripButton);

        if (tripInfo.getCompleted()){
            setTitle("COMPLETED TRIP");
            mCompletedTrip.setVisibility(View.GONE);
        }
        else{
            setTitle("ASSIGNED TRIP");
            mCompletedTrip.setVisibility(View.VISIBLE);
        }

        mCompletedTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //creating the review popup
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewAssignedTrips.this);
                alertDialogBuilder.setTitle("Review Your Trip");
                //alertDialogBuilder.setCancelable(false);
                // Init popup dialog view and it's ui controls.
                initReviewPopup();
                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(mReviewPopup);
                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private void initBadReviewPopup() {

        LayoutInflater layoutInflater = LayoutInflater.from(ViewAssignedTrips.this);
        mBadReviewPopup=layoutInflater.inflate(R.layout.bad_review_popup,null);

        getWidgetInfo();
    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initReviewPopup()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(ViewAssignedTrips.this);

        // Inflate the popup dialog from a layout xml file.
        mReviewPopup = layoutInflater.inflate(R.layout.review_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        mRatingBar = mReviewPopup.findViewById(R.id.ratingBar);
        mReviewDetails = mReviewPopup.findViewById(R.id.userReviewDetails);
        mSubmitReview = mReviewPopup.findViewById(R.id.submitReviewButton);

        if (mReviewDetails.getText().toString().length() > 0) {
            reviewDetails = mReviewDetails.getText().toString();
        }

        mSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingNumber = mRatingBar.getRating();

                //Log.d("RatingBar",String.valueOf(ratingNumber));

                if(ratingNumber<4.0){

                    final AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(ViewAssignedTrips.this);
                    DialogBuilder.setTitle("What was the problem?");
                    DialogBuilder.setCancelable(false);
                    initBadReviewPopup();
                    DialogBuilder.setView(mBadReviewPopup);
                    //open bad review popup
                    final AlertDialog alertDialog2 = DialogBuilder.create();
                    alertDialog2.show();
                }
                else{
                    setHighReviewInfo();
                    submitProblemsFunction(mSubmitReview);
                }
            }
        });

    }

    private  void setWidgets(){

        //View to hide
        TextView mToText = findViewById(R.id.toText);

        mFrom = findViewById(R.id.fromCd);
        mTo = findViewById(R.id.toCd);
        mSDate = findViewById(R.id.startDateCd);
        mSTime = findViewById(R.id.startTimeCd);
        mEDate = findViewById(R.id.endDateCd);
        mETime = findViewById(R.id.endTimeCd);
        mOffer= findViewById(R.id.offeredBudget);
        mTripType = findViewById(R.id.familyOrFriendsCd);
        mSeats = findViewById(R.id.numSeatsCd);
        mIsReturn = findViewById(R.id.returnCd);
        mExtraDetails = findViewById(R.id.additionalDetailsCd);

        mFrom.setText(tripInfo.getFrom());
        mTo.setText(tripInfo.getTo());
        mSDate.setText(tripInfo.getStartDate());
        mSTime.setText(tripInfo.getStartTime());
        mOffer.setText(tripInfo.getDriverOffer());
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
    }

    private  void getWidgetInfo(){
        final CheckBox mBadLanguage, mPaymentProblem, mPromiseBreaker, mPunctualProblem, mOtherProblem;
        final EditText mOtherProblemDetails;
        Button mSubmitProblems;

        mBadLanguage = mBadReviewPopup.findViewById(R.id.badLanguage);
        mPaymentProblem= mBadReviewPopup.findViewById(R.id.paymentProblem);
        mPromiseBreaker = mBadReviewPopup.findViewById(R.id.promiseBreaker);
        mPunctualProblem = mBadReviewPopup.findViewById(R.id.punctualProblem);
        mOtherProblem = mBadReviewPopup.findViewById(R.id.otherProblem);
        mOtherProblemDetails = mBadReviewPopup.findViewById(R.id.otherProblemDetails);
        mSubmitProblems = mBadReviewPopup.findViewById(R.id.submitProblemsButton);

        mBadLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                badLanguage = mBadLanguage.getText().toString();
                problemsList.add(badLanguage);
            }
        });

        mPaymentProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                paymentProblem = mPaymentProblem.getText().toString();
                problemsList.add(paymentProblem);
            }
        });

        mPromiseBreaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                promiseBreaker = mPromiseBreaker.getText().toString();
                problemsList.add(promiseBreaker);
            }
        });

        mPunctualProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                punctualProblem = mPunctualProblem.getText().toString();
                problemsList.add(punctualProblem);
            }
        });

        mOtherProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                otherProblemDetails = mOtherProblemDetails.getText().toString();
                problemsList.add(otherProblemDetails);
            }
        });


        Log.d("testing popup0", problemsList.toString());
        submitProblemsFunction(mSubmitProblems);

    }

    private void submitProblemsFunction(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setLowReviewInfo();

                //Add review to database
                DatabaseReference mReviewRef = FirebaseDatabase.getInstance().getReference().child("Review");
                mReviewRef.child(review.getCustomerPhoneNumber()).push().setValue(review);

                //Add customer's rating to customer object
                final DatabaseReference mCustomerRef = FirebaseDatabase.getInstance().getReference().
                        child("Customer").child(review.getCustomerPhoneNumber());
                mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("ratedBy").getValue() !=null ){
                            float rating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                            int raters = Integer.parseInt(dataSnapshot.child("ratedBy").getValue().toString());
                            rating = ((rating * raters) + ratingNumber) / (raters+1);

                            dataSnapshot.child("rating").getRef().setValue(rating);
                            dataSnapshot.child("ratedBy").getRef().setValue(raters+1);

                        }
                        else{
                            dataSnapshot.child("rating").getRef().setValue(ratingNumber);
                            dataSnapshot.child("ratedBy").getRef().setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //Change completed status in "Trips" branch to true
                DatabaseReference mTripsRef = FirebaseDatabase.getInstance().getReference()
                        .child("Trips/" + tripInfo.getPhoneNumber() + "/" + tripInfo.getDatabaseId() + "/");
                mTripsRef.child("completed").setValue(true);

                //Change status in "Offers" branch to completed
                DatabaseReference mOffersRef = FirebaseDatabase.getInstance().getReference()
                        .child("Offer/" + tripInfo.getDatabaseId() + "/" + tripInfo.getDriverAssigned() + "/");
                mOffersRef.child("acceptanceStatus").setValue("completed");


                //Add trip to customer's completed trips
                DatabaseReference myCustomerRef = FirebaseDatabase.getInstance().getReference()
                        .child("Customer").child(tripInfo.getPhoneNumber());
                myCustomerRef.child("tripsCompleted/").push().setValue(tripInfo.getDatabaseId());

                //Add trip to driver's completed trips
                DatabaseReference myDriverRef = FirebaseDatabase.getInstance().getReference()
                        .child("Driver").child(tripInfo.getDriverAssigned());
                myDriverRef.child("tripsCompleted/").push().setValue(tripInfo.getDatabaseId());

                //Remove from driver's assigned trips
                myDriverRef.child("offerConfirmed").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            //Log.d("testing", child.getValue(String.class));
                            if (child.getValue(String.class).equals(tripInfo.getDatabaseId())) {
                                child.getRef().removeValue();

                                Intent intent = new Intent(ViewAssignedTrips.this, TripTabsActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    //on clicking back button finish activity and go back
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setLowReviewInfo(){

        review = new Review();

        review.setDriverPhoneNumber(tripInfo.getDriverAssigned());
        review.setCustomerPhoneNumber(tripInfo.getPhoneNumber());
        review.setRating(ratingNumber);
        review.setReview(mReviewDetails.getText().toString());
        review.setProblems(problemsList);

    }

    private void setHighReviewInfo(){

        review = new Review();
        review.setDriverPhoneNumber(tripInfo.getDriverAssigned());
        review.setCustomerPhoneNumber(tripInfo.getPhoneNumber());
        review.setRating(ratingNumber);
        review.setReview(mReviewDetails.getText().toString());

    }
}
