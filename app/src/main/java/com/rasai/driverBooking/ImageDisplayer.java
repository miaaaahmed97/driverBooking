package com.rasai.driverBooking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDisplayer {

    Intent buttonIntent;
    Driver driver;
    int resources;
    Context context;
    String stringID;

    public ImageDisplayer(Driver driver, int resource, Context context, String stringID) {
        this.driver = driver;
        this.resources = resource;
        this.context = context;
        this.stringID = stringID;
    }


    //displays the uploaded image next to the upload icon
    public void displayImage(Bitmap bmp, Uri uri){
        Log.d("testing", "Inside displayimage");
        ViewGroup viewGroup = null;

            LayoutInflater myCustomInflater = LayoutInflater.from(context);
            View customView = myCustomInflater.inflate(resources, viewGroup, false);

            ImageView imageView;
            //get ID of calling button
            //String stringID= buttonIntent.getExtras().getString("EXTRA");
            int intID =Integer.parseInt(stringID);

            switch(resources){
                case R.layout.activity_driver_registration2:
                    switch (intID){
                        case R.id.addExterior:
                            imageView = customView.findViewById(R.id.showIDPicture);
                            driver.setIdImage(uri.toString());
                            break;
                        case R.id.addCnic:
                            imageView = customView.findViewById(R.id.showCNIC);
                            driver.setCnicImage(uri.toString());
                            break;
                        case R.id.addDrivingLicense:
                            imageView = customView.findViewById(R.id.showDeposit);
                            driver.setDrivingLicenseImage(uri.toString());
                            break;
                        default:
                            imageView = customView.findViewById(R.id.showDeposit);
                            break;
                    }
                    Log.d("pleasee", driver.toString());
                    imageView.setImageBitmap(bmp);
            }

    }
}
