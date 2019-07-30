package com.rasai.driverBooking.Registration;

import android.app.Activity;
import android.content.ClipData;
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

public class vehicleRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener,Serializable {

    private Intent buttonIntent;
    private static final int GET_FROM_GALLERY = 1;

    private TextView addExteriorText;
    private TextView addInteriorText;
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

    private class TextViewListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            buttonIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            //get ID of calling button
            String viewID= String.valueOf(v.getId());
            buttonIntent.putExtra("EXTRA",viewID);
            startActivityForResult(Intent.createChooser(buttonIntent,"Select Only 3 Images"), GET_FROM_GALLERY);
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

                addExteriorText.setOnClickListener(new TextViewListener());
                Log.d("showID", String.valueOf(R.id.exterior_textview));
                addInteriorText.setOnClickListener(new TextViewListener());


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
            Uri imageUri;
            //Uri selectedImage = data.getData();
            //displayImage(selectedImage);

            if(data.getClipData() != null) {
                //evaluate the count to display message
                int count = data.getClipData().getItemCount();
                if(count<3){
                    Toast.makeText(getBaseContext(), "Please upload 3 images", Toast.LENGTH_LONG).show();
                }
                else if (count==3){
                    displayMultipleImages(data.getClipData());
                }else{
                    Toast.makeText(getBaseContext(), "Only 3 images will be uploaded", Toast.LENGTH_LONG).show();
                    displayMultipleImages(data.getClipData());
                }

                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
            else if(data.getData() != null) {
            //imageUri = data.getData();
            //do something with the image (save it to some directory or whatever you need to do with it here)
                Toast.makeText(getBaseContext(), "Please upload 3 images", Toast.LENGTH_LONG).show();
        }
        }

    }

    public void displayMultipleImages(ClipData clipData){
        Uri uri;
        ImageView imageview;

        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.exterior_textview:
                for(int i = 0; i < 3; i++){
                    uri = clipData.getItemAt(i).getUri();
                    switch (i){
                        case 0:
                            imageview = findViewById(R.id.showExterior);
                            getVehicleInformation().setExteriorImage(uri.toString());
                            break;
                        case 1:
                            imageview = findViewById(R.id.showExterior2);
                            getVehicleInformation().setExteriorImage2(uri.toString());
                            break;
                        case 2:
                            imageview = findViewById(R.id.showExterior3);
                            getVehicleInformation().setExteriorImage3(uri.toString());
                            break;

                        //to stop may not have been initialized errors
                        default:imageview = findViewById(R.id.showExterior);break;

                    }
                    Glide.with(vehicleRegistration.this)
                            .load(uri)
                            .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                            .into(imageview);
                }

                break;
            case R.id.interior_textview:

                for(int i = 0; i < 3; i++){
                    uri = clipData.getItemAt(i).getUri();
                    switch (i){
                        case 0:
                            imageview = findViewById(R.id.showInterior);
                            getVehicleInformation().setInteriorImage(uri.toString());
                            break;
                        case 1:
                            imageview = findViewById(R.id.showInterior2);
                            getVehicleInformation().setInteriorImage2(uri.toString());
                            break;
                        case 2:
                            imageview = findViewById(R.id.showInterior3);
                            getVehicleInformation().setInteriorImage3(uri.toString());
                            break;

                        //to stop may not have been initialized errors
                        default:imageview = findViewById(R.id.showInterior);break;

                    }
                    Glide.with(vehicleRegistration.this)
                            .load(uri)
                            .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                            .into(imageview);
                }
                break;
        }
    }

    //displays the uploaded image next to the upload icon
    public void displayImage(Uri uri){

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
