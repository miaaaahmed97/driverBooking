package com.rasai.driverBooking.Registration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.Objects;

public class vehicleRegistration extends AppCompatActivity implements Serializable {

    private Button registrationButton;
    private TextInputEditText mManufacturer;
    private TextInputEditText mModel;
    private TextInputEditText mRegistration;
    private TextInputEditText mNumberSeats;
    private int numberOfSeats;

    private Vehicle vehicleInformation;
    private Driver driverInformation;

    public Vehicle getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(Vehicle vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }

    public Driver getDriverInformation() {
        return driverInformation;
    }

    public void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        Objects.requireNonNull(getSupportActionBar()).hide();
        //set the progress
        StateProgressBar stateProgressBar = findViewById(R.id.simpleProgressBar);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        stateProgressBar.enableAnimationToCurrentState(true);
        stateProgressBar.setAnimationDuration(2000);

        mManufacturer = findViewById(R.id.manufacturer_field);
        mModel =  findViewById(R.id.model_field);
        mRegistration =  findViewById(R.id.registration_field);
        Switch toggle =  findViewById(R.id.ac_switch);
        mNumberSeats = findViewById(R.id.numberSeats_field);
        registrationButton =  findViewById(R.id.RegisterVehicle);

                mNumberSeats.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // setup the alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(vehicleRegistration.this);
                        builder.setTitle("Choose Number of Seats");

                        // add a checkbox list
                        String[] seatNumbersArray = getResources().getStringArray(R.array.seats_array);
                        builder.setSingleChoiceItems(seatNumbersArray, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:numberOfSeats=4;break;
                                    case 1:numberOfSeats=5; break;
                                    case 2: numberOfSeats=6;break;
                                    case 3: numberOfSeats=7;break;
                                    case 4:numberOfSeats=8; break;
                                    case 5: numberOfSeats=9;break;
                                    case 6:numberOfSeats=10; break;
                                    case 7: numberOfSeats=11;break;
                                    case 8: numberOfSeats=12;break;
                                    case 9: numberOfSeats=13;break;
                                    case 10:numberOfSeats=14; break;
                                    case 11: numberOfSeats=15;break;
                                    case 12: numberOfSeats=16;break;
                                }

                            }
                        });

                        // add OK and Cancel buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user clicked OK
                                mNumberSeats.setText(String.valueOf(numberOfSeats));
                            }
                        });
                        builder.setNegativeButton("Cancel", null);

                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

        //Get Driver Object from driverRegistration2
        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        //initialize vehicle
        setVehicleInformation(new Vehicle());

        //Strat AC switch listener
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // The toggle is enabled
                    getVehicleInformation().setHasAc("yes");

                } else {

                    // The toggle is disabled
                    getVehicleInformation().setHasAc("no");

                }
            }
        });
        //End AC switch listener
    }

    @Override
    public void onStart() {
        super.onStart();

                //Register Button Listener
                class MyOnClickListener implements View.OnClickListener, Serializable
                {
                    @Override
                    public void onClick(View view) {

                        String manufacturer = mManufacturer.getText().toString();
                        String model= mModel.getText().toString();
                        String registration = mRegistration.getText().toString();
                        String seats = String.valueOf(numberOfSeats);


                        if (manufacturer.length() > 0 && model.length() > 0 && registration.length() > 0 && numberOfSeats>0) {
                            vehicleInformation.setManufacturer(manufacturer);
                            vehicleInformation.setModel(model);
                            vehicleInformation.setRegistration(registration);
                            vehicleInformation.setVehicleSeats(seats);

                            Intent navNext = new Intent(vehicleRegistration.this, vehicleRegistration2.class);
                            navNext.putExtra("driverObject", driverInformation);
                            navNext.putExtra("vehicleObject", vehicleInformation);
                            startActivity(navNext);
                        } else {
                            Toast.makeText(getBaseContext(), "Please provide all info.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                registrationButton.setOnClickListener(new MyOnClickListener());
    }
}
