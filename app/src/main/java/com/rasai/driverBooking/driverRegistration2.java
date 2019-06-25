package com.rasai.driverBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.graphics.ImageDecoder.decodeBitmap;

public class driverRegistration2 extends AppCompatActivity {

    /*
    public static final int GET_FROM_GALLERY = 1;
    ImageButton addIDButton, addCNICButton, addDrivLicButton;

    class ImageButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        }
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration2);

        /*addIDButton = findViewById(R.id.addIDPicture);
        addCNICButton=findViewById(R.id.addCnic);
        addDrivLicButton = findViewById(R.id.addDrivingLicense);

        addIDButton.setOnClickListener(new ImageButtonListener());
        addCNICButton.setOnClickListener(new ImageButtonListener());
        addDrivLicButton.setOnClickListener(new ImageButtonListener());
        */

    }
    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                displayImage(bitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            displayImage(picturePath);

        }


    }

    //displays the uploaded image next to the upload icon
    public void displayImage(Bitmap bmp){
        ImageView imageView = (ImageView) findViewById(R.id.showIDPicture);
        imageView.setImageBitmap(bmp);
    }
*/
    public void registerDriver(View v){
        if (v.getId()==R.id.registerDriverButton) {
            Intent i = new Intent(driverRegistration2.this, vehicleRegistration.class);
            startActivity(i);

        }

    }
}

