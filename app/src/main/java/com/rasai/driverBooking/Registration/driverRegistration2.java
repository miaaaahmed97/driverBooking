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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class driverRegistration2 extends AppCompatActivity implements Serializable{

    Intent buttonIntent;
    //ImageDisplayer imageDisplayer;

    private static final int GET_FROM_GALLERY = 1;
    ImageButton addIDButton, addCNICButton, addDrivLicButton;
    Button registrationButton;

    private StorageReference mStorageRef;

    private Driver driverInformation;

    public Driver getDriverInformation() {
        return driverInformation;
    }

    public void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    class ImageButtonListener implements View.OnClickListener, Serializable {
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
        setContentView(R.layout.activity_driver_registration2);

        //get firebase storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        addIDButton = findViewById(R.id.addExterior);
        addCNICButton=findViewById(R.id.addCnic);
        addDrivLicButton = findViewById(R.id.addDrivingLicense);
        registrationButton = (Button) findViewById(R.id.registerDriverButton);

        addIDButton.setOnClickListener(new ImageButtonListener());
        addCNICButton.setOnClickListener(new ImageButtonListener());
        addDrivLicButton.setOnClickListener(new ImageButtonListener());

        class MyOnClickListener implements View.OnClickListener, Serializable {
            @Override
            public void onClick(View view) {
                //Log.d("testing3", driverInformation.toString());

                Intent navNext = new Intent(driverRegistration2.this, vehicleRegistration.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);
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
            Uri selectedImage = data.getData();

            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //image display

                displayImage(bmp, selectedImage);
                //Refactoring try
                //String stringID= buttonIntent.getExtras().getString("EXTRA");
                //imageDisplayer = new ImageDisplayer(getDriverInformation(), R.layout.activity_driver_registration2, this, stringID);
                //imageDisplayer.displayImage(bmp, selectedImage);

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
                imageView = findViewById(R.id.showIDPicture);
                getDriverInformation().setIdImage(uri.toString());
                break;
            case R.id.addCnic:
                imageView = findViewById(R.id.showCNIC);
                getDriverInformation().setCnicImage(uri.toString());
                break;
            case R.id.addDrivingLicense:
                imageView = findViewById(R.id.showDeposit);
                getDriverInformation().setDrivingLicenseImage(uri.toString());
                break;
            default:
                imageView = findViewById(R.id.showDeposit);
                break;
        }
        Log.d("pleasee", getDriverInformation().toString());
        imageView.setImageBitmap(bmp);
    }

}

