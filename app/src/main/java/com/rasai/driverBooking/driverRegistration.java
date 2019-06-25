package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class driverRegistration extends AppCompatActivity implements Serializable {

    private Spinner lSpinner;
    private TextInputEditText mCnic;
    private TextInputEditText mBday;
    private TextInputEditText mAddress;
    private Spinner mLangSpinner;
    private TextView mLangSelected;
    private Button mnextButton;

    Driver driverInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        driverInformation = new Driver();

        mnextButton = (Button) findViewById(R.id.continueDriverReg);
        mnextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set tripInformation
                driverInformation.setCnic(mCnic.toString());
                driverInformation.setBirthday(mBday.toString());
                driverInformation.setAddress(mAddress.toString());

                Intent navNext = new Intent(driverRegistration.this, driverRegistration2.class);
                navNext.putExtra("driverObject", (Serializable) driverInformation);
                startActivity(navNext);
            }
        });

    }
    /*public void goToVehicleReg(View v){
        if (v.getId()==R.id.continueDriverReg) {
            Intent i = new Intent(driverRegistration.this, driverRegistration2.class);
            startActivity(i);

        }

    }*/



}