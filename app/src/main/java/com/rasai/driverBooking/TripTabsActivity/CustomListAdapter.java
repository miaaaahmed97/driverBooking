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

            mFrom = convertView.findViewById(R.id.fromCd);
            mTo = convertView.findViewById(R.id.toCd);
            mSDate = convertView.findViewById(R.id.startDateCd);
            mSTime = convertView.findViewById(R.id.startTimeCd);
            mEDate = convertView.findViewById(R.id.endDateCd);
            mETime = convertView.findViewById(R.id.endTimeCd);
            mOfferMade= convertView.findViewById(R.id.offerMadeCd);
            //mMaxBudget = (TextView) convertView.findViewById(R.id.budgetMaxCd);
            mTripType = convertView.findViewById(R.id.familyOrFriendsCd);
            //mSeats = (TextView) convertView.findViewById(R.id.numSeatsCd);
            mIsReturn = convertView.findViewById(R.id.returnCd);
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
