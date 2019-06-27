package com.rasai.driverBooking;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.UUID;

public class Driver implements Serializable {

    private String phoneNumber;
    private String cnic;
    private String birthday;
    private String address;
    private String languages;
    private Vehicle vehicle;
    private SecurityDeposit securityDeposit;
    private String cnicImage;
    private String idImage;
    private String drivingLicenseImage;

    @Override
    public String toString() {
        return "Driver{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", cnic='" + cnic + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", languages='" + languages + '\'' +
                ", vehicle=" + vehicle +
                ", securityDeposit=" + securityDeposit +
                ", cnicImage='" + cnicImage + '\'' +
                ", idImage='" + idImage + '\'' +
                ", drivingLicenseImage='" + drivingLicenseImage + '\'' +
                '}';
    }

    public void postDriverInfo(DatabaseReference myRef) { //add to database package
        myRef.child("Driver/"+phoneNumber).push().setValue(this);
    }

    private Uri getImageUri(String imageName){
        Uri filePath;

        switch(imageName){
            case "cnicImage":
                filePath = Uri.parse(cnicImage);
                break;
            case "idImage":
                filePath = Uri.parse(idImage);
                break;
            case "drivingLicenseImage":
                filePath = Uri.parse(drivingLicenseImage);
                break;
            case "exteriorImage":
                filePath = Uri.parse(vehicle.getExteriorImage());
                break;
            case "interiorImage":
                filePath = Uri.parse(vehicle.getInteriorImage());
                break;
            case "depositImage":
                filePath = Uri.parse(securityDeposit.getDepositImage());
                break;
            default:
                filePath = Uri.parse(cnicImage);
                break;
        }
        return  filePath;
    }

    private void setImageUrl(String imageName, String url){

        switch(imageName){
            case "cnicImage":
                cnicImage = url;
                break;
            case "idImage":
                idImage = url;
                break;
            case "drivingLicenseImage":
                drivingLicenseImage =url;
                break;
            case "exteriorImage":
                vehicle.setExteriorImage(url);
                break;
            case "interiorImage":
                vehicle.setInteriorImage(url);
                break;
            case "depositImage":
                securityDeposit.setDepositImage(url);
                break;
            default:
                cnicImage = url;
                break;
        }
    }

    public void uploadImage(StorageReference storageReference, final String[] imageNameArray) {

        for(final String image_Name: imageNameArray){
            final String imageName = image_Name;
            Uri filePath = getImageUri(imageName);
            final String imageid;
            final String[] imagelink = new String[1];
            final Uri[] download_url = new Uri[1];

            if(filePath != null)
            {
                imageid="Driver/"+ phoneNumber +"/"+UUID.randomUUID().toString();
                Log.d("imagelink",imageid);

                StorageReference ref = storageReference.child(imageid);
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();

                                while(!urlTask.isSuccessful()){
                                }
                                download_url[0] = urlTask.getResult();
                                imagelink[0] = String.valueOf(download_url[0]);
                                setImageUrl(imageName, imagelink[0]);

                                Log.d("imagelink", imagelink[0]);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
            }
        }

    }

    public String getCnicImage() {
        return cnicImage;
    }

    public void setCnicImage(String cnicImage) {
        this.cnicImage = cnicImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getDrivingLicenseImage() {
        return drivingLicenseImage;
    }

    public void setDrivingLicenseImage(String drivingLicenseImage) {
        this.drivingLicenseImage = drivingLicenseImage;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
