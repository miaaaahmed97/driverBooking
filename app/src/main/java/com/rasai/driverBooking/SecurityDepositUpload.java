package com.rasai.driverBooking;

import androidx.annotation.NonNull;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class SecurityDepositUpload extends AppCompatActivity {

    Intent buttonIntent;
    private static final int GET_FROM_GALLERY = 1;

    private StorageReference mStorageRef;
    FirebaseDatabase database;

    ImageButton addDepositButton;
    private Button registrationButton;
    private TextInputEditText mDate;
    private TextInputEditText mAmount;

    private Driver driverInformation;
    private SecurityDeposit securityDeposit;

    Uri testURI;

    public void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    public Driver getDriverInformation() {
        return driverInformation;
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

        registrationButton = (Button) findViewById(R.id.Done);
        mDate = (TextInputEditText) findViewById(R.id.date_field);
        mAmount = (TextInputEditText) findViewById(R.id.amount_field);
        addDepositButton = (ImageButton) findViewById(R.id.addSlip);

        addDepositButton.setOnClickListener(new ImageButtonListener());

        //Get Driver Object from driverRegistration2
        Intent i = getIntent();
        setDriverInformation((Driver) i.getSerializableExtra("driverObject"));

        //initialize vehicle
        setSecurityDeposit(new SecurityDeposit());

        //Register Button Listener
        class MyOnClickListener implements View.OnClickListener, Serializable
        {
            @Override
            public void onClick(View view) {
                //Log.d("testing3", driverInformation.toString());

                getSecurityDeposit().setAmount(mAmount.getText().toString());
                getSecurityDeposit().setDepositDate(mDate.getText().toString());
                getDriverInformation().setSecurityDeposit(getSecurityDeposit());

                /*mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rasai-3c730.appspot.com");
                database = FirebaseDatabase.getInstance();

                StorageReference ref = mStorageRef.child("Driver/");
                ref.putFile(testURI);
                       /* .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                /*final StorageReference storageRef = mStorageRef.child("Driver/"+getDriverInformation().getPhoneNumber()+"CNIC");
                UploadTask uploadTask = storageRef.putFile(Uri.parse(getDriverInformation().getCnicImage()));

                //upload image to firebase
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //getDriverInformation().setCnicImage(taskSnapshot.getMetadata().toString());
                        // ...
                        Log.d("checking", "addedd succcessfully");
                    }
                });*/

                //getDriverInformation().storeDriverImage(mStorageRef, "cnicImage", Uri.parse(getDriverInformation().getCnicImage()));

                DatabaseReference mDriver = database.getReference();
                getDriverInformation().postDriverInfo(mDriver);

                Intent navNext = new Intent(SecurityDepositUpload.this, DriverHome.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);
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
