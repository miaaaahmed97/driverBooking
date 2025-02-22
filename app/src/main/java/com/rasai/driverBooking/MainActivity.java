package com.dryver.driverBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.Serializable;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mRef;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide the top bar
        getSupportActionBar().hide();

        Firebase.setAndroidContext(this);

        mRef = FirebaseDatabase.getInstance().getReference().child("Driver");
        listener = new MyValueEventListener(user);

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

                user = mauth.getCurrentUser();

                Log.d("testing0 MainActvity", "inside authlistener");
                if (firebaseAuth.getCurrentUser() != null) {
                    // Sign in logic here.
                    listener = new MyValueEventListener(user);
                    isRegistered(mRef);
                }
            }
        };

        if( user != null){
            Log.d("testing1 MainActvity", "inside if");
            //already signed in
            //startActivity(myIntent);
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver");
            listener = new MyValueEventListener(user);
            isRegistered(mRef);
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

    class MyValueEventListener implements ValueEventListener, Serializable {

        FirebaseUser mUser;

        public MyValueEventListener(FirebaseUser Myuser) {
            this.mUser = Myuser;
        }

        Boolean controller = true;

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

            //if (mUser != null) {
                for(DataSnapshot child: children){

                    assert (user.getPhoneNumber()!=null);
                    if(child.getKey().equals(user.getPhoneNumber())){

                        Intent intentHome = new Intent(MainActivity.this, DriverHome.class);

                        //Update Firebase Messaging token
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                                            return;
                                        }

                                        // Get new Instance ID token
                                        String token_id = task.getResult().getToken();

                                        //Add name to Customer in Firebase
                                        final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference()
                                                .child("Driver").child(user.getPhoneNumber());
                                        mRef.child("token_id").setValue(token_id);

                                    }
                                });

                        startActivity(intentHome);
                        controller = false;
                    }
                }
            //}

            if (controller) {
                //new intent creation has to be inside a method
                Intent intentReg = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intentReg);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(mAuthListener);
    }

    private void isRegistered(DatabaseReference myRef){

        myRef.addListenerForSingleValueEvent(listener);

    }

    @Override
    protected void onStop() {
        mRef.removeEventListener(listener);
        super.onStop();
    }
}
