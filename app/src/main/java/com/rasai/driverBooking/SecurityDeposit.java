package com.rasai.driverBooking;

public class SecurityDeposit {

    private String depositDate;
    private String amount;

    @Override
    public String toString() {
        return "SecurityDeposit{" +
                "depositDate='" + depositDate + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
