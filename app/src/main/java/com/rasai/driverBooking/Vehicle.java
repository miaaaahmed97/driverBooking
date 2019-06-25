package com.rasai.driverBooking;

public class Vehicle {

    private String manufacturer;
    private String model;
    private String registration;
    private String vehicleSeats;
    private String hasAc = "no";

    @Override
    public String toString() {
        return "Vehicle{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", registration='" + registration + '\'' +
                ", vehicleSeats='" + vehicleSeats + '\'' +
                ", hasAc='" + hasAc + '\'' +
                '}';
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
