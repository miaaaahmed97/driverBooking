package com.rasai.driverBooking.CustomObject;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vehicle implements Serializable {

    private String manufacturer;
    private String model;
    private String registration;
    private String vehicleSeats;
    private String hasAc = "no";
    private String frontviewImage;
    private String backviewImage;
    private String sideviewImage;
    private String seatsImage;
    private String interiorImage1;
    private String interiorImage2;
    /*private List<String> exterior= new ArrayList<String>();
    private List<String> interior= new ArrayList<String>();
    private String exteriorImage1;
    private String exteriorImage2;
    private String exteriorImage3;
    private String interiorImage1;
    private String interiorImage2;
    private String interiorImage3;*/

    /*public String getInteriorImage2() {
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
                ", exteriorImage1='" + exteriorImage1 + '\'' +
                ", exteriorImage2='" + exteriorImage2 + '\'' +
                ", exteriorImage3='" + exteriorImage3 + '\'' +
                ", interiorImage1='" + interiorImage1 + '\'' +
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

    public String getExteriorImage1() {
        return exteriorImage1;
    }

    public void setExteriorImage1(String exteriorImage1) {
        this.exteriorImage1 = exteriorImage1;
    }

    public String getInteriorImage1() {
        return interiorImage1;
    }

    public void setInteriorImage1(String interiorImage1) {
        this.interiorImage1 = interiorImage1;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", registration='" + registration + '\'' +
                ", vehicleSeats='" + vehicleSeats + '\'' +
                ", hasAc='" + hasAc + '\'' +
                ", exterior=" + exterior +
                ", interior=" + interior +
                '}';
    }

    public List<String> getExterior() {
        return exterior;
    }

    public void setExterior(List<String> exterior) {
        this.exterior = exterior;
    }

    public List<String> getInterior() {
        return interior;
    }

    public void setInterior(List<String> interior) {
        this.interior = interior;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", registration='" + registration + '\'' +
                ", vehicleSeats='" + vehicleSeats + '\'' +
                ", hasAc='" + hasAc + '\'' +
                ", exterior=" + exterior +
                ", interior=" + interior +
                '}';
    }

    public Map<Integer, String> getExterior() {
        return exterior;
    }

    public void setExterior(Map<Integer, String> exterior) {
        this.exterior = exterior;
    }

    public Map<Integer, String> getInterior() {
        return interior;
    }

    public void setInterior(Map<Integer, String> interior) {
        this.interior = interior;
    }*/

    @Override
    public String toString() {
        return "Vehicle{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", registration='" + registration + '\'' +
                ", vehicleSeats='" + vehicleSeats + '\'' +
                ", hasAc='" + hasAc + '\'' +
                ", frontviewImage='" + frontviewImage + '\'' +
                ", backviewImage='" + backviewImage + '\'' +
                ", sideviewImage='" + sideviewImage + '\'' +
                ", seatsImage='" + seatsImage + '\'' +
                ", interiorImage1='" + interiorImage1 + '\'' +
                ", interiorImage2='" + interiorImage2 + '\'' +
                '}';
    }

    public String getFrontviewImage() {
        return frontviewImage;
    }

    public void setFrontviewImage(String frontviewImage) {
        this.frontviewImage = frontviewImage;
    }

    public String getBackviewImage() {
        return backviewImage;
    }

    public void setBackviewImage(String backviewImage) {
        this.backviewImage = backviewImage;
    }

    public String getSideviewImage() {
        return sideviewImage;
    }

    public void setSideviewImage(String sideviewImage) {
        this.sideviewImage = sideviewImage;
    }

    public String getSeatsImage() {
        return seatsImage;
    }

    public void setSeatsImage(String seatsImage) {
        this.seatsImage = seatsImage;
    }

    public String getInteriorImage1() {
        return interiorImage1;
    }

    public void setInteriorImage1(String interiorImage1) {
        this.interiorImage1 = interiorImage1;
    }

    public String getInteriorImage2() {
        return interiorImage2;
    }

    public void setInteriorImage2(String interiorImage2) {
        this.interiorImage2 = interiorImage2;
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
