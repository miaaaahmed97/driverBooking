package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class vehicleRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        Spinner manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this, R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        Spinner seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(this,
        R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);



    }
}
