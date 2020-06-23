package com.dryver.driverBooking.Registration;

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
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.dryver.driverBooking.CustomObject.Driver;
import com.dryver.driverBooking.CustomObject.Vehicle;
import com.dryver.driverBooking.DriverHome;
import com.dryver.driverBooking.R;
import com.dryver.driverBooking.CustomObject.SecurityDeposit;

import java.io.Serializable;
import java.util.Objects;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SecurityDepositUpload extends AppCompatActivity {

    private Intent buttonIntent;
    private static final int GET_FROM_GALLERY = 1;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private TextView addDepositText;
    private ImageView depositSlipImage;
    private Button registrationButton;
    private TextInputEditText mDate;
    private TextInputEditText mAmount;

    private Driver driverInfo;
    private SecurityDeposit securityDeposit;

    private int totalImages = 10;

    private void setDriverInfo(Driver driverInfo) {
        this.driverInfo = driverInfo;
    }

    private Driver getDriverInfo() {
        return driverInfo;
    }

    private SecurityDeposit getSecurityDeposit() {
        return securityDeposit;
    }

    private void setSecurityDeposit(SecurityDeposit securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    private class ImageButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_deposit_upload);

        //hise top bar and set value of progress bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //set the progress
        StateProgressBar stateProgressBar = findViewById(R.id.simpleProgressBar);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
        stateProgressBar.enableAnimationToCurrentState(true);
        stateProgressBar.setAnimationDuration(2000);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        depositSlipImage = findViewById(R.id.bankDepositSlip);
        registrationButton =  findViewById(R.id.Done);
        mDate =  findViewById(R.id.date_field);
        mAmount =  findViewById(R.id.amount_field);
        addDepositText = findViewById(R.id.uploadPictureText);
        depositSlipImage.setOnClickListener(new ImageButtonListener());

        //Get Driver Object from driverRegistration2
        Intent i = getIntent();
        setDriverInfo((Driver) i.getSerializableExtra("driverObject"));

        //initialize vehicle
        setSecurityDeposit(new SecurityDeposit());

        //Register Button Listener
        class MyOnClickListener implements View.OnClickListener, Serializable
        {
            @Override
            public void onClick(View view) {
                //Log.d("testing3", driverInfo.toString());

                //Integer amount = Integer.parseInt(mAmount.getText().toString());
                String date = mDate.getText().toString();

                if (mAmount.getText().toString().length()>0 && date.length()>0 && getSecurityDeposit().getDepositImage() !=null ) {
                    getSecurityDeposit().setAmount(Integer.parseInt(mAmount.getText().toString()));
                    getSecurityDeposit().setDepositDate(date);
                    getDriverInfo().setSecurityDeposit(getSecurityDeposit());
                    getDriverInfo().setRating((float) 5.0);

                    Vehicle vehicle = getDriverInfo().getVehicle();

                    String[] imageNames = {getDriverInfo().getCnicImage(), getDriverInfo().getIdImage(),
                            getDriverInfo().getDrivingLicenseImage(), vehicle.getFrontviewImage(),
                            vehicle.getBackviewImage(), vehicle.getSideviewImage(), vehicle.getSeatsImage(),
                            vehicle.getInteriorImage1(), vehicle.getInteriorImage2(), getSecurityDeposit().getDepositImage()};

                    getDriverInfo().uploadImage(storageReference, imageNames);

                    Intent navNext = new Intent(SecurityDepositUpload.this, DriverHome.class);
                    navNext.putExtra("driverObject", driverInfo);
                    startActivity(navNext);

                } else {
                    Toast.makeText(SecurityDepositUpload.this, "Please fill all info ", Toast.LENGTH_SHORT).show();
                }

                /*String[] imageNames = {getDriverInfo().getCnicImage(), getDriverInfo().getIdImage(),
                        getDriverInfo().getDrivingLicenseImage(), getDriverInfo().getVehicle().getExteriorImage(),
                getDriverInfo().getVehicle().getInteriorImage(), getSecurityDeposit().getDepositImage()};

                if (imageNames.length ==6){
                    getDriverInfo().uploadImage(storageReference, imageNames);

                    Intent navNext = new Intent(SecurityDepositUpload.this, DriverHome.class);
                    navNext.putExtra("driverObject", driverInfo);
                    startActivity(navNext);
                }else{
                    Log.d("MyError", "select all images");
                    Toast.makeText(SecurityDepositUpload.this, "Please Upload all Images ", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
        registrationButton.setOnClickListener(new MyOnClickListener());
    }

    //called automatically after any button is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int radius = 15; // corner radius, higher value = more rounded
        int margin = 0; // crop margin, set to 0 for corners with no crop
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            getSecurityDeposit().setDepositImage(uri.toString());
            Log.d("pleasee", getSecurityDeposit().toString());

            Glide.with(SecurityDepositUpload.this)
                    .load(uri)
                    .apply(new RequestOptions().centerInside().transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.ic_car))
                    .into(depositSlipImage);
        }
    }
}
