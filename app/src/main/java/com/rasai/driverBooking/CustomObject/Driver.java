package com.dryver.driverBooking.CustomObject;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Driver implements Serializable {

    private String name;
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
    private String databaseId;
    private String token_id;
    private float rating;
    private List<String> offersMade = new ArrayList<>();
    private List<String> offerAccepted = new ArrayList<>();
    private List<String> tripsCompleted = new ArrayList<>();

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cnic='" + cnic + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", languages='" + languages + '\'' +
                ", vehicle=" + vehicle +
                ", securityDeposit=" + securityDeposit +
                ", cnicImage='" + cnicImage + '\'' +
                ", idImage='" + idImage + '\'' +
                ", drivingLicenseImage='" + drivingLicenseImage + '\'' +
                ", databaseId='" + databaseId + '\'' +
                ", token_id='" + token_id + '\'' +
                ", rating=" + rating +
                ", offersMade=" + offersMade +
                ", offerAccepted=" + offerAccepted +
                ", tripsCompleted=" + tripsCompleted +
                '}';
    }

    private void postDriverInfo(DatabaseReference myRef) { //add to database package
        myRef.child("Driver/"+phoneNumber).setValue(this);
    }

    private Uri getImageUri(String image){
        Uri filePath;

        switch(image){
            case "cnicImage":
                filePath = Uri.parse(cnicImage);
                break;
            case "idImage":
                filePath = Uri.parse(idImage);
                break;
            case "drivingLicenseImage":
                Log.d("Driver", "drivingLicenseImage: "+drivingLicenseImage);
                filePath = Uri.parse(drivingLicenseImage);
                break;
            case "frontviewImage":
                Log.d("Driver", "vehicle.getFrontviewImage(): "+vehicle.getFrontviewImage());
                filePath = Uri.parse(vehicle.getFrontviewImage());
                break;
            case "backviewImage":
                filePath = Uri.parse(vehicle.getBackviewImage());
                break;
            case "sideviewImage":
                filePath = Uri.parse(vehicle.getSideviewImage());
                break;
            case "seatsImage":
                filePath = Uri.parse(vehicle.getSeatsImage());
                break;
            case "interiorImage1":
                filePath = Uri.parse(vehicle.getInteriorImage1());
                break;
            case "interiorImage2":
                filePath = Uri.parse(vehicle.getInteriorImage2());
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

    private void setImageUrl(int imageCount, String url){

        Log.d("Driver Upload", "inside setImageUrl() with imageCount : "+imageCount);
        Log.d("Driver Upload", "inside setImageUrl() with url: "+ url);

        switch(imageCount){
            case 0:
                cnicImage = url;
                break;
            case 1:
                idImage = url;
                break;
            case 2:
                drivingLicenseImage =url;
                break;
            case 3:
                vehicle.setFrontviewImage(url);
                break;
            case 4:
                vehicle.setBackviewImage(url);
                break;
            case 5:
                vehicle.setSideviewImage(url);
                break;
            case 6:
                vehicle.setSeatsImage(url);
                break;
            case 7:
                vehicle.setInteriorImage1(url);
                break;
            case 8:
                vehicle.setInteriorImage2(url);
                break;
            case 9:
                securityDeposit.setDepositImage(url);
                break;
            default:
                cnicImage = url;
                break;
        }
    }

    public void uploadImage(StorageReference storageReference, final String[] imageNameArray) {

        final int[] counter = {0};

        String[] imageTrackerArray = {"cnicImage", "idImage", "drivingLicenseImage",
                "frontviewImage", "backviewImage", "sideviewImage", "seatsImage",
                "interiorImage1", "interiorImage2", "depositImage"};

        for(final String image: imageTrackerArray){

            Log.d("Driver",  "image: "+image);

            Uri filePath = getImageUri(image);

            final String imageid;

            if(filePath != null)
            {
                imageid="Driver/"+ phoneNumber +"/"+UUID.randomUUID().toString();
                Log.d("imagelink",imageid);

                StorageReference ref = storageReference.child(imageid);
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("Driver Upload", "onSuccess: uri= "+ uri.toString());
                                        setImageUrl(counter[0], uri.toString());
                                        counter[0] +=1;

                                        if (counter[0] == imageTrackerArray.length) {

                                            DatabaseReference mDriver = FirebaseDatabase.getInstance().getReference();
                                            postDriverInfo(mDriver);
                                        }
                                    }
                                });
                                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();

                                while(!urlTask.isSuccessful()){
                                }
                                /*download_url[0] = urlTask.getResult();
                                imagelink[0] = String.valueOf(download_url[0]);
                                setImageUrl(imageName, imagelink[0]);

                                Log.d("imagelink", imagelink[0]);*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Driver", "image upload failure exception: "+e.getMessage());
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

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
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

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public List<String> getOffersMade() {
        return offersMade;
    }

    public void setOffersMade(List<String> offersMade) {
        this.offersMade = offersMade;
    }

    public List<String> getOfferAccepted() {
        return offerAccepted;
    }

    public void setOfferAccepted(List<String> offerAccepted) {
        this.offerAccepted = offerAccepted;
    }

    public List<String> getTripsCompleted() {
        return tripsCompleted;
    }

    public void setTripsCompleted(List<String> tripsCompleted) {
        this.tripsCompleted = tripsCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
