package com.rasai.driverBooking;

import java.io.Serializable;
import java.util.List;

public class Driver implements Serializable {

    private String cnic;
    private String birthday;
    private String address;
    private String languages;
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

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
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
