package com.rasai.driverBooking;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.List;

public class Driver implements Serializable {

    private String phoneNumber;
    private String cnic;
    private String birthday;
    private String address;
    private String languages;
    private Vehicle vehicle;
    private SecurityDeposit securityDeposit;
    private Uri cnicImage;
    private Uri idImage;
    private Uri drivingLicenseImage;

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

    public void storeDriverImage (StorageReference storageRef, final String fieldName, Uri file){
        final StorageReference mStorageRef = storageRef.child("Driver/"+phoneNumber+fieldName);
        UploadTask uploadTask = mStorageRef.putFile(file);

        //upload image to firebase
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("checking", fieldName+" addedd succcessfully");
            }
        });

        //get download uri
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    switch(fieldName){
                        case "cnicImage":
                            cnicImage = downloadUri;
                            break;
                        case "idImage":
                            idImage = downloadUri;
                            break;
                        case "drivingLicenseImage":
                            drivingLicenseImage = downloadUri;
                            break;
                        default:
                            Log.d("checking", "Inside default of store Image; Drive.java");
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void postDriverInfo(DatabaseReference mRef){}

    public Uri getCnicImage() {
        return cnicImage;
    }

    public void setCnicImage(Uri cnicImage) {
        this.cnicImage = cnicImage;
    }

    public Uri getIdImage() {
        return idImage;
    }

    public void setIdImage(Uri idImage) {
        this.idImage = idImage;
    }

    public Uri getDrivingLicenseImage() {
        return drivingLicenseImage;
    }

    public void setDrivingLicenseImage(Uri drivingLicenseImage) {
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
