package com.rasai.driverBooking.TripTabsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rasai.driverBooking.CustomObject.TripInformation;
import com.rasai.driverBooking.R;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<TripInformation> {

    private int resourceLayout;
    private Context mContext;
    List<TripInformation> list;

    public CustomListAdapter(Context context, int resource, List<TripInformation> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
        this.list = objects;
    }
    TextView mFrom;
    TextView mTo;
    TextView mSDate;
    TextView mSTime;
    TextView mEDate;
    TextView mETime;
    TextView mOfferMade;
    TextView mMaxBudget;
    TextView mTripType;
    TextView mSeats;
    TextView mIsReturn;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(resourceLayout, parent, false);

            mFrom = (TextView) convertView.findViewById(R.id.fromCd);
            mTo = (TextView) convertView.findViewById(R.id.toCd);
            mSDate = (TextView) convertView.findViewById(R.id.startDateCd);
            mSTime = (TextView) convertView.findViewById(R.id.startTimeCd);
            mEDate = (TextView) convertView.findViewById(R.id.endDateCd);
            mETime = (TextView) convertView.findViewById(R.id.endTimeCd);
            mOfferMade= (TextView) convertView.findViewById(R.id.offerMadeCd);
            //mMaxBudget = (TextView) convertView.findViewById(R.id.budgetMaxCd);
            mTripType = (TextView) convertView.findViewById(R.id.familyOrFriendsCd);
            //mSeats = (TextView) convertView.findViewById(R.id.numSeatsCd);
            mIsReturn = (TextView) convertView.findViewById(R.id.returnCd);
        }

        mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mSDate.setText(getItem(position).getStartDate());
        mSTime.setText(getItem(position).getStartTime());
        mOfferMade.setText(getItem(position).getDriverOffer());
        //mMaxBudget.setText(getItem(position).getMaxBudget());
        mTripType.setText(getItem(position).getTripType());
        //mSeats.setText(getItem(position).getSeats());
        mIsReturn.setText(getItem(position).getIsReturn());

        if(getItem(position).getEndDate().length() > 0){
            mEDate.setText(getItem(position).getEndDate());
            mETime.setText(getItem(position).getEndTime());
        }
        else{
            mETime.setText("");
        }

        return convertView;

    }

}



/*import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasai.driverBooking.CustomObject.Offer;
import com.rasai.driverBooking.CustomObject.TripInformation;
import com.rasai.driverBooking.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Offer> {

    List<Offer> list;

    TextView mFrom;
    TextView mTo;
    TextView mSDate;
    TextView mSTime;
    TextView mEDate;
    TextView mETime;
    TextView mMinBudget;
    TextView mMaxBudget;
    TextView mTripType;
    TextView mSeats;
    TextView mIsReturn;
    TextView mOfferMade;
    ArrayList<TripInformation> trip = new ArrayList<>();

    TripInformation tripInfo;

    public TextView getmFrom() {
        return mFrom;
    }

    public TextView getmTo() {
        return mTo;
    }

    public TextView getmSDate() {
        return mSDate;
    }

    public TextView getmSTime() {
        return mSTime;
    }

    public TextView getmEDate() {
        return mEDate;
    }

    public TextView getmETime() {
        return mETime;
    }

    public TextView getmMinBudget() {
        return mMinBudget;
    }

    public TextView getmMaxBudget() {
        return mMaxBudget;
    }

    public TextView getmTripType() {
        return mTripType;
    }

    public TextView getmSeats() {
        return mSeats;
    }

    public TextView getmIsReturn() {
        return mIsReturn;
    }

    public TextView getmOfferMade() {
        return mOfferMade;
    }

    public CustomListAdapter(Context context, int resource, List<Offer> objects) {
        super(context, resource, objects);
        this.list = objects;
        //Log.d("testing list in adapter", objects.toString());
        //Log.d("testing list adapter2", list.toString());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Log.d("getview", list.toString());

        Log.d("testing list", list.toString());

        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        final View customView = myCustomInflater.inflate(R.layout.activity_offer_list, parent, false);

        mFrom = (TextView) customView.findViewById(R.id.fromCd);
        mTo = (TextView) customView.findViewById(R.id.toCd);
        mSDate = (TextView) customView.findViewById(R.id.startDateCd);
        mSTime = (TextView) customView.findViewById(R.id.startTimeCd);
        mEDate = (TextView) customView.findViewById(R.id.endDateCd);
        mETime = (TextView) customView.findViewById(R.id.endTimeCd);
        mTripType = (TextView) customView.findViewById(R.id.familyOrFriendsCd);
        mIsReturn = (TextView) customView.findViewById(R.id.returnCd);
        mOfferMade = (TextView) customView.findViewById(R.id.offerMadeCd);



        //Log.d("getview2", getItem(position).getFrom());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Trips/"+
                getItem(position).getCustomerPhoneNumber()+"/"+getItem(position).getTripID()+"/");
        Log.d("testing adapter", getItem(position).getCustomerPhoneNumber());
        Log.d("testing adapter", getItem(position).getTripID());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("testing adapter", dataSnapshot.toString());
                tripInfo = new TripInformation();
                tripInfo = dataSnapshot.getValue(TripInformation.class);
                Log.d("tripObject", dataSnapshot.getValue(TripInformation.class).toString());
                Log.d("tripObject", tripInfo.getTo());

                trip.add(tripInfo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(trip != null)
        {
        mFrom.setText(trip.get(position).getFrom());
        }

        /*mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mSDate.setText(getItem(position).getStartDate());
        mSTime.setText(getItem(position).getStartTime());
        mTripType.setText(getItem(position).getTripType());
        mIsReturn.setText(getItem(position).getIsReturn());

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Offer/"+
                getItem(position).getDatabaseId()+user.getPhoneNumber());
        final Offer[] offer = new Offer[1];

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offer[0] = dataSnapshot.getValue(Offer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mOfferMade.setText(offer[0].getAmount());


        //mOfferMade.setText(getItem(position).getIsReturn());

        if(getItem(position).getEndDate().length() > 0){
            mEDate.setText(getItem(position).getEndDate());
            mETime.setText(getItem(position).getEndTime());
        }
        else{
            mETime.setText("");
        }
*/
        /*return customView;

    }

}*/

