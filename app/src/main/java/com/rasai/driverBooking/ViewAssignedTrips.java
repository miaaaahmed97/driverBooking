package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;


public class ViewAssignedTrips extends AppCompatActivity {

    private Button mCompletedTrip, mSubmitReview;
    private View mReviewPopup;
    private RatingBar mRatingBar;
    private EditText mReviewDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assigned_trip);
        setTitle("Assigned Trip");

        mCompletedTrip = findViewById(R.id.completedTripButton);

        //creating the review popup
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Review Your Trip");
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        initPopupViewControls();

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(mReviewPopup);


        mCompletedTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(ViewAssignedTrips.this);

        // Inflate the popup dialog from a layout xml file.
        mReviewPopup = layoutInflater.inflate(R.layout.review_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        mRatingBar = (RatingBar) mReviewPopup.findViewById(R.id.ratingBar);
        mReviewDetails = (EditText) mReviewPopup.findViewById(R.id.userReviewText);
        mSubmitReview = mReviewPopup.findViewById(R.id.submitReviewButton);

    }
}
