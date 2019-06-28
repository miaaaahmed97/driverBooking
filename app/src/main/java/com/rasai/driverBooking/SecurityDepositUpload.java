package com.rasai.driverBooking;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class SecurityDepositUpload extends AppCompatActivity {

    Intent buttonIntent;
    private static final int GET_FROM_GALLERY = 1;

    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageButton addDepositButton;
    private Button registrationButton;
    private TextInputEditText mDate;
    private TextInputEditText mAmount;

    private Driver driverInfo;
    private SecurityDeposit securityDeposit;

    Uri testURI;

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
            buttonIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_deposit_upload);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        registrationButton = (Button) findViewById(R.id.Done);
        mDate = (TextInputEditText) findViewById(R.id.date_field);
        mAmount = (TextInputEditText) findViewById(R.id.amount_field);
        addDepositButton = (ImageButton) findViewById(R.id.addSlip);

        addDepositButton.setOnClickListener(new ImageButtonListener());

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

                getSecurityDeposit().setAmount(mAmount.getText().toString());
                getSecurityDeposit().setDepositDate(mDate.getText().toString());
                getDriverInfo().setSecurityDeposit(getSecurityDeposit());

                String[] imageNames = {getDriverInfo().getCnicImage(), getDriverInfo().getIdImage(),
                        getDriverInfo().getDrivingLicenseImage(), getDriverInfo().getVehicle().getExteriorImage(),
                getDriverInfo().getVehicle().getInteriorImage(), getSecurityDeposit().getDepositImage()};

                if (imageNames.length ==6){
                    getDriverInfo().uploadImage(storageReference, imageNames);

                    DatabaseReference mDriver = database.getReference();
                    getDriverInfo().postDriverInfo(mDriver);

                    Intent navNext = new Intent(SecurityDepositUpload.this, DriverHome.class);
                    navNext.putExtra("driverObject", driverInfo);
                    startActivity(navNext);
                }else{
                    Log.d("MyError", "select all images");
                    Toast.makeText(SecurityDepositUpload.this, "Please Upload all Images ", Toast.LENGTH_SHORT).show();
                }
            }
        }
        registrationButton.setOnClickListener(new MyOnClickListener());
    }

    public void displayImage(Bitmap bmp, Uri uri){
        ImageView imageView;
        testURI = uri;
        imageView = findViewById(R.id.showDeposit);
        getSecurityDeposit().setDepositImage(uri.toString());
        Log.d("pleasee", getSecurityDeposit().toString());
        imageView.setImageBitmap(bmp);
    }

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
}
