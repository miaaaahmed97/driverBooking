package com.rasai.driverBooking.MessageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.BottomNavigationViewHelper;
import com.rasai.driverBooking.CustomObject.ChatListItem;
import com.rasai.driverBooking.CustomObject.Message;
import com.rasai.driverBooking.R;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 1;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private Button mSendButton;
    private EditText mTextbox;

    List<Message> messageList = new ArrayList<>();

    private ChatListItem chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Intent i = getIntent();
        chat = (ChatListItem) i.getSerializableExtra("CHAT");

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        mSendButton = (Button) findViewById(R.id.button_chatbox_send);
        mTextbox = (EditText) findViewById(R.id.edittext_chatbox);

        mMessageAdapter = new MessageListAdapter(MessageListActivity.this, messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setHasFixedSize(true);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(MessageListActivity.this, RecyclerView.VERTICAL,false));

        sendMessage(mSendButton);

        //creating bottom navigation view
        setupBottomNavigationView();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Chat").child(chat.getChatId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messageList.clear();

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children){

                    messageList.add(child.getValue(Message.class));
                }

                Log.d("testing0 ListActivity", messageList.toString());

                mMessageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d("MessageListActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(MessageListActivity.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void sendMessage(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mTextbox.getText().toString().trim();
                if (content.length() > 0) {

                    Message newMessage = new Message();
                    newMessage.setTextMessage(mTextbox.getText().toString());
                    newMessage.setIdSender(chat.getDriverPhone());
                    newMessage.setIdReceiver(chat.getCustomerPhone());
                    newMessage.setTimestamp(System.currentTimeMillis());
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(chat.getChatId()).push().setValue(newMessage);

                    mTextbox.setText("");
                }
            }
        });

    }

}
