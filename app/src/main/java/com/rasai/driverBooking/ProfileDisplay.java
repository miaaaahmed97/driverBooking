package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rasai.driverBooking.Registration.MultiSelectionSpinner;
import com.rasai.driverBooking.TripTabsActivity.TripTabsActivity;

import java.util.ArrayList;
import java.util.List;


public class ProfileDisplay extends AppCompatActivity {

    private MultiSelectionSpinner mLangSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        mLangSpinner = (MultiSelectionSpinner)findViewById(R.id.lang_spinner);

        List<String> languageList = new ArrayList<String>();
        languageList.add("English");
        languageList.add("Urdu");
        languageList.add("Punjabi");
        mLangSpinner.setItems(languageList);

        //Start - Spinner Layout Setup
        Spinner manufacturerSpinner = (Spinner) findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerAdapter);

        Spinner seatsSpinner = (Spinner) findViewById(R.id.seatsSpinner);
        ArrayAdapter<CharSequence> seatsAdapter = ArrayAdapter.createFromResource(this,
                R.array.seats_array, android.R.layout.simple_spinner_item);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);
        //End - Spinner Layout Setup
    }


}
