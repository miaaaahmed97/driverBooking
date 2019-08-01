package com.rasai.driverBooking.Registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class vehicleRegistration extends AppCompatActivity implements Serializable {


    private Button registrationButton;
    private TextInputEditText mManufacturer;
    private TextInputEditText mModel;
    private TextInputEditText mRegistration;
    private TextInputEditText mNumberSeats;
    private int numberOfSeats;
    //Spinner seatsSpinner;

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

        assert getSupportActionBar() != null;   //null check
        setTitle("VEHICLE REGISTRATION");

        //Start - Spinner Layout Setup
        /*Spinner manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);*/
        //End - Spinner Layout Setup


                mManufacturer = findViewById(R.id.manufacturer_field);
                mModel =  findViewById(R.id.model_field);
                mRegistration =  findViewById(R.id.registration_field);
                mNumberSeats = findViewById(R.id.numberSeats_field);
                Switch toggle =  findViewById(R.id.ac_switch);

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


        //spinner listeners
        //manufacturerSpinner.setOnItemSelectedListener(this);
        //seatsSpinner.setOnItemSelectedListener(this);

    }

    //Start Dropdown Listener
    /*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        new Thread(){
            @Override
            public void run(){
                if(parent.getId() == R.id.manufacturer_field)
                {
                    getVehicleInformation().setManufacturer(parent.getSelectedItem().toString());
                }
                else if(parent.getId() == R.id.seatsSpinner)
                {
                    getVehicleInformation().setVehicleSeats(parent.getSelectedItem().toString());
                }
            }
        };
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    */
    //End Manufacturer Dropdown Listener

    //displays the uploaded image next to the upload icon
    /*public void displayImage(Uri uri){

        ImageView imageView;
        //get ID of calling button
        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.exterior_textview:
                imageView = findViewById(R.id.showExterior);
                getVehicleInformation().setExteriorImage(uri.toString());
                break;
            case  R.id.interior_textview:
                imageView = findViewById(R.id.showInterior);
                getVehicleInformation().setInteriorImage(uri.toString());
                break;
                default:
                    imageView = findViewById(R.id.showInterior);
                    break;

        }
        Log.d("pleasee", getVehicleInformation().toString());
        Glide.with(vehicleRegistration.this)
                .load(uri)
                .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                .into(imageView);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        //Call separate thread to avoid application doing too much work on its main thread
        new Thread(){
            @Override
            public void run(){

                registrationButton =  findViewById(R.id.RegisterVehicle);

                //Register Button Listener
                class MyOnClickListener implements View.OnClickListener, Serializable
                {
                    @Override
                    public void onClick(View view) {
                        //Log.d("testing3", driverInformation.toString());

                        //TODO add check for images

                        String manufacturer = mManufacturer.getText().toString();
                        String model= mModel.getText().toString();
                        String registration = mRegistration.getText().toString();


                        if (manufacturer.length() > 0 && model.length() > 0 && registration.length() > 0 && numberOfSeats>0) {
                            vehicleInformation.setManufacturer(manufacturer);
                            vehicleInformation.setModel(model);
                            vehicleInformation.setRegistration(registration);
                            getDriverInformation().setVehicle(vehicleInformation);

                            Intent navNext = new Intent(vehicleRegistration.this, SecurityDepositUpload.class);
                            navNext.putExtra("driverObject", driverInformation);
                            startActivity(navNext);
                        } else {
                            Toast.makeText(getBaseContext(), "Please provide all info.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                registrationButton.setOnClickListener(new MyOnClickListener());
            }
        }.start();

    }
}
