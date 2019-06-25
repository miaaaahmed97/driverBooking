package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class driverRegistration extends AppCompatActivity {
    private Spinner lSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

    }
    public void goToVehicleReg(View v){
        if (v.getId()==R.id.continueDriverReg) {
            Intent i = new Intent(driverRegistration.this, driverRegistration2.class);
            startActivity(i);

        }

    }



}