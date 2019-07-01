package com.rasai.driverBooking.CustomObject;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Offer implements Serializable {

    private String amount;
    private String driverPhoneNumber;
    private String tripID;
    private String databaseId;
    private String acceptanceStatus = "unconfirmed";

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getAcceptanceStatus() {
        return acceptanceStatus;
    }

    public void setAcceptanceStatus(String acceptanceStatus) {
        this.acceptanceStatus = acceptanceStatus;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "amount='" + amount + '\'' +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                ", tripID='" + tripID + '\'' +
                ", databaseId='" + databaseId + '\'' +
                ", acceptanceStatus='" + acceptanceStatus + '\'' +
                '}';
    }

    public void makeOffer(DatabaseReference myRef){
        DatabaseReference myReference =  myRef.child("Offer/").push();
        databaseId = myReference.getKey();
        myRef.child("Offer").child(databaseId).setValue(this);
    }

    public void changeOffer(DatabaseReference myRef){
        myRef.child("Offer").child(databaseId).child("amount").setValue(amount);
    }
}
