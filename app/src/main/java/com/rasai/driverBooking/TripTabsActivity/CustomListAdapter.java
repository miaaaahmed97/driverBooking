package com.dryver.driverBooking.TripTabsActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dryver.driverBooking.CustomObject.TripInformation;
import com.dryver.driverBooking.R;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class CustomListAdapter extends ArrayAdapter<TripInformation> {

    private int resourceLayout;
    private Context mContext;
    private List<TripInformation> list;

    public CustomListAdapter(Context context, int resource, List<TripInformation> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
        this.list = objects;
    }
    private TextView mFrom;
    private TextView mTo;
    private TextView mSDate;
    private TextView mSTime;
    private TextView mEDate;
    private TextView mETime;
    private TextView mOfferMade;
    private TextView mTripType;
    private TextView mIsReturn;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Log.d("CustomListAdapter", "inside getView()");

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
            mTripType = convertView.findViewById(R.id.familyOrFriendsCd);
            mIsReturn = convertView.findViewById(R.id.returnCd);
        }

        mFrom.setText(getItem(position).getFrom());
        mTo.setText(getItem(position).getTo());
        mSDate.setText(getItem(position).getStartDate());
        mSTime.setText(getItem(position).getStartTime());
        mOfferMade.setText(getItem(position).getDriverOffer());
        mTripType.setText(getItem(position).getTripType());
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
