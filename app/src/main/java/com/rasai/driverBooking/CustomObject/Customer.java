package com.rasai.driverBooking.CustomObject;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private float rating;
    private int ratedBy;
    private String phoneNumber;
    private String name;
    private List<String> tripsCompleted = new ArrayList<>();
    private List<String> tripsPosted = new ArrayList<>();

    public int getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(int ratedBy) {
        this.ratedBy = ratedBy;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTripsCompleted() {
        return tripsCompleted;
    }

    public void setTripsCompleted(List<String> tripsCompleted) {
        this.tripsCompleted = tripsCompleted;
    }

    public List<String> getTripsPosted() {
        return tripsPosted;
    }

    public void setTripsPosted(List<String> tripsPosted) {
        this.tripsPosted = tripsPosted;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "rating=" + rating +
                ", ratedBy=" + ratedBy +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", tripsCompleted=" + tripsCompleted +
                ", tripsPosted=" + tripsPosted +
                '}';
    }
}