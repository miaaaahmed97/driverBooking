package com.rasai.driverBooking.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.DriverHome;
import com.rasai.driverBooking.R;
import com.rasai.driverBooking.CustomObject.SecurityDeposit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

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

    public void setDriverInfo(Driver driverInfo) {
        this.driverInfo = driverInfo;
    }

    public Driver getDriverInfo() {
        return driverInfo;
    }

    public SecurityDeposit getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(SecurityDeposit securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    class ImageButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_deposit_upload);

        assert getSupportActionBar() != null;   //null check
        setTitle("SECURITY DEPOSIT");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        depositSlipImage = findViewById(R.id.bankDepositSlip);
        registrationButton = (Button) findViewById(R.id.Done);
        mDate = (TextInputEditText) findViewById(R.id.date_field);
        mAmount = (TextInputEditText) findViewById(R.id.amount_field);
        addDepositText = findViewById(R.id.uploadPictureText);
        addDepositText.setOnClickListener(new ImageButtonListener());

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

                Integer amount = Integer.parseInt(mAmount.getText().toString());
                String date = mDate.getText().toString();

                if (amount != null && date.length()>0 && getSecurityDeposit().getDepositImage() !=null ) {
                    getSecurityDeposit().setAmount(amount);
                    getSecurityDeposit().setDepositDate(date);
                    getDriverInfo().setSecurityDeposit(getSecurityDeposit());
                    getDriverInfo().setRating((float) 5.0);

                    String[] imageNames = {getDriverInfo().getCnicImage(), getDriverInfo().getIdImage(),
                            getDriverInfo().getDrivingLicenseImage(), getDriverInfo().getVehicle().getExteriorImage(),
                            getDriverInfo().getVehicle().getInteriorImage(), getSecurityDeposit().getDepositImage()};

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

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            getSecurityDeposit().setDepositImage(uri.toString());
            Log.d("pleasee", getSecurityDeposit().toString());

            Glide.with(SecurityDepositUpload.this)
                    .load(uri)
                    .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                    .into(depositSlipImage);
        }
    }
}
