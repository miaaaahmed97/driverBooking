package com.dryver.driverBooking.Registration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.dryver.driverBooking.CustomObject.Driver;
import com.dryver.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverRegistration extends AppCompatActivity implements Serializable {

    private static final String TAG = "DriverRegistration" ;
    private TextInputEditText mName;
    private TextInputEditText mCnic;
    private TextInputEditText mBday;
    private TextInputEditText mAddress;
    private Button mnextButton;
    private FrameLayout mSelectLanguages;
    private TextView mSelectLanguagesText;

    private Driver driverInformation;
    //private List<String> listLangSelected;
    //private String StringLangSelected;
    private String phoneNumber;
    private int languageCounter = 0;

    //private FirebaseDatabase database;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        //hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        driverInformation = new Driver();
        phoneNumber = user.getPhoneNumber();
        //mLangSpinner = findViewById(R.id.lang_spinner);
        mSelectLanguages = findViewById(R.id.selectLanguagesLayout);
        mSelectLanguagesText = findViewById(R.id.selectLanguagesText);

        //set the progress
        //String[] descriptionData = {"Driver\nRegistration", "Upload\nPictures", "Register\nVehicle", "Car\nPictures" , "Security\nDeposit"};
        StateProgressBar stateProgressBar = findViewById(R.id.simpleProgressBar);
        //stateProgressBar.setStateDescriptionData(descriptionData);
        //stateProgressBar.setStateDescriptionTypeface("fonts/roboto_light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/roboto_thin.ttf");


        mSelectLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> languagesList = new ArrayList<>();
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverRegistration.this);
                builder.setTitle("Choose Languages");
                // add a checkbox list
                String[] languages = getResources().getStringArray(R.array.languages_array);
                boolean[] checkedItems = null;
                builder.setMultiChoiceItems(languages, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // user checked a box, add to the array
                        if(isChecked){
                            languagesList.add(languages[which]);
                            languageCounter+=1;
                        }
                        //user unchecked a box remove from array
                        else {
                            languagesList.remove(languages[which]);
                            languageCounter-=1;
                        }
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        if(languagesList.size()>0){
                            mSelectLanguagesText.setText(languagesList.toString());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*List<String> languageList = new ArrayList<String>();
        languageList.add("English");
        languageList.add("Urdu");
        languageList.add("Punjabi");
        mLangSpinner.setItems(languageList);

        mLangSpinner.callOnClick();*/

        //StringLangSelected = mLangSpinner.buildSelectedItemString();
        //mLangSpinner.onClick();


        mName = findViewById(R.id.name_field);
        mCnic =  findViewById(R.id.cnic_field);
        mBday =  findViewById(R.id.birthday_field);
        mBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText clickedEditText = (EditText) view;
                //final Calendar cldr = Calendar.getInstance();
                int day = 1; //cldr.get(Calendar.DAY_OF_MONTH);
                int month = 0; //cldr.get(Calendar.MONTH);
                int year = 1990; //cldr.get(Calendar.YEAR);
                // date datePicker dialog
                DatePickerDialog datePicker = new DatePickerDialog(DriverRegistration.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //view.updateDate(1980, 1, 1);
                                clickedEditText.setText(String.format("%02d/%02d/%04d",dayOfMonth ,(monthOfYear + 1), year));
                            }
                        }, year, month, day);
                datePicker.show();

            }
        });
        mAddress =  findViewById(R.id.address_field);

        //mBday.addTextChangedListener(new DateMask());
        //showSelectedLanguages = (TextView) findViewById(R.id.selectedLanguages_textview);
        //

        mnextButton =  findViewById(R.id.continueDriverReg);

        class MyOnClickListener implements View.OnClickListener, Serializable {
            @Override
            public void onClick(View view) {

                //StringLangSelected = mLangSpinner.getSelectedStrings().toString();
                String name = mName.getText().toString();
                String cnic = mCnic.getText().toString();
                String bday = mBday.getText().toString();
                String address = mAddress.getText().toString();
                String StringLangSelected = mSelectLanguagesText.getText().toString();

                //Log.d("testing1", StringLangSelected);

                //set tripInformation
                if (name.length() > 0 && cnic.length() == 13 && bday.length()> 0
                        && address.length() > 0 && languageCounter > 0) {
                    driverInformation.setPhoneNumber(phoneNumber);
                    driverInformation.setName(name);
                    driverInformation.setCnic(cnic);
                    driverInformation.setBirthday(bday);
                    driverInformation.setAddress(address);
                    driverInformation.setLanguages(StringLangSelected);

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }
                                    // Get new Instance ID token
                                    String token_id = task.getResult().getToken();
                                    driverInformation.setToken_id(token_id);
                                    Intent navNext = new Intent(DriverRegistration.this, driverRegistration2.class);
                                    navNext.putExtra("driverObject", driverInformation);
                                    startActivity(navNext);
                                }
                            });
                } else {
                    Toast.makeText(getBaseContext(), "Please fill all fields according to format.",
                            Toast.LENGTH_LONG).show();
                }

            }
        }

        mnextButton.setOnClickListener(new MyOnClickListener());

    }

    //disable select languages text
    //public static void disappear(){
    //    mSelectLanguages.setVisibility(View.GONE);
    //}

    /*date validator
    * this works for format and values both
    private boolean isThisDateValid(String dateToValidate){

        //empty date field
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            sdf.parse(dateToValidate);
            //System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
    */

}