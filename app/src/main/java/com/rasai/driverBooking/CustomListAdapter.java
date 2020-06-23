package com.dryver.driverBooking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dryver.driverBooking.CustomObject.TripInformation;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class CustomListAdapter extends ArrayAdapter<TripInformation> {

    private List<TripInformation> list;

    private TextView mFrom;
    private TextView mTo;
    private TextView mSDate;
    private TextView mSTime;
    private TextView mEDate;
    private TextView mETime;
    private TextView mMinBudget;
    private TextView mMaxBudget;
    TextView mTripType;
    private TextView mSeats;
    private TextView mIsReturn;

    public CustomListAdapter(Context context, int resource, List<TripInformation> objects) {
        super(context, resource, objects);
        this.list = objects;
        //Log.d("testing list in adapter", objects.toString());
        //Log.d("testing list adapter2", list.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Log.d("getview", list.toString());

        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View customView = myCustomInflater.inflate(R.layout.driver_home_list_item, parent, false);

        mFrom = customView.findViewById(R.id.fromCd);
        mTo = customView.findViewById(R.id.toCd);
        mSDate = customView.findViewById(R.id.startDateCd);
        mSTime = customView.findViewById(R.id.startTimeCd);
        mEDate = customView.findViewById(R.id.endDateCd);
        mETime = customView.findViewById(R.id.endTimeCd);
        mMinBudget= customView.findViewById(R.id.budgetMinCd);
        mMaxBudget = customView.findViewById(R.id.budgetMaxCd);
        //mTripType = (TextView) customView.findViewById(R.id.familyOrFriendsCd);
        mSeats = customView.findViewById(R.id.numSeatsCd);
        mIsReturn = customView.findViewById(R.id.returnCd);


        //Log.d("getview2", getItem(position).getFrom());

        mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mSDate.setText(getItem(position).getStartDate());
        mSTime.setText(getItem(position).getStartTime());
        mMinBudget.setText(getItem(position).getMinBudget());
        mMaxBudget.setText(getItem(position).getMaxBudget());
        //mTripType.setText(getItem(position).getTripType());
        mSeats.setText(getItem(position).getSeats());
        mIsReturn.setText(getItem(position).getIsReturn());

        if(getItem(position).getEndDate().length() > 0){
            mEDate.setText(getItem(position).getEndDate());
            mETime.setText(getItem(position).getEndTime());
        }
        else{
            mETime.setText("");
        }

        return customView;

    }

}
