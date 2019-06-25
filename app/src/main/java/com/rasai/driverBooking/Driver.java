package com.rasai.driverBooking;

import java.util.ArrayList;

public class Driver {

    private String cnic;
    private String birthday;
    private String address;
    private ArrayList<String> languages;
    private Vehicle vehicle;
    private SecurityDeposit securityDeposit;

    @Override
    public String toString() {
        return "Driver{" +
                "cnic='" + cnic + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", languages=" + languages +
                ", vehicle=" + vehicle +
                ", securityDeposit=" + securityDeposit +
                '}';
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public SecurityDeposit getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(SecurityDeposit securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

}
