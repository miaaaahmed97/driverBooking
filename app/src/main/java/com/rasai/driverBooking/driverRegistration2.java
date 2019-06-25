package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class driverRegistration2 extends AppCompatActivity implements Serializable {

    Intent buttonIntent;

    private static final int GET_FROM_GALLERY = 1;
    ImageButton addIDButton, addCNICButton, addDrivLicButton;

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
        setContentView(R.layout.activity_driver_registration2);

        Intent i = getIntent();
        final Driver driverInformation = (Driver) i.getSerializableExtra("driverObject");

        addIDButton = findViewById(R.id.addIDPicture);
        addCNICButton=findViewById(R.id.addCnic);
        addDrivLicButton = findViewById(R.id.addDrivingLicense);

        addIDButton.setOnClickListener(new ImageButtonListener());
        Log.d("showID", String.valueOf(R.id.addCnic));
        addCNICButton.setOnClickListener(new ImageButtonListener());
        addDrivLicButton.setOnClickListener(new ImageButtonListener());
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
                displayImage(bmp);

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
    public void displayImage(Bitmap bmp){
        ImageView imageView;
        //get ID of calling button
        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.addIDPicture:
                imageView = findViewById(R.id.showIDPicture);
                break;
            case R.id.addCnic:
                imageView = findViewById(R.id.showCNIC);
                break;
            case R.id.addDrivingLicense:
                imageView = findViewById(R.id.showDrivingLicense);
                break;
            default:
                imageView = findViewById(R.id.showDrivingLicense);
                break;
        }
        imageView.setImageBitmap(bmp);

    }

    public void registerDriver(View v){
        if (v.getId()==R.id.registerDriverButton) {
            Intent i = new Intent(driverRegistration2.this, vehicleRegistration.class);
            startActivity(i);

        }

    }
}

