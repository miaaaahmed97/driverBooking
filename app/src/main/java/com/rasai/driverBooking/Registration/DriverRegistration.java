package com.rasai.driverBooking.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverRegistration extends AppCompatActivity implements Serializable {

    private TextInputEditText mName;
    private TextInputEditText mCnic;
    private TextInputEditText mBday;
    private TextInputEditText mAddress;
    private MultiSelectionSpinner mLangSpinner;
    private Button mnextButton;
    private static TextView mSelectLanguages;

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

        //Set tht title
        assert getSupportActionBar() != null;   //null check
        setTitle("REGISTRATION");

        driverInformation = new Driver();

        phoneNumber = user.getPhoneNumber();
        mLangSpinner = findViewById(R.id.lang_spinner);
        mSelectLanguages = findViewById(R.id.LanguagesText);

        List<String> languageList = new ArrayList<String>();
        languageList.add("English");
        languageList.add("Urdu");
        languageList.add("Punjabi");
        mLangSpinner.setItems(languageList);

        mLangSpinner.callOnClick();

        //StringLangSelected = mLangSpinner.buildSelectedItemString();
        //mLangSpinner.onClick();


        mName = findViewById(R.id.name_field);
        mCnic =  findViewById(R.id.cnic_field);
        mBday =  findViewById(R.id.birthday_field);
        mAddress =  findViewById(R.id.address_field);

        //mBday.addTextChangedListener(new DateMask());
        //showSelectedLanguages = (TextView) findViewById(R.id.selectedLanguages_textview);
        //

        mnextButton =  findViewById(R.id.continueDriverReg);

        class MyOnClickListener implements View.OnClickListener, Serializable {
            @Override
            public void onClick(View view) {

                StringLangSelected = mLangSpinner.getSelectedStrings().toString();

                Log.d("testing1", StringLangSelected);

                //set tripInformation
                driverInformation.setPhoneNumber(phoneNumber);
                driverInformation.setName(mName.getText().toString());
                driverInformation.setCnic(mCnic.getText().toString());
                driverInformation.setBirthday(mBday.getText().toString());
                driverInformation.setAddress(mAddress.getText().toString());
                driverInformation.setLanguages(StringLangSelected);

                //Log.d("testing2", StringLangSelected);

                //Log.d("testing3", driverInformation.toString());

                Intent navNext = new Intent(DriverRegistration.this, driverRegistration2.class);
                navNext.putExtra("driverObject", driverInformation);
                startActivity(navNext);
            }
        }

        mnextButton.setOnClickListener(new MyOnClickListener());

    }

    //disable select languages text
    public static void disappear(){
        mSelectLanguages.setVisibility(View.GONE);
    }

    //cannot go back to previous activity, closes down app to background
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class DateMask implements TextWatcher {

        private static final int MAX_LENGTH = 8;
        private static final int MIN_LENGTH = 2;

        private String updatedText;
        private boolean editing;


        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
            if (text.toString().equals(updatedText) || editing) return;

            String digits = text.toString().replaceAll("\\D", "");
            int length = digits.length();

            if (length <= MIN_LENGTH) {
                updatedText = digits;
                return;
            }

            if (length > MAX_LENGTH) {
                digits = digits.substring(0, MAX_LENGTH);
            }

            if (length <= 4) {
                String day = digits.substring(0, 2);
                String month = digits.substring(2);

                updatedText = String.format(Locale.US, "%s/%s",day,month);
            } else {
                String day = digits.substring(0, 2);
                String month = digits.substring(2, 4);
                String year = digits.substring(4);

                updatedText = String.format(Locale.US, "%s/%s/%s",day, month, year);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editing) return;

            editing = true;

            editable.clear();
            editable.insert(0, updatedText);

            editing = false;
        }
    }

}