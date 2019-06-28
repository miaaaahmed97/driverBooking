package com.rasai.driverBooking.Registration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class vehicleRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener,Serializable {

    Intent buttonIntent;
    private static final int GET_FROM_GALLERY = 1;

    ImageButton addExteriorButton;
    ImageButton addInteriorButton;
    private Button registrationButton;
    private TextInputEditText mModel;
    private TextInputEditText mRegistration;

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

    class ImageButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);
            //get ID of calling button
            String viewID= String.valueOf(v.getId());
            buttonIntent.putExtra("EXTRA",viewID);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        //Start - Spinner Layout Setup
        Spinner manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        Spinner seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(this,
                R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);
        //End - Spinner Layout Setup

        registrationButton = (Button) findViewById(R.id.RegisterVehicle);
        mModel = (TextInputEditText) findViewById(R.id.model_field);
        mRegistration = (TextInputEditText) findViewById(R.id.registration_field);
        Switch toggle = (Switch) findViewById(R.id.ac_switch);
        addExteriorButton = (ImageButton) findViewById(R.id.addExterior);
        addInteriorButton= (ImageButton) findViewById(R.id.addInterior);

        addExteriorButton.setOnClickListener(new ImageButtonListener());
        Log.d("showID", String.valueOf(R.id.addExterior));
        addInteriorButton.setOnClickListener(new ImageButtonListener());

        //Get Driver Object from driverRegistration2
        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        //initialize vehicle
        setVehicleInformation(new Vehicle());

        //spinner listeners
        manufacturerSpinner.setOnItemSelectedListener(this);
        seatsSpinner.setOnItemSelectedListener(this);

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

        //Register Button Listener
        class MyOnClickListener implements View.OnClickListener, Serializable
        {
            @Override
            public void onClick(View view) {
                //Log.d("testing3", driverInformation.toString());

                vehicleInformation.setModel(mModel.getText().toString());
                vehicleInformation.setRegistration(mRegistration.getText().toString());
                getDriverInformation().setVehicle(vehicleInformation);

                Intent navNext = new Intent(vehicleRegistration.this, SecurityDepositUpload.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);
            }
        }
        registrationButton.setOnClickListener(new MyOnClickListener());

    }

    //Start Dropdown Listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        if(parent.getId() == R.id.manufacturerSpinner)
        {
            getVehicleInformation().setManufacturer(parent.getSelectedItem().toString());
        }
        else if(parent.getId() == R.id.seatsSpinner)
        {
            getVehicleInformation().setVehicleSeats(parent.getSelectedItem().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    //End Manufacturer Dropdown Listener

    //called automatically after any button is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //image display
                displayImage(bmp, selectedImage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //displays the uploaded image next to the upload icon
    public void displayImage(Bitmap bmp, Uri uri){
        ImageView imageView;
        //get ID of calling button
        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.addExterior:
                imageView = findViewById(R.id.showExterior);
                getVehicleInformation().setExteriorImage(uri.toString());
                break;
            case R.id.addInterior:
                imageView = findViewById(R.id.showInterior);
                getVehicleInformation().setInteriorImage(uri.toString());
                break;
            default:
                imageView = findViewById(R.id.showInterior);
                break;
        }
        Log.d("pleasee", getVehicleInformation().toString());
        imageView.setImageBitmap(bmp);
    }

}
