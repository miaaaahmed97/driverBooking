package com.rasai.driverBooking.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.Objects;

public class driverRegistration2 extends AppCompatActivity implements Serializable{

    private Intent buttonIntent;

    private static final int GET_FROM_GALLERY = 1;

    private Button addIDButton, addCNICButton, addDrivLicButton;
    private Button registrationButton;
    private Button mAddImageButton, mDoneButton;
    private View mAddImagePopup;
    private ImageView mSelectedImage;
    private AlertDialog alertDialog;
    private int callingView;
    private TextView mHelpingText;

    private StorageReference mStorageRef;

    private Driver driverInformation;

    private Driver getDriverInformation() {
        return driverInformation;
    }

    private void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    private class addImageButtonListener implements View.OnClickListener, Serializable {
        @Override
        public void onClick(View v) {

            buttonIntent =new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String[] mimeTypes = {"image/jpeg", "image/png"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            //get ID of calling button
            //String viewID= String.valueOf(v.getId());
            //buttonIntent.putExtra("EXTRA",viewID);
            startActivityForResult(buttonIntent, GET_FROM_GALLERY);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration2);

        //hide the bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //get firebase storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        //set the progress
        StateProgressBar stateProgressBar = findViewById(R.id.simpleProgressBar);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        stateProgressBar.enableAnimationToCurrentState(true);
        stateProgressBar.setAnimationDuration(2000);

        addIDButton = findViewById(R.id.addIDPicture);
        addCNICButton =findViewById(R.id.addCnic);
        addDrivLicButton = findViewById(R.id.addDrivingLicense);
        registrationButton = findViewById(R.id.registerDriverButton);

        addIDButton.setOnClickListener(new createPopupListener());
        addCNICButton.setOnClickListener(new createPopupListener());
        addDrivLicButton.setOnClickListener(new createPopupListener());

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

    private class createPopupListener implements View.OnClickListener, Serializable{

        @Override
        public void onClick(View view) {

            //creating the picture popup
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(driverRegistration2.this);
            alertDialogBuilder.setCancelable(true);
            callingView = view.getId();
            initAddImagePopup();
            Log.d("IDDDD",String.valueOf(view.getId()));
            switch (callingView){
                case R.id.addIDPicture:
                    // Init popup dialog view and it's ui controls.
                    alertDialogBuilder.setTitle("Add ID Image");
                    break;
                case R.id.addCnic:
                    alertDialogBuilder.setTitle("Add CNIC Image");
                    break;
                case R.id.addDrivingLicense:
                    alertDialogBuilder.setTitle("Add Driving License Image");
                    break;
                default:break;
            }
            // Set the inflated layout view object to the AlertDialog builder.
            alertDialogBuilder.setView(mAddImagePopup);
            // Create AlertDialog and show.
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }



    private void initAddImagePopup() {
        LayoutInflater layoutInflater = LayoutInflater.from(driverRegistration2.this);
        mAddImagePopup = layoutInflater.inflate(R.layout.add_picture_popup_reg,null);
        mSelectedImage = mAddImagePopup.findViewById(R.id.imageSelected);
        mHelpingText = mAddImagePopup.findViewById(R.id.helpingText);
        Log.d("IDDDD in init",String.valueOf(callingView));
        switch (callingView){
            case R.id.addIDPicture:
                mHelpingText.setText("Please upload a photo of yourself that clearly shows your face.");
                if(getDriverInformation().getIdImage()==null){
                    mSelectedImage.setImageResource((R.drawable.man));
                }
                else{
                    Glide.with(driverRegistration2.this).
                            load(Uri.parse(getDriverInformation().getIdImage()))
                            .apply(new RequestOptions().centerInside()
                                    .placeholder(R.drawable.ic_avatar))
                            .into(mSelectedImage);
                }
                break;
            case R.id.addCnic:
                mHelpingText.setText("Please upload a properly focused image of your CNIC.");
                if(getDriverInformation().getCnicImage()==null){
                    mSelectedImage.setImageResource((R.drawable.cnic));
                }
                else{
                    Glide.with(driverRegistration2.this).
                            load(Uri.parse(getDriverInformation().getCnicImage()))
                            .apply(new RequestOptions().centerInside()
                                    .placeholder(R.drawable.ic_image))
                            .into(mSelectedImage);
                }
                break;
            case R.id.addDrivingLicense:
                mHelpingText.setText("Please upload a properly focused image of your driving license");
                if(getDriverInformation().getDrivingLicenseImage()==null){
                    mSelectedImage.setImageResource((R.drawable.driving_license));
                }
                else{
                    Glide.with(driverRegistration2.this).
                            load(Uri.parse(getDriverInformation().getDrivingLicenseImage()))
                            .apply(new RequestOptions().centerInside()
                                    .placeholder(R.drawable.ic_image))
                            .into(mSelectedImage);
                }
                break;
                default:
                    break;

        }
        mAddImageButton = mAddImagePopup.findViewById(R.id.addImageButton);
        mDoneButton = mAddImagePopup.findViewById(R.id.doneButton);
        mAddImageButton.setOnClickListener(new addImageButtonListener());
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
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
                Glide.with(driverRegistration2.this).
                        load(selectedImage)
                        .apply(new RequestOptions().centerInside()
                                .placeholder(R.drawable.ic_image))
                        .into(mSelectedImage);
                Log.d("IDDDD in oAR",String.valueOf(callingView));
                switch (callingView){
                    case R.id.addIDPicture:
                        getDriverInformation().setIdImage(selectedImage.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            addIDButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.green));
                        }
                        break;
                    case R.id.addCnic:
                        getDriverInformation().setCnicImage(selectedImage.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            addCNICButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.green));
                        }
                        break;
                    case R.id.addDrivingLicense:
                        getDriverInformation().setDrivingLicenseImage(selectedImage.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            addDrivLicButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.green));
                        }
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /*** Showing Alert Dialog with Settings option
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
    /***Select image from gallery

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

