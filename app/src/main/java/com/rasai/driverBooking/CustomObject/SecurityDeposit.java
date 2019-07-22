package com.rasai.driverBooking.CustomObject;

import java.io.Serializable;

public class SecurityDeposit implements Serializable {

    private String depositDate;
    private int amount;
    private String depositImage;

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDepositImage() {
        return depositImage;
    }

    public void setDepositImage(String depositImage) {
        this.depositImage = depositImage;
    }

    @Override
    public String toString() {
        return "SecurityDeposit{" +
                "depositDate='" + depositDate + '\'' +
                ", amount='" + amount + '\'' +
                ", depositImage='" + depositImage + '\'' +
                '}';
    }
}
