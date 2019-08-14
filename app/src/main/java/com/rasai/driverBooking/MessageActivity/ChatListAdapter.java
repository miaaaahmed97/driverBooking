package com.rasai.driverBooking.MessageActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rasai.driverBooking.CustomObject.ChatListItem;
import com.rasai.driverBooking.R;

import java.util.List;

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

        mFrom = (TextView) customView.findViewById(R.id.fromCd);
        mTo = (TextView) customView.findViewById(R.id.toCd);
        mName = (TextView) customView.findViewById(R.id.name_chat);
        mPreview = (TextView) customView.findViewById(R.id.lastMessage);

        mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mName.setText(getItem(position).getName());
        mPreview.setText(getItem(position).getMsgPreview());

        return customView;
    }

}
