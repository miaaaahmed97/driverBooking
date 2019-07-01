package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rasai.driverBooking.Registration.DriverRegistration;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    Intent myIntent;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        //new intent creation has to be inside a method
        myIntent = new Intent(MainActivity.this, DriverRegistration.class);

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
                if (firebaseAuth.getCurrentUser() != null) {
                    // Sign in logic here.
                    startActivity(myIntent);
                }
            }
        };

        if( user != null){
            //already signed in
            startActivity(myIntent);
        }else{
            //create login options
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
}
