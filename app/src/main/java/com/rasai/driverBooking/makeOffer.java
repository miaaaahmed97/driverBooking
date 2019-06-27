package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class makeOffer extends AppCompatActivity {

    private Button mDone, mChangeBudget, mMakeOffer;
    TextInputEditText mBudget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        mBudget = (TextInputEditText) findViewById(R.id.offeredBudget);


        mMakeOffer = findViewById(R.id.makeOfferButton);
        mChangeBudget = findViewById(R.id.changeBudgetButton);
        mDone = findViewById(R.id.doneBudgetButton);
        //hide change budget and done buttons
        mChangeBudget.setVisibility(View.GONE);
        mDone.setVisibility(View.GONE);


    }

    public void makeBudgetOffer(View v) {
        if (v.getId() == R.id.makeOfferButton) {
            //show change budget and done button and hide make offer.
            mChangeBudget.setVisibility(View.VISIBLE);
            mDone.setVisibility(View.VISIBLE);
            mMakeOffer.setVisibility(View.GONE);
            //make budget fixed text
            mBudget.setFocusableInTouchMode(false);
        }
    }

    public void changeBudgetOffer(View v) {
        if (v.getId() == R.id.changeBudgetButton) {
            //show done button
            mChangeBudget.setVisibility(View.GONE);
            mDone.setVisibility(View.GONE);
            mMakeOffer.setVisibility(View.VISIBLE);

            //make budget editable
            mBudget.setFocusableInTouchMode(true);

            /*textView.setFocusable(false);
            textView.setEnabled(false);
            textView.setCursorVisible(false);
            listener=textView.getKeyListener();
            textView.setKeyListener(null);
            */

        }
    }

    public void doneOffer(View v){
        //go to next page
    }
}
