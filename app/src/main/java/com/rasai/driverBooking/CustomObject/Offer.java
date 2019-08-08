package com.rasai.driverBooking.CustomObject;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Offer implements Serializable {

    private String amount;
    private String driverPhoneNumber;
    private String tripID;
    private String acceptanceStatus = "unconfirmed";
    private String customerPhoneNumber;
    private String token_id;
    List<String> offers;
    private Driver driver = new Driver();

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
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
                ", acceptanceStatus='" + acceptanceStatus + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", token_id='" + token_id + '\'' +
                '}';
    }

    public void makeOffer(DatabaseReference myRef){
        myRef.child("Offer/"+"/"+tripID+"/"+driverPhoneNumber).setValue(this);
        //offers.add(tripID);
        myRef.child("Driver").child(driverPhoneNumber).child("offersMade/").push().setValue(tripID);


        /*DatabaseReference myReference =  myRef.child("Offer/").push();
        databaseId = myReference.getKey();
        myRef.child("Offer").child(databaseId).setValue(this);*/
    }

    public void changeOffer(DatabaseReference myRef){
        myRef.child("Offer/"+"/"+tripID+"/"+driverPhoneNumber).child("amount").setValue(amount);
        //myRef.child("Offer").child(databaseId).child("amount").setValue(amount);
    }
}
