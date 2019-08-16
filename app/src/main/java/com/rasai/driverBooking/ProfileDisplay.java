package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.SecurityDeposit;
import com.rasai.driverBooking.CustomObject.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class ProfileDisplay extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private Intent imageIntent;
    private View mSecurityDepositPopup;

    //get current user
    private String phone_Number, mHasAC;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private ImageView mIDImage, mSecurityDepositImage;
    private Switch mACSwitch;

    private Button mAddSecurityDeposit;
    private Button mSignOut;
    private Button mEdit;
    private Button mSave;
    private Button mSubmitSecurityPopupButton;

    //initializing the popup listener to be added to the languages textview on clicking edit button
    private LanguagePopupListener languagePopupListener = new LanguagePopupListener();
    private SeatsPopupListener seatsPopupListener = new SeatsPopupListener();

    private Uri DepositUri;

    private TextView mDriverName, mDriverMobile, mDriverCNIC,mDriverDOB, mDriverAddress,
            mDriverLanguages, mManufacturer, mModel,mRegField, mNumberOfseats, mSecurityAmount;
    private int numberOfSeats;

    private Driver driver;
    private ArrayList<String> mDriverLang = new ArrayList<>();

    int languageCounter = 0;

    private TextView mSecurityDepositAmount;
    private AlertDialog alertDialog;

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
            driver= new Driver();
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

            setWidgets();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        assert getSupportActionBar() != null;   //null check
        setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        phone_Number = user.getPhoneNumber();

        mIDImage = findViewById(R.id.userPicture);
        mDriverName = findViewById(R.id.driverName);
        mDriverMobile = findViewById(R.id.driverMobile);
        mDriverCNIC = findViewById(R.id.driverCNIC);
        mDriverDOB = findViewById(R.id.driverDOB);
        mDriverAddress = findViewById(R.id.driverAddress);
        mDriverLanguages = findViewById(R.id.languagesText);
        mManufacturer =  findViewById(R.id.manufacturer_field);
        mModel = findViewById(R.id.model_field);
        mRegField = findViewById(R.id.registration_field);
        mNumberOfseats = findViewById(R.id.numberSeats_field);
        mACSwitch = findViewById(R.id.ac_switch);
        mAddSecurityDeposit = findViewById(R.id.addSecurityButton);
        mSecurityAmount = findViewById(R.id.securityDepositAmount);

        mEdit = findViewById(R.id.edit);
        mSave = findViewById(R.id.save);
        mSignOut =  findViewById(R.id.logout);

        /*
        * Database reference to retrieve information about the logged in driver
        * */
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(phone_Number);
        mRef.addValueEventListener(new MyValueEventListener());

        //adding change image code to ID image
        mIDImage.setOnClickListener(new addImageListener());

        //adding select image code to security deposit button
        mAddSecurityDeposit.setOnClickListener(new addImageListener());

        displayProfilePicture(this);

        //user chooses to edit
        edit(mEdit);
        save(mSave);

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
            processImage(selectedImage);

        }

    }

    private void processImage(Uri uri) {
        //get ID of calling view
        String stringID= imageIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.userPicture:
                //mIDImage.setImageBitmap(bmp);
                uploadProfilePicture(uri);
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
                DepositUri = uri;
                Glide.with(ProfileDisplay.this)
                        .load(uri)
                        .apply(new RequestOptions().centerInside()
                                .placeholder(R.drawable.ic_image))
                        .into(mSecurityDepositImage);
                // Create AlertDialog and show.
                alertDialog = alertDialogBuilder.create();
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

        //adds the amount and stores the picture
        mSubmitSecurityPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mDriverRef = FirebaseDatabase.getInstance()
                        .getReference().child("Driver").
                                child(phone_Number).child("securityDeposit").child("amount");
                mDriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("ProfileDisplay", dataSnapshot.toString());
                        int currentAmount = dataSnapshot.getValue(Integer.class);
                        currentAmount += Integer.parseInt(mSecurityDepositAmount.getText().toString());
                        dataSnapshot.getRef().setValue(currentAmount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(DepositUri != null)
                {
                    String imageid="Driver/"+ phone_Number +"/"+ "SecurityDepositSlips/" + UUID.randomUUID().toString();
                    Log.d("imagelink",imageid);

                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageid);
                    ref.putFile(DepositUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("Driver Upload", "onSuccess: uri= "+ uri.toString());

                                        }
                                    });
                                }
                            });
                }
                /*Intent intent = new Intent(ProfileDisplay.this, ProfileDisplay.class);
                startActivity(intent);*/
            alertDialog.dismiss();
            }
        });
    }

    private void setWidgets() {

        //todo setRating(driver.getRating());

        mDriverName.setText(driver.getName());
        mDriverMobile.setText(driver.getPhoneNumber());
        mDriverCNIC.setText(driver.getCnic());
        mDriverDOB.setText(driver.getBirthday());
        mDriverAddress.setText(driver.getAddress());
        mDriverLanguages.setText(extractLanguages(driver.getLanguages()));
        mManufacturer.setText(driver.getVehicle().getManufacturer());
        mModel.setText(driver.getVehicle().getModel());
        mRegField.setText(driver.getVehicle().getRegistration());
        mNumberOfseats.setText(driver.getVehicle().getVehicleSeats());
        if(driver.getVehicle().getHasAc().contentEquals("yes"))
            mACSwitch.setChecked(true);
        else
            mACSwitch.setChecked(false);
        Log.d("ProfileDisplay", String.valueOf(driver.getSecurityDeposit().getAmount()));
        mSecurityAmount.setText(String.valueOf(driver.getSecurityDeposit().getAmount()));
    }

    private String extractLanguages(String languages) {
        int length = languages.length();
        return languages.substring(1, (length-1));
    }

    private void displayProfilePicture(final Context context){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Driver").child(phone_Number).child("idImage");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    new RequestOptions();
                    Glide.with(context).
                            load(Uri.parse(dataSnapshot.getValue(String.class))).
                            apply( RequestOptions.circleCropTransform()).into(mIDImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadProfilePicture(Uri uri){

        new RequestOptions();
        Glide.with(ProfileDisplay.this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform().centerInside()
                            .placeholder(R.drawable.ic_image))
                    .into(mIDImage);
            //Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //image display

            String imageid;

            if(uri != null)
            {
                imageid="Driver/"+ phone_Number +"/"+ "Profile Picture/"+ UUID.randomUUID().toString();
                Log.d("imagelink",imageid);

                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageid);
                ref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("Driver Upload", "onSuccess: uri= "+ uri.toString());

                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver");
                                        mRef.child(phone_Number).child("idImage").setValue(uri.toString());
                                    }
                                });
                            }
                        });
            }
    }

    private class LanguagePopupListener implements View.OnClickListener{

        @Override
        public void onClick(View view)
        {
            //List<String> languagesList = new ArrayList<>();

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDisplay.this);
            builder.setTitle("Choose Languages");

            //covert languages string from driver object to array list
            mDriverLang = new ArrayList<String>(Arrays.asList(extractLanguages(driver.getLanguages()).split(",")));
            int i = 0;
            for (String lang: mDriverLang){
                mDriverLang.set(i, lang.replaceAll(" ", ""));
                i++;
                Log.d("ProfileDisplay", "language: "+lang);
            }

            Log.d("ProfileDisplay", mDriverLang.toString());

            // add a checkbox list
            String[] languages = getResources().getStringArray(R.array.languages_array);

            boolean[] checkedItems = new boolean[languages.length];
            for(int itr=0; itr<languages.length; itr++){
                checkedItems[itr] = mDriverLang.contains(languages[itr]);
            }



            builder.setMultiChoiceItems(languages, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // user checked a box, add to the array
                    if(isChecked){
                        if (!(mDriverLang.contains(languages[which]))) {
                            mDriverLang.add(languages[which]);
                            checkedItems[which] = true;
                        }
                    }
                    //user unchecked a box remove from array
                    else {
                        if (mDriverLang.contains(languages[which])) {
                            mDriverLang.remove(languages[which]);
                            checkedItems[which] = false;
                        }
                    }
                }
            });
            // add OK and Cancel buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    if(mDriverLang.size()>0){
                        mDriverLanguages.setText(extractLanguages(mDriverLang.toString()));
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private class SeatsPopupListener implements  View.OnClickListener{

        @Override
        public void onClick(View view) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDisplay.this);
            builder.setTitle("Choose Number of Seats");

            // add a checkbox list
            String[] seatNumbersArray = getResources().getStringArray(R.array.seats_array);
            builder.setSingleChoiceItems(seatNumbersArray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:numberOfSeats=4;break;
                        case 1:numberOfSeats=5; break;
                        case 2: numberOfSeats=6;break;
                        case 3: numberOfSeats=7;break;
                        case 4:numberOfSeats=8; break;
                        case 5: numberOfSeats=9;break;
                        case 6:numberOfSeats=10; break;
                        case 7: numberOfSeats=11;break;
                        case 8: numberOfSeats=12;break;
                        case 9: numberOfSeats=13;break;
                        case 10:numberOfSeats=14; break;
                        case 11: numberOfSeats=15;break;
                        case 12: numberOfSeats=16;break;
                    }

                }
            });

            // add OK and Cancel buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    mNumberOfseats.setText(String.valueOf(numberOfSeats));
                }
            });
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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

                mDriverLanguages.setOnClickListener(languagePopupListener);
                mDriverLanguages.setTextColor(getResources().getColor(R.color.black));

                mManufacturer.setClickable(true);
                mManufacturer.setFocusableInTouchMode(true);
                mManufacturer.setEnabled(true);
                mManufacturer.setTextColor(getResources().getColor(R.color.black));

                mModel.setClickable(true);
                mModel.setFocusableInTouchMode(true);
                mModel.setEnabled(true);
                mModel.setTextColor(getResources().getColor(R.color.black));

                mRegField.setClickable(true);
                mRegField.setFocusableInTouchMode(true);
                mRegField.setEnabled(true);
                mRegField.setTextColor(getResources().getColor(R.color.black));

                mNumberOfseats.setOnClickListener(seatsPopupListener);
                mNumberOfseats.setTextColor(getResources().getColor(R.color.black));


                mACSwitch.setClickable(true);

                //seatsSpinner.setClickable(true);

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
                mDriverName.setTextColor(getResources().getColor(R.color.hintgray));

                mDriverAddress.setClickable(false);
                mDriverAddress.setFocusable(false);
                mDriverAddress.setFocusableInTouchMode(false);
                mDriverAddress.setEnabled(false);
                mDriverAddress.setTextColor(getResources().getColor(R.color.hintgray));

                mDriverLanguages.setOnClickListener(null);
                mDriverLanguages.setTextColor(getResources().getColor(R.color.hintgray));

                mManufacturer.setClickable(false);
                mManufacturer.setFocusable(false);
                mManufacturer.setFocusableInTouchMode(false);
                mManufacturer.setEnabled(false);
                mManufacturer.setTextColor(getResources().getColor(R.color.hintgray));

                mModel.setClickable(false);
                mModel.setFocusable(false);
                mModel.setFocusableInTouchMode(false);
                mModel.setEnabled(false);
                mModel.setTextColor(getResources().getColor(R.color.hintgray));

                mRegField.setClickable(false);
                mRegField.setFocusable(false);
                mRegField.setFocusableInTouchMode(false);
                mRegField.setEnabled(false);
                mRegField.setTextColor(getResources().getColor(R.color.hintgray));

                mNumberOfseats.setOnClickListener(null);
                mNumberOfseats.setTextColor(getResources().getColor(R.color.hintgray));

                mACSwitch.setClickable(false);

                //seatsSpinner.setClickable(false);


                //hide save button and show edit button
                mEdit.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.GONE);

                String name = mDriverName.getText().toString();
                String address = mDriverAddress.getText().toString();
                String languages = mDriverLang.toString();
                String manufacturer = mManufacturer.getText().toString();
                String model = mModel.getText().toString();
                String registration =  mRegField.getText().toString();
                String numberSeats = mNumberOfseats.getText().toString();


                //TODO SAVE THE ITEMS
                DatabaseReference mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                        .child(phone_Number);

                //todo use language counter to check selected languages
                if(name.length()>0){
                    mDriverRef.child("name").setValue(name);
                }
                if(address.length()>0){
                    mDriverRef.child("address").setValue(address);
                }
                if(mDriverLang.size()>0){
                    mDriverRef.child("languages").setValue(languages);
                }
                if(manufacturer.length()>0){
                    mDriverRef.child("manufacturer").setValue(manufacturer);
                }
                if(model.length()>0){
                    mDriverRef.child("vehicle").child("model").setValue(model);
                }
                if(registration.length()>0){
                    mDriverRef.child("vehicle").child("registration").setValue(registration);
                }
                if(numberSeats.length()>0){
                    mDriverRef.child("vehicle").child("vehicleSeats").setValue(numberSeats);
                }
                final String[] ac = new String[1];
                mACSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            ac[0] = "yes";
                        } else {
                            // The toggle is disabled
                            ac[0] = "no";
                        }
                            mDriverRef.child("vehicle").child("hasAc").setValue(ac[0]);
                    }
                });

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

                finishAffinity();

                Intent intent = new Intent(ProfileDisplay.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

}
