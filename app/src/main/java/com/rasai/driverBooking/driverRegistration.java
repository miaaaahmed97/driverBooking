package com.rasai.driverBooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class driverRegistration extends Activity implements Serializable {

    private TextInputEditText mCnic;
    private TextInputEditText mBday;
    private TextInputEditText mAddress;
    private MultiSelectionSpinner mLangSpinner;
    private Button mnextButton;
    private TextView showSelectedLanguages;

    Driver driverInformation;
    private List<String> listLangSelected;
    private String StringLangSelected;
    private String phoneNumber;

    //private FirebaseDatabase database;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        driverInformation = new Driver();

        phoneNumber = user.getPhoneNumber();

        mLangSpinner = (MultiSelectionSpinner)findViewById(R.id.lang_spinner);

        List<String> languageList = new ArrayList<String>();
        languageList.add("English");
        languageList.add("Urdu");
        languageList.add("Punjabi");
        mLangSpinner.setItems(languageList);

        //StringLangSelected = mLangSpinner.buildSelectedItemString();
        //mLangSpinner.onClick();


        mCnic = (TextInputEditText) findViewById(R.id.cnic_field);
        mBday = (TextInputEditText) findViewById(R.id.birthday_field);
        mAddress = (TextInputEditText) findViewById(R.id.address_field);

        //showSelectedLanguages = (TextView) findViewById(R.id.selectedLanguages_textview);
        //

        mnextButton = (Button) findViewById(R.id.continueDriverReg);
        class MyOnClickListener implements View.OnClickListener, Serializable {
            @Override
            public void onClick(View view) {

                StringLangSelected = mLangSpinner.getSelectedStrings().toString();

                Log.d("testing1", StringLangSelected);

                //set tripInformation
                driverInformation.setPhoneNumber(phoneNumber);
                driverInformation.setCnic(mCnic.getText().toString());
                driverInformation.setBirthday(mBday.getText().toString());
                driverInformation.setAddress(mAddress.getText().toString());
                driverInformation.setLanguages(StringLangSelected);

                //Log.d("testing2", StringLangSelected);

                //Log.d("testing3", driverInformation.toString());

                Intent navNext = new Intent(driverRegistration.this, driverRegistration2.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);
            }
        }

        mnextButton.setOnClickListener(new MyOnClickListener());

    }
}