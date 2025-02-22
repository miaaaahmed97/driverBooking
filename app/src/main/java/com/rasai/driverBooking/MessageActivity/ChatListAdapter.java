package com.dryver.driverBooking.MessageActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dryver.driverBooking.CustomObject.ChatListItem;
import com.dryver.driverBooking.R;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class ChatListAdapter extends ArrayAdapter<ChatListItem> {

    TextView mFrom;
    TextView mTo;
    TextView mName;
    TextView mPreview;
    ImageView mId;

    public ChatListAdapter(Context context, int resource, List<ChatListItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Log.d("testing0 ChatAdapter", "inside getview");

        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View customView = myCustomInflater.inflate(R.layout.chat_main_card, parent, false);

        mFrom = customView.findViewById(R.id.fromCd);
        mTo = customView.findViewById(R.id.toCd);
        mName = customView.findViewById(R.id.name_chat);
        mPreview = customView.findViewById(R.id.lastMessage);

        mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mName.setText(getItem(position).getName());
        mPreview.setText(getItem(position).getMsgPreview());

        return customView;
    }

}
