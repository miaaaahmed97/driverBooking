package com.rasai.driverBooking.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.Objects;

public class vehicleRegistration2 extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private ImageView mExterior1, mExterior2, mExterior3, mInterior1, mInterior2, mInterior3;
    private Intent buttonIntent;
    private Button mRegisterVehicle2Button;

    private Vehicle vehicleInfo;
    private Driver driverInformation;

    int radius = 15; // corner radius, higher value = more rounded
    int margin = 0; // crop margin, set to 0 for corners with no crop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration2);


        Objects.requireNonNull(getSupportActionBar()).hide();
        //set the progress
        StateProgressBar stateProgressBar = findViewById(R.id.simpleProgressBar);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        stateProgressBar.enableAnimationToCurrentState(true);
        stateProgressBar.setAnimationDuration(2000);

        Intent i = getIntent();
        driverInformation = (Driver) i.getSerializableExtra("driverObject");
        vehicleInfo = (Vehicle) i.getSerializableExtra("vehicleObject");

        mRegisterVehicle2Button = findViewById(R.id.RegisterVehicle2Button);
        mRegisterVehicle2Button.setOnClickListener(new MyOnClickListener());

        mExterior1 = findViewById(R.id.showExterior);
        mExterior2 = findViewById(R.id.showExterior2);
        mExterior3 = findViewById(R.id.showExterior3);
        mInterior1 = findViewById(R.id.showInterior);
        mInterior2 = findViewById(R.id.showInterior2);
        mInterior3 = findViewById(R.id.showInterior3);

        mExterior1.setOnClickListener(new ImageViewListener());
        mExterior2.setOnClickListener(new ImageViewListener());
        mExterior3.setOnClickListener(new ImageViewListener());
        mInterior1.setOnClickListener(new ImageViewListener());
        mInterior2.setOnClickListener(new ImageViewListener());
        mInterior3.setOnClickListener(new ImageViewListener());

    }

    private class ImageViewListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //buttonIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            //get ID of calling button
            String viewID= String.valueOf(v.getId());
            buttonIntent.putExtra("EXTRA",viewID);
            startActivityForResult(Intent.createChooser(buttonIntent,"Select an Image"), GET_FROM_GALLERY);
        }
    }
    //called automatically after any button is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            displayImage(selectedImage);
        }

    }

    private void displayImage(Uri uri) {
        ImageView imageview;

        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);

        switch (intID){
            case R.id.showExterior:
                imageview = mExterior1;
                vehicleInfo.setFrontviewImage(uri.toString());
                break;
            case R.id.showExterior2:
                imageview = mExterior2;
                vehicleInfo.setBackviewImage(uri.toString());
                break;
            case R.id.showExterior3:
                imageview = mExterior3;
                vehicleInfo.setSideviewImage(uri.toString());
                break;
            case R.id.showInterior:
                imageview = mInterior1;
                vehicleInfo.setSeatsImage(uri.toString());
                break;
            case R.id.showInterior2:
                imageview = mInterior2;
                vehicleInfo.setInteriorImage1(uri.toString());
                break;
            case R.id.showInterior3:
                imageview = mInterior3;
                vehicleInfo.setInteriorImage2(uri.toString());
                break;

                default:
                    imageview = mExterior1;
                    break;
        }

        new RequestOptions();
        Glide.with(vehicleRegistration2.this)
                .load(uri)
                .centerCrop()
                .into(imageview);

    }

    class MyOnClickListener implements View.OnClickListener, Serializable
    {
        @Override
        public void onClick(View view) {

            if (vehicleInfo.getFrontviewImage()!=null && vehicleInfo.getBackviewImage()!=null &&
                    vehicleInfo.getSideviewImage()!=null && vehicleInfo.getSeatsImage()!=null &&
                    vehicleInfo.getInteriorImage1()!=null && vehicleInfo.getInteriorImage2()!=null)
            {

                driverInformation.setVehicle(vehicleInfo);

                Log.d("VehicleRegistration2", driverInformation.toString());

                Intent navNext = new Intent(vehicleRegistration2.this, SecurityDepositUpload.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);

            } else {
                Toast.makeText(getBaseContext(), "Please provide all info.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
