package com.rasai.driverBooking.CustomObject;

import android.net.Uri;

import java.io.Serializable;

public class Vehicle implements Serializable {

    private String manufacturer;
    private String model;
    private String registration;
    private String vehicleSeats;
    private String hasAc = "no";
    private String exteriorImage;
    private String exteriorImage2;
    private String exteriorImage3;
    private String interiorImage;
    private String interiorImage2;
    private String interiorImage3;

    public String getInteriorImage2() {
        return interiorImage2;
    }

    public void setInteriorImage2(String interiorImage2) {
        this.interiorImage2 = interiorImage2;
    }

    public String getInteriorImage3() {
        return interiorImage3;
    }

    public void setInteriorImage3(String interiorImage3) {
        this.interiorImage3 = interiorImage3;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", registration='" + registration + '\'' +
                ", vehicleSeats='" + vehicleSeats + '\'' +
                ", hasAc='" + hasAc + '\'' +
                ", exteriorImage='" + exteriorImage + '\'' +
                ", exteriorImage2='" + exteriorImage2 + '\'' +
                ", exteriorImage3='" + exteriorImage3 + '\'' +
                ", interiorImage='" + interiorImage + '\'' +
                ", interiorImage2='" + interiorImage2 + '\'' +
                ", interiorImage3='" + interiorImage3 + '\'' +
                '}';
    }

    public String getExteriorImage2() {
        return exteriorImage2;
    }

    public void setExteriorImage2(String exteriorImage2) {
        this.exteriorImage2 = exteriorImage2;
    }

    public String getExteriorImage3() {
        return exteriorImage3;
    }

    public void setExteriorImage3(String exteriorImage3) {
        this.exteriorImage3 = exteriorImage3;
    }

    public String getExteriorImage() {
        return exteriorImage;
    }

    public void setExteriorImage(String exteriorImage) {
        this.exteriorImage = exteriorImage;
    }

    public String getInteriorImage() {
        return interiorImage;
    }

    public void setInteriorImage(String interiorImage) {
        this.interiorImage = interiorImage;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getVehicleSeats() {
        return vehicleSeats;
    }

    public void setVehicleSeats(String vehicleSeats) {
        this.vehicleSeats = vehicleSeats;
    }

    public String getHasAc() {
        return hasAc;
    }

    public void setHasAc(String hasAc) {
        this.hasAc = hasAc;
    }


}
