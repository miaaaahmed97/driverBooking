package com.rasai.driverBooking.CustomObject;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripInformation implements Serializable {

    public TripInformation() {}

    private String phoneNumber;
    private String CustomerName;
    private String to;
    private String from;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String tripType;
    private String seats;
    private String minBudget;
    private String maxBudget;
    private String extraDetails;
    private String isReturn = "no";
    private Boolean confirmed = false;
    private Boolean completed = false;
    private String databaseId;
    private String driverOffer = "";
    private String driverAssigned;
    private String customerToken;

    @Override
    public String toString() {
        return "TripInformation{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                ", tripType='" + tripType + '\'' +
                ", seats='" + seats + '\'' +
                ", minBudget='" + minBudget + '\'' +
                ", maxBudget='" + maxBudget + '\'' +
                ", extraDetails='" + extraDetails + '\'' +
                ", isReturn='" + isReturn + '\'' +
                ", confirmed=" + confirmed +
                ", completed=" + completed +
                ", databaseId='" + databaseId + '\'' +
                ", driverAssigned='" + driverAssigned + '\'' +
                ", customerToken='" + customerToken + '\'' +
                '}';
    }

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getDriverAssigned() {
        return driverAssigned;
    }

    public void setDriverAssigned(String driverAssigned) {
        this.driverAssigned = driverAssigned;
    }

    public String getDriverOffer() {
        return driverOffer;
    }

    public void setDriverOffer(String driverOffer) {
        this.driverOffer = driverOffer;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = changeDateFormat(startDate);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = changeDateFormat(endDate);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(String minBudget) {
        this.minBudget = minBudget;
    }

    public String getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(String maxBudget) {
        this.maxBudget = maxBudget;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public void postTrip(DatabaseReference myRef) { //add to database package
        DatabaseReference myReference =  myRef.child("Trips/"+phoneNumber).push();
        databaseId = myReference.getKey();
        myRef.child("Trips").child(phoneNumber).child(databaseId).setValue(this);
    }

    public String changeDateFormat(String date) {
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yy");
        DateFormat reqFormatter=new SimpleDateFormat("dd MMM yyyy");

        Date date1= null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        String retDate= reqFormatter.format(date1);
        Log.d("datechecker",retDate);

        return retDate;
    }
}

