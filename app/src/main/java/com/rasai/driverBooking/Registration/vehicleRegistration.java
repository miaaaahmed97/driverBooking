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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

    TextView addExteriorText;
    TextView addInteriorText;
    private Button registrationButton;
    private TextInputEditText mManufacturer;
    private TextInputEditText mModel;
    private TextInputEditText mRegistration;
    Spinner seatsSpinner;

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

        assert getSupportActionBar() != null;   //null check
        setTitle("VEHICLE REGISTRATION");

        //Start - Spinner Layout Setup
        /*Spinner manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);*/

        seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);
        //End - Spinner Layout Setup


                mManufacturer = findViewById(R.id.manufacturer_field);
                mModel =  findViewById(R.id.model_field);
                mRegistration =  findViewById(R.id.registration_field);
               Switch toggle =  findViewById(R.id.ac_switch);
                addExteriorText =  findViewById(R.id.exterior_textview);
                addInteriorText =  findViewById(R.id.interior_textview);

                addExteriorText.setOnClickListener(new ImageButtonListener());
                Log.d("showID", String.valueOf(R.id.exterior_textview));
                addInteriorText.setOnClickListener(new ImageButtonListener());


                //Get Driver Object from driverRegistration2
                Intent i = getIntent();
                setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

                //initialize vehicle
                setVehicleInformation(new Vehicle());


        //spinner listeners
        //manufacturerSpinner.setOnItemSelectedListener(this);
        seatsSpinner.setOnItemSelectedListener(this);

    }

    //Start Dropdown Listener
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

        new Thread(){
            @Override
            public void run(){

                ImageView imageView;
                //get ID of calling button
                String stringID= buttonIntent.getExtras().getString("EXTRA");
                int intID =Integer.parseInt(stringID);
                switch (intID){
                    case R.id.exterior_textview:
                        //todo show images in remaining showExterior images
                        imageView = findViewById(R.id.showExterior);
                        getVehicleInformation().setExteriorImage(uri.toString());
                        break;
                    case R.id.interior_textview:
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
        };
    }

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

                        if (manufacturer.length() > 0 && model.length() > 0 && registration.length() > 0) {
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
