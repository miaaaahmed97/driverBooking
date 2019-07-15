package com.rasai.driverBooking.MessageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.BottomNavigationViewHelper;
import com.rasai.driverBooking.CustomObject.ChatListItem;
import com.rasai.driverBooking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainChat extends AppCompatActivity {

    //get current user
    private String phone_Number;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseUser user = mauth.getCurrentUser();

    private static final int ACTIVITY_NUM = 1;
    private ListView mListView;
    private ChatListAdapter mAdapter;

    List<String> tripList = new ArrayList<>();
    List<ChatListItem> chatsList = new ArrayList<ChatListItem>();

    ChatListItem currentChat;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_main);

        phone_Number = user.getPhoneNumber();

        mListView = findViewById(R.id.list_view);
        mAdapter = new ChatListAdapter(MainChat.this, R.layout.activity_chats_main, chatsList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {

            Intent chatIntent = new Intent(MainChat.this, MessageListActivity.class);
            chatIntent.putExtra("CHAT", chatsList.get(i));
            Log.d("testing5 MainChat",  chatsList.get(i).toString());
            startActivity(chatIntent);
        });

        //popup box with actions such as deleting, archiving chat
        mListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });

        class MyDriverValueEventListener implements ValueEventListener, Serializable{
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> offersConfirmed = dataSnapshot.child("offerConfirmed").getChildren();

                for(DataSnapshot child: offersConfirmed){
                    tripList.add(child.getValue(String.class));
                }

                Iterable<DataSnapshot> tripsCompleted = dataSnapshot.child("tripsCompleted").getChildren();

                for(DataSnapshot child: tripsCompleted){
                    tripList.add(child.getValue(String.class));
                }

                driverCallback();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

        DatabaseReference mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                .child(phone_Number);
        mDriverRef.addValueEventListener(new MyDriverValueEventListener());


        //creating bottom navigation view
        setupBottomNavigationView();

    }

    class MyOfferValueEventListener implements ValueEventListener, Serializable{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            ChatListItem chat = new ChatListItem();

            chat.setDriverPhone(phone_Number);
            chat.setCustomerPhone(dataSnapshot.child("customerPhoneNumber").getValue(String.class));
            chat.setChatId(createChatID(chat.getCustomerPhone(), phone_Number));

            chatsList.add(chat);

            if(tripList.size() == chatsList.size()){
                offersCallback();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    class MyTripsValueEventListener implements ValueEventListener, Serializable{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            ChatListItem chat;
            chat = chatsList.get(counter);

            chat.setFrom(dataSnapshot.child("from").getValue(String.class));
            chat.setTo(dataSnapshot.child("to").getValue(String.class));

            counter +=1 ;

            if(counter == chatsList.size()){
                mAdapter.notifyDataSetChanged();
            }

            Log.d("testing2 mainChat", chatsList.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void driverCallback(){

        for(String trip: tripList){

            DatabaseReference mChatReference = FirebaseDatabase.getInstance().getReference()
                    .child("Offer").child(trip).child(phone_Number);
            mChatReference.addValueEventListener(new MyOfferValueEventListener());
        }

    }

    private void offersCallback(){

        int itr;
        for (itr=0; itr < chatsList.size(); itr++){

            DatabaseReference mChatReference = FirebaseDatabase.getInstance().getReference()
                    .child("Trips").child(chatsList.get(itr).getCustomerPhone()).child(tripList.get(itr));
            mChatReference.addValueEventListener(new MyTripsValueEventListener());

        }

    }

    private String createChatID(String customerPhone, String driverPhone){
        String chatThreadId = "";

        /*
         * compareTo() returns a positive number if customer number is lexically smaller than
         * the driver number
         * and negative number if customer number is lexically greater than the driver number
         * */
        if(customerPhone.compareTo(driverPhone) > 0)
        {
            chatThreadId = driverPhone+customerPhone;
        }
        else if(customerPhone.compareTo(driverPhone) <0 )
        {
            chatThreadId =  customerPhone+driverPhone;
        }
        else{
            chatThreadId =  customerPhone+driverPhone;
        }

        return chatThreadId;
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        //Log.d("TripTabsActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(MainChat.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        //get the item 0 as we're on page 0
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        //select it
        menuItem.setChecked(true);
    }
}
