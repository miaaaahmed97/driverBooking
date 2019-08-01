package com.rasai.driverBooking.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rasai.driverBooking.CustomObject.Driver;
import com.rasai.driverBooking.CustomObject.Vehicle;
import com.rasai.driverBooking.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class vehicleRegistration2 extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private ImageView mExterior, mExterior2, mExterior3, mInterior, mInterior2, mInterior3;
    private Intent buttonIntent;
    private Button mRegisterVehicle2Button;

    private Vehicle vehicleInformation;
    private Driver driverInformation;

    int radius = 15; // corner radius, higher value = more rounded
    int margin = 5; // crop margin, set to 0 for corners with no crop

    public Vehicle getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(Vehicle vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }

    public Driver getDriverInformation() {
        return driverInformation;
    }

    public void setDriverInformation(Driver driverInformation) {
        this.driverInformation = driverInformation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration2);

        getSupportActionBar().hide();

        mExterior = findViewById(R.id.showExterior);
        mExterior2 = findViewById(R.id.showExterior2);
        mExterior3 = findViewById(R.id.showExterior3);
        mInterior = findViewById(R.id.showInterior);
        mInterior2 = findViewById(R.id.showInterior2);
        mInterior3 = findViewById(R.id.showInterior3);
        mRegisterVehicle2Button = findViewById(R.id.RegisterVehicle2Button);

        mExterior.setOnClickListener(new ImageViewListener());
        mExterior2.setOnClickListener(new ImageViewListener());
        mExterior3.setOnClickListener(new ImageViewListener());
        mInterior.setOnClickListener(new ImageViewListener());
        mInterior2.setOnClickListener(new ImageViewListener());
        mInterior3.setOnClickListener(new ImageViewListener());

    }

    private class ImageViewListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //calls gallery
            buttonIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //buttonIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //buttonIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
            buttonIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
            //get ID of calling button
            String viewID= String.valueOf(v.getId());
            buttonIntent.putExtra("EXTRA",viewID);
            startActivityForResult(Intent.createChooser(buttonIntent,"Select an Image"), GET_FROM_GALLERY);
        }
    }
    //called automatically after any button is clicked and gallery intent is made
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            displayImage(selectedImage);

            /*if(data.getClipData() != null) {
                //evaluate the count to display message
                int count = data.getClipData().getItemCount();
                if(count<3){
                    Toast.makeText(getBaseContext(), "Please upload 3 images", Toast.LENGTH_LONG).show();
                }
                else if (count==3){
                    displayMultipleImages(data.getClipData());
                }else{
                    Toast.makeText(getBaseContext(), "Only 3 images will be uploaded", Toast.LENGTH_LONG).show();
                    displayMultipleImages(data.getClipData());
                }

                //do something with the image (save it to some directory or whatever you need to do with it here)
            }*/

                //imageUri = data.getData();
                //do something with the image (save it to some directory or whatever you need to do with it here)
                //Toast.makeText(getBaseContext(), "Please upload 3 images", Toast.LENGTH_LONG).show();

        }

    }

    private void displayImage(Uri uri) {
        ImageView imageview;

        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.showExterior:
                imageview = mExterior;
                //getVehicleInformation().setExteriorImage(uri.toString());
                break;
            case R.id.showExterior2:
                imageview = mExterior2;
                //getVehicleInformation().setExteriorImage2(uri.toString());
                break;
            case R.id.showExterior3:
                imageview = mExterior3;
                //getVehicleInformation().setExteriorImage3(uri.toString());
                break;
            case R.id.showInterior:
                imageview = mInterior;
                //getVehicleInformation().setInteriorImage(uri.toString());
                break;
            case R.id.showInterior2:
                imageview = mInterior2;
                //getVehicleInformation().setInteriorImage2(uri.toString());
                break;
            case R.id.showInterior3:
                imageview = mInterior3;
                //getVehicleInformation().setInteriorImage3(uri.toString());
                break;
                default: imageview = mExterior;break;
        }
        Glide.with(vehicleRegistration2.this)
                .load(uri)
                .apply(new RequestOptions().centerCrop().transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.add_image_holder))
                .into(imageview);

    }

    /*public void displayMultipleImages(ClipData clipData){
        Uri uri;
        ImageView imageview;

        String stringID= buttonIntent.getExtras().getString("EXTRA");
        int intID =Integer.parseInt(stringID);
        switch (intID){
            case R.id.exterior_textview:
                for(int i = 0; i < 3; i++){
                    uri = clipData.getItemAt(i).getUri();
                    switch (i){
                        case 0:
                            imageview = findViewById(R.id.showExterior);
                            getVehicleInformation().setExteriorImage(uri.toString());
                            break;
                        case 1:
                            imageview = findViewById(R.id.showExterior2);
                            getVehicleInformation().setExteriorImage2(uri.toString());
                            break;
                        case 2:
                            imageview = findViewById(R.id.showExterior3);
                            getVehicleInformation().setExteriorImage3(uri.toString());
                            break;

                        //to stop may not have been initialized errors
                        default:imageview = findViewById(R.id.showExterior);break;

                    }
                    Glide.with(vehicleRegistration2.this)
                            .load(uri)
                            .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                            .into(imageview);
                }

                break;
            case R.id.interior_textview:

                for(int i = 0; i < 3; i++){
                    uri = clipData.getItemAt(i).getUri();
                    switch (i){
                        case 0:
                            imageview = findViewById(R.id.showInterior);
                            getVehicleInformation().setInteriorImage(uri.toString());
                            break;
                        case 1:
                            imageview = findViewById(R.id.showInterior2);
                            getVehicleInformation().setInteriorImage2(uri.toString());
                            break;
                        case 2:
                            imageview = findViewById(R.id.showInterior3);
                            getVehicleInformation().setInteriorImage3(uri.toString());
                            break;

                        //to stop may not have been initialized errors
                        default:imageview = findViewById(R.id.showInterior);break;

                    }
                    Glide.with(vehicleRegistration2.this)
                            .load(uri)
                            .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_car))
                            .into(imageview);
                }
                break;
        }
    }*/
}
