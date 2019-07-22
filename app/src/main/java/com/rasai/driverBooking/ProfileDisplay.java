package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.SecurityDeposit;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.Registration.MultiSelectionSpinner;
import com.rasai.driverBooking.TripTabsActivity.AssignedTrips.ViewAssignedTrips;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProfileDisplay extends AppCompatActivity {

    private MultiSelectionSpinner mLangSpinner;
    private Spinner manufacturerSpinner, seatsSpinner;

    private static final int GET_FROM_GALLERY = 1;
    Intent imageIntent;
    private View mSecurityDepositPopup;

    //get current user
    private String phone_Number,StringLangSelected, mHasAC;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private ImageView mIDImage, mSecurityDepositImage;
    private Switch mACSwitch;

    private Button mAddSecurityDeposit;
    private Button mSignOut;
    private Button mEdit;
    private Button mSave;
    private Button mSubmitSecurityPopupButton;



    private TextView mDriverName, mDriverMobile, mDriverCNIC,mDriverDOB, mDriverAddress, mRegField, mSecurityAmount, mModel;
    private TextView mSecurityDepositAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        assert getSupportActionBar() != null;   //null check
        setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        //phone_Number = user.getPhoneNumber();
        phone_Number = "+923105907927";

        mIDImage = findViewById(R.id.userPicture);
        mDriverName = (TextView)findViewById(R.id.driverName);

        mLangSpinner = findViewById(R.id.lang_spinner);
        List<String> languageList = new ArrayList<String>();

        mDriverMobile = findViewById(R.id.driverMobile);
        mDriverCNIC = findViewById(R.id.driverCNIC);
        mDriverDOB = findViewById(R.id.driverDOB);
        mDriverAddress = findViewById(R.id.driverAddress);

        manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        mRegField = findViewById(R.id.registration_field);
        mACSwitch = findViewById(R.id.ac_switch);
        mModel = findViewById(R.id.model_field);
        mAddSecurityDeposit = findViewById(R.id.addSecurityButton);
        mSecurityAmount = findViewById(R.id.securityDepositAmount);

        mEdit = findViewById(R.id.edit);
        mSave = findViewById(R.id.save);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(phone_Number);
        mRef.addValueEventListener(new MyValueEventListener());

        languageList.add("English");
        languageList.add("Urdu");
        languageList.add("Punjabi");
        mLangSpinner.setItems(languageList);

        //Start - Spinner Layout Setup
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(this,
                R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);
        //End - Spinner Layout Setup

        //adding change image code to ID image
        mIDImage.setOnClickListener(new addImageListener());
        //adding select image code to security deposit button
        mAddSecurityDeposit.setOnClickListener(new addImageListener());

        //user chooses to edit
        edit(mEdit);
        //save(mSave);

        mSignOut = (Button) findViewById(R.id.logout);
        signOut(mSignOut);
    }

    //on clicking back button finish activity and go back
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //called automatically after ID image is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //image display
                processImage(bmp, selectedImage);
            } catch (FileNotFoundException e) {
                // T/ODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // T/ODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void processImage(Bitmap bmp, Uri uri) {
        //get ID of calling view
        String stringID= imageIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.userPicture:
                mIDImage.setImageBitmap(bmp);
                //getDriverInformation().setIdImage(uri.toString());
                //todo store in database?
                break;
            case R.id.addSecurityButton:
                //getSecurityDeposit().setDepositImage(uri.toString());
                //creating the review popup
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileDisplay.this);
                alertDialogBuilder.setTitle("Security Deposit");
                alertDialogBuilder.setCancelable(true);
                // Init popup dialog view and it's ui controls.
                initSecurityDepositPopup();
                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(mSecurityDepositPopup);
                mSecurityDepositImage.setImageBitmap(bmp);
                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
            default:
                break;
        }
    }

    private void initSecurityDepositPopup() {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(ProfileDisplay.this);

        // Inflate the popup dialog from a layout xml file.
        mSecurityDepositPopup = layoutInflater.inflate(R.layout.security_deposit_alert_pd, null);
        mSecurityDepositImage = mSecurityDepositPopup.findViewById(R.id.newSecurityDepositImage);
        mSecurityDepositAmount = mSecurityDepositPopup.findViewById(R.id.newSecurityDepositAmount);
        mSubmitSecurityPopupButton = mSecurityDepositPopup.findViewById(R.id.submitNewSecurityButton);

        //todo add a listener for the button
    }

    class addImageListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //calls gallery
            imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent, GET_FROM_GALLERY);
            //get ID of calling button
            String viewID= String.valueOf(view.getId());
            imageIntent.putExtra("EXTRA",viewID);
        }
    }
    class MyValueEventListener implements ValueEventListener, Serializable {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //Log.d("pD",dataSnapshot.toString());
            Driver driver= new Driver();
            driver.setIdImage(dataSnapshot.child("idImage").getValue(String.class));
            driver.setName(dataSnapshot.child("name").getValue(String.class));
            driver.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue(String.class));
            driver.setCnic(dataSnapshot.child("cnic").getValue(String.class));
            driver.setBirthday(dataSnapshot.child("birthday").getValue(String.class));
            driver.setLanguages(dataSnapshot.child("languages").getValue(String.class));
            driver.setAddress(dataSnapshot.child("address").getValue(String.class));
            driver.setVehicle(dataSnapshot.child("vehicle").getValue(Vehicle.class));
            driver.setSecurityDeposit(dataSnapshot.child("securityDeposit").getValue(SecurityDeposit.class));
            //Log.d("vehicle",driver.getVehicle().getManufacturer());

            displayValues(driver);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void displayValues(Driver driver) {
        //todo mIDImage.setImageURI();
        //todo setRating(driver.getRating());
        mDriverName.setText(driver.getName());
        mDriverMobile.setText(driver.getPhoneNumber());
        mDriverCNIC.setText(driver.getCnic());
        mDriverDOB.setText(driver.getBirthday());
        //todo StringLangSelected = mLangSpinner.getSelectedItemsAsString();
        mDriverAddress.setText(driver.getAddress());

        //todo manufacturerSpinner.set
        mModel.setText(driver.getVehicle().getModel());
        mRegField.setText(driver.getVehicle().getRegistration());
        //todo number of seats
        if(driver.getVehicle().getHasAc().contentEquals("yes"))
            mACSwitch.setChecked(true);
        else
            mACSwitch.setChecked(false);
        //todo image display
        mSecurityAmount.setText(driver.getSecurityDeposit().getAmount());
    }

    private void edit(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDriverName.setClickable(true);
                mDriverName.setFocusableInTouchMode(true);
                mDriverName.setEnabled(true);
                mDriverName.setTextColor(getResources().getColor(R.color.black));

                mDriverAddress.setClickable(true);
                mDriverAddress.setFocusableInTouchMode(true);
                mDriverAddress.setEnabled(true);
                mDriverAddress.setTextColor(getResources().getColor(R.color.black));

                mModel.setClickable(true);
                mModel.setFocusableInTouchMode(true);
                mModel.setEnabled(true);
                mModel.setTextColor(getResources().getColor(R.color.black));

                mRegField.setClickable(true);
                mRegField.setFocusableInTouchMode(true);
                mRegField.setEnabled(true);
                mRegField.setTextColor(getResources().getColor(R.color.black));

                //hide edit button and show save button
                mEdit.setVisibility(View.GONE);
                mSave.setVisibility(View.VISIBLE);
            }
        });
    }

    private void save(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDriverName.setClickable(false);
                mDriverName.setFocusable(false);
                mDriverName.setFocusableInTouchMode(false);
                mDriverName.setEnabled(false);
                mDriverName.setTextColor(getResources().getColor(R.color.darkgrey2));

                mDriverAddress.setClickable(false);
                mDriverAddress.setFocusable(false);
                mDriverAddress.setFocusableInTouchMode(false);
                mDriverAddress.setEnabled(false);
                mDriverAddress.setTextColor(getResources().getColor(R.color.darkgrey2));

                mModel.setClickable(false);
                mModel.setFocusable(false);
                mModel.setFocusableInTouchMode(false);
                mModel.setEnabled(false);
                mModel.setTextColor(getResources().getColor(R.color.darkgrey2));

                mRegField.setClickable(false);
                mRegField.setFocusable(false);
                mRegField.setFocusableInTouchMode(false);
                mRegField.setEnabled(false);
                mRegField.setTextColor(getResources().getColor(R.color.darkgrey2));

                //hide save button and show edit button
                mEdit.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.GONE);

                String name = mDriverName.getText().toString();
                String address = mDriverAddress.getText().toString();
                String model = mModel.getText().toString();
                String registration =  mRegField.getText().toString();


                DatabaseReference mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                        .child(phone_Number);
                /*if(name.length()>0){
                    mDriverRef.child("name").setValue(name);
                }
                if(address.length()>0){
                    mDriverRef.child("address").setValue(address);
                }
                if(model.length()>0){
                    mDriverRef.child("vehicle").child("model").setValue(address);
                }
                if(registration.length()>0){
                    mDriverRef.child("vehicle").child("registration").setValue(registration);
                }*/

            }
        });
    }

    private void signOut(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // [START auth_sign_out]
                FirebaseAuth.getInstance().signOut();
                // [END auth_sign_out]

                finish();

                Intent intent = new Intent(ProfileDisplay.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
