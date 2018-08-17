package com.nkorchak.cameraexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import static android.graphics.BitmapFactory.decodeByteArray;

public class MainActivity extends AppCompatActivity {

    private Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = findViewById(R.id.frame_layout);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        camera = Camera.open();

        CustomCamera customCamera = new CustomCamera(this, camera);
        frameLayout.addView(customCamera);

        findViewById(R.id.btn_capture_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    camera.takePicture(null, null, mPictureCallback);
                }
            }
        });

        findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 123);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 123:
                    Uri selectedImage = data.getData();

                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    intent.putExtra("path", selectedImage.toString());
                    intent.putExtra("type", Utils.GALLERY_ID);
                    startActivity(intent);
                    finish();

                    break;
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = decodeByteArray(data, 0, data.length);
            String filePath = Utils.tempFileImage(MainActivity.this, Utils.rotateImage(bitmap), "name");
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.putExtra("path", filePath);
            intent.putExtra("type", Utils.CAPTURE_ID);
            startActivity(intent);
            finish();
        }
    };
}
