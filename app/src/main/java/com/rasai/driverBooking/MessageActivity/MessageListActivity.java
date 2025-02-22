package com.dryver.driverBooking.MessageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.dryver.driverBooking.CustomObject.ChatListItem;
import com.dryver.driverBooking.CustomObject.Message;
import com.dryver.driverBooking.R;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    //private static final int ACTIVITY_NUM = 1;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private LinearLayoutManager mManager;

    private Button mSendButton;
    private EditText mTextbox;
    private View mNoMessageLayout;

    private List<Message> messageList = new ArrayList<>();

    private ChatListItem chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Intent i = getIntent();
        chat = (ChatListItem) i.getSerializableExtra("CHAT");

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        //Display name of customer at the top
        setTitle(chat.getName());

        mMessageRecycler =  findViewById(R.id.reyclerview_message_list);

        mSendButton =  findViewById(R.id.button_chatbox_send);
        mTextbox =  findViewById(R.id.edittext_chatbox);

        mMessageAdapter = new MessageListAdapter(MessageListActivity.this, messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
        //mMessageRecycler.scrollToPosition(messageList.size() - 1);
        mMessageRecycler.smoothScrollToPosition(messageList.size()-1);
        mMessageRecycler.setHasFixedSize(true);
        mManager = new
                LinearLayoutManager(MessageListActivity.this, RecyclerView.VERTICAL,false);
        mManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(mManager);

        sendMessage(mSendButton);


        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Chat").child(chat.getChatId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messageList.clear();
                mMessageRecycler.setVisibility(View.VISIBLE);
                if(mNoMessageLayout!=null){
                    mNoMessageLayout.setVisibility(View.GONE);
                }

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children){

                    messageList.add(child.getValue(Message.class));
                }

                Log.d("testing0 ListActivity", messageList.toString());

                if(messageList.size()>0){
                    mMessageAdapter.notifyDataSetChanged();
                }
                else{
                    if(mNoMessageLayout==null){
                        ViewStub stub = findViewById(R.id.noMessageLayout);
                        mNoMessageLayout =  stub.inflate();
                    }
                    else{
                        mNoMessageLayout.setVisibility(View.VISIBLE);
                    }
                    mMessageRecycler.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //on clicking back button finish activity and go back
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void sendMessage(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mTextbox.getText().toString().trim();
                if (content.length() > 0) {

                    final String userPhone = chat.getCustomerPhone();

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(userPhone);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Message newMessage = new Message();
                            newMessage.setTextMessage(mTextbox.getText().toString());
                            newMessage.setIdSender(chat.getDriverPhone());
                            newMessage.setIdReceiver(userPhone);
                            newMessage.setTimestamp(System.currentTimeMillis());
                            newMessage.setToken_receiver(dataSnapshot.child("token_id").getValue(String.class));

                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(chat.getDriverPhone());
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    newMessage.setSender_name(dataSnapshot.child("name").getValue(String.class));

                                    FirebaseDatabase.getInstance().getReference().child("Chat").child(chat.getChatId()).push().setValue(newMessage);

                                    mTextbox.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }

}
