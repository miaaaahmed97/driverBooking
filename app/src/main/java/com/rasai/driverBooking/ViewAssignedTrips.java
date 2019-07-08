package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;


public class ViewAssignedTrips extends AppCompatActivity {

    private Button mCompletedTrip, mSubmitReview;
    private View mReviewPopup, mBadReviewPopup;
    private RatingBar mRatingBar;
    private EditText mReviewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assigned_trip);
        setTitle("Assigned Trip");

        mCompletedTrip = findViewById(R.id.completedTripButton);

        mCompletedTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //creating the review popup
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewAssignedTrips.this);
                alertDialogBuilder.setTitle("Review Your Trip");
                alertDialogBuilder.setCancelable(false);
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
        CheckBox mBadLanguage, mPaymentProblem, mPromiseBreaker, mPunctualProblem, mOtherProblem;
        EditText mOtherProblemDetails;
        Button mSubmitProblems;

        LayoutInflater layoutInflater = LayoutInflater.from(ViewAssignedTrips.this);
        mBadReviewPopup=layoutInflater.inflate(R.layout.bad_review_popup,null);

        mBadLanguage = findViewById(R.id.badLanguage);
        mPaymentProblem= findViewById(R.id.paymentProblem);
        mPromiseBreaker = findViewById(R.id.promiseBreaker);
        mPunctualProblem = findViewById(R.id.punctualProblem);
        mOtherProblem = findViewById(R.id.otherProblem);
        mOtherProblemDetails = findViewById(R.id.otherProblemDetails);
        mSubmitProblems = findViewById(R.id.submitProblemsButton);

    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initReviewPopup()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(ViewAssignedTrips.this);

        // Inflate the popup dialog from a layout xml file.
        mReviewPopup = layoutInflater.inflate(R.layout.review_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        mRatingBar = (RatingBar) mReviewPopup.findViewById(R.id.ratingBar);
        mReviewDetails = (EditText) mReviewPopup.findViewById(R.id.userReviewDetails);
        mSubmitReview = mReviewPopup.findViewById(R.id.submitReviewButton);

        mSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float ratingNumber = mRatingBar.getRating();
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
            }
        });

    }
}
