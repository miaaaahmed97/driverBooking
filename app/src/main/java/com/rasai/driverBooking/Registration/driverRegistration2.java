package com.rasai.driverBooking.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    TextView addIDText, addCNICText, addDrivLicText;
    Button registrationButton;

    private StorageReference mStorageRef;

    private Driver driverInformation;

    public Driver getDriverInformation() {
        return driverInformation;
    }

    public void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    private class TextViewListener implements View.OnClickListener, Serializable {
        @Override
        public void onClick(View v) {

            buttonIntent =new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            //get ID of calling button
            String viewID= String.valueOf(v.getId());
            buttonIntent.putExtra("EXTRA",viewID);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration2);

        //Set tht title
        assert getSupportActionBar() != null;   //null check
        setTitle("IDENTITY CONFIRMATION");

        //get firebase storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        addIDText = findViewById(R.id.addDisplayPicture);
        addIDText.setPaintFlags(addIDText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        addCNICText =findViewById(R.id.addCnic);
        addCNICText.setPaintFlags(addCNICText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        addDrivLicText = findViewById(R.id.addDrivingLicense);
        addDrivLicText.setPaintFlags(addDrivLicText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        registrationButton = findViewById(R.id.registerDriverButton);

        addIDText.setOnClickListener(new TextViewListener());
        addCNICText.setOnClickListener(new TextViewListener());
        addDrivLicText.setOnClickListener(new TextViewListener());

        class MyOnClickListener implements View.OnClickListener, Serializable {
            @Override
            public void onClick(View view) {
                //Log.d("testing3", driverInformation.toString());

                Driver mDriver = getDriverInformation();

                if (mDriver.getCnicImage()!=null && mDriver.getIdImage() !=null
                        && mDriver.getDrivingLicenseImage()!=null) {

                    Intent navNext = new Intent(driverRegistration2.this, vehicleRegistration.class);
                    navNext.putExtra("driverObject", driverInformation);
                    startActivity(navNext);

                } else {

                    Toast.makeText(getBaseContext(), "Please upload all images",
                            Toast.LENGTH_LONG).show();

                }
            }
        }
        registrationButton.setOnClickListener(new MyOnClickListener());
    }

    //called automatically after any button is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode==GET_FROM_GALLERY){
                Uri selectedImage = data.getData();
                //image display
                displayImage(selectedImage);
            }
        }
    }

    //displays the uploaded image next to the upload icon
    public void displayImage(Uri uri){
        ImageView imageView;
        //get ID of calling button
        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.addDisplayPicture:
                imageView = findViewById(R.id.showDisplayPicture);
                getDriverInformation().setIdImage(uri.toString());
                break;
            case R.id.addCnic:
                imageView = findViewById(R.id.showCNIC);
                getDriverInformation().setCnicImage(uri.toString());
                break;
            case R.id.addDrivingLicense:
                imageView = findViewById(R.id.showDrivingLicense);
                getDriverInformation().setDrivingLicenseImage(uri.toString());
                break;
            default:
                imageView = findViewById(R.id.showDrivingLicense);
                break;
        }
        //Log.d("pleasee", getDriverInformation().toString());
        //imageView.setImageURI(uri);

        Glide.with(driverRegistration2.this)
                .load(uri)
                .apply(new RequestOptions().centerInside()
                        .placeholder(R.drawable.rounded_rectangle_grey))
                        .into(imageView);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }*/
    /**
     * Select image from gallery

    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Sets the type as image/*. This ensures only components of type image are selected
        //pickPhoto.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickPhoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickPhoto, GET_FROM_GALLERY);
    }*/

    // navigating user to app settings
    /*
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }*/

}

