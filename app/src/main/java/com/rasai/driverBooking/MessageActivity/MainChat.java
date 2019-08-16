package com.rasai.driverBooking.MessageActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
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
import com.google.firebase.database.Query;
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

    private DatabaseReference mDriverRef;

    private static final int ACTIVITY_NUM = 2;
    private ListView mListView;
    private ChatListAdapter mAdapter;
    private View mNoChatsLayout;

    private List<String> tripList = new ArrayList<>();
    private List<ChatListItem> chatsList = new ArrayList<ChatListItem>();

    private int tripsCounter = 0;
    private int chatCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_main);

        //Set the title
        assert getSupportActionBar() != null;   //null check
        setTitle("CHAT");

        phone_Number = user.getPhoneNumber();

        mListView = findViewById(R.id.chats_list_view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent chatIntent = new Intent(MainChat.this, MessageListActivity.class);
                chatIntent.putExtra("CHAT", chatsList.get(i));
                Log.d("testing5 MainChat",  chatsList.get(i).toString());
                startActivity(chatIntent);
            }
        });

        //popup box with actions such as deleting, archiving chat
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainChat.this);
                //builder.setTitle("Choose an option");

                // add a list
                String[] actions = {"Archive", "Delete", "Block"};
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Archive
                                archive(chatsList.get(i).getChatId());
                                break;
                            case 1: // Delete
                                delete(chatsList.get(i).getChatId());
                                break;
                            case 2: // Block
                                block(chatsList.get(i).getChatId(), chatsList.get(i).getCustomerPhone());
                                break;
                            /*case 3: // Report
                                break;*/
                            default:
                                break;
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


                return true;
            }
        });

        mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                .child(phone_Number);

        //creating bottom navigation view
        setupBottomNavigationView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDriverRef.addValueEventListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDriverRef.removeEventListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDriverRef.removeEventListener(listener);
    }

    class MyDriverValueEventListener implements ValueEventListener, Serializable{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            tripList.clear();
            chatsList.clear();

            Log.d("MainChat", "MyDriverValueEventListener");

            mListView.setVisibility(View.VISIBLE);
            if(mNoChatsLayout!=null){
                mNoChatsLayout.setVisibility(View.GONE);
            }

            Iterable<DataSnapshot> offersConfirmed = dataSnapshot.child("chatThreads").getChildren();

            for(DataSnapshot child: offersConfirmed){
                Log.d("MainChat", "child: "+child.toString());
                tripList.add(child.getValue(String.class));
            }

            Log.d("MainChat", "tripList.size(): "+tripList.size());

            if(tripList.size()>0){
                driverCallback();
            }
            else{
                if(mNoChatsLayout==null){
                    ViewStub stub = findViewById(R.id.noChatsLayout);
                    mNoChatsLayout = stub.inflate();
                }
                else{
                    mNoChatsLayout.setVisibility(View.VISIBLE);
                }
                mListView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private ValueEventListener listener = new MyDriverValueEventListener();

    class MyOfferValueEventListener implements ValueEventListener, Serializable{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            ChatListItem chat = new ChatListItem();

            chat.setDriverPhone(phone_Number);
            chat.setCustomerPhone(dataSnapshot.child("customerPhoneNumber").getValue(String.class));

            chatsList.add(chat);

            if(tripList.size() == chatsList.size()){
                Log.d("MainChat", "calling offersCallback()");
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
            chat = chatsList.get(tripsCounter);

            chat.setFrom(dataSnapshot.child("from").getValue(String.class));
            chat.setTo(dataSnapshot.child("to").getValue(String.class));
            chat.setName(dataSnapshot.child("customerName").getValue(String.class));
            chat.setChatId(dataSnapshot.child("databaseId").getValue(String.class));

            DatabaseReference mChatRef = FirebaseDatabase.getInstance().getReference();
            Query query = mChatRef.child("Chat").child(chat.getChatId()).orderByKey().limitToLast(1);
            query.addListenerForSingleValueEvent(new MyChatValueEventListener());

            tripsCounter+=1;

            Log.d("testing2 mainChat", chatsList.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    class MyChatValueEventListener implements ValueEventListener, Serializable{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Log.d("testing4 mainChat", dataSnapshot.toString());

            ChatListItem chat;
            chat = chatsList.get(chatCounter);

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

            for (DataSnapshot child: children){

                chat.setMsgPreview(child.child("textMessage").getValue(String.class));

            }

            chatCounter +=1 ;

            if(chatCounter == (chatsList.size())){

                Log.d("testing3 mainChat", chatsList.toString());
                mAdapter = new ChatListAdapter(MainChat.this, R.layout.activity_chats_main, chatsList);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
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

        tripsCounter =0;
        chatCounter =0;

        int itr;
        for (itr=0; itr < chatsList.size(); itr++){

            DatabaseReference mChatReference = FirebaseDatabase.getInstance().getReference()
                    .child("Trips").child(chatsList.get(itr).getCustomerPhone()).child(tripList.get(itr));
            mChatReference.addValueEventListener(new MyTripsValueEventListener());

        }

    }

    private void delete(String id){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                .child(phone_Number).child("chatThreads");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children){
                    if(child.getValue().toString().equals(id)){
                        child.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void archive(String id){

        delete(id);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                .child(phone_Number).child("chatArchive");
        mRef.push().setValue(id);

    }

    private void block(String id, String contactNumber){

        delete(id);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver")
                .child(phone_Number).child("blockedNumbers");
        mRef.push().setValue(contactNumber);

    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d("mmmccc", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(MainChat.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        //get the item 2 as we're on page 2
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        //select it
        menuItem.setChecked(true);
    }
}
