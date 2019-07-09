package com.rasai.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.Registration.DriverRegistration;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        //Called when there is a change in the authentication state
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            /*gets invoked in the UI thread on changes in the authentication state
             * Right after the listener has been registered
             * When a user is signed in
             * When the current user is signed out
             * When the current user changes
             * When there is a change in the current user's token
             */
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver");

                Log.d("testing0 MainActvity", "inside authlistener");
                if (firebaseAuth.getCurrentUser() != null) {
                    // Sign in logic here.
                    isRegistered(mRef, firebaseAuth.getCurrentUser());
                }
            }
        };

        if( user != null){
            Log.d("testing1 MainActvity", "inside if");
            //already signed in
            //startActivity(myIntent);
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver");
            isRegistered(mRef, user);
        }else{
            //create login options
            Log.d("testing2 MainActvity", "inside else");
            startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setAvailableProviders
                                    (Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder()
                                                    .build()/*,
                                    new AuthUI.IdpConfig.EmailBuilder()
                                            .build()*/)).build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(mAuthListener);
    }

    private void isRegistered(DatabaseReference myRef, FirebaseUser Myuser){

        final FirebaseUser mUser = Myuser;

        myRef.addValueEventListener(new ValueEventListener() {

            Boolean controller = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child: children){

                    if(child.getKey().equals(mUser.getPhoneNumber())){

                        Intent intentHome = new Intent(MainActivity.this, DriverHome.class);
                        startActivity(intentHome);
                        controller = false;
                    }
                }

                if (controller) {
                    //new intent creation has to be inside a method
                    Intent intentReg = new Intent(MainActivity.this, DriverRegistration.class);
                    startActivity(intentReg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
