package com.nkorchak.cameraexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imgView;
    private Bitmap bitmap = null;
    private File file = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imgView = findViewById(R.id.image);
        progressBar = findViewById(R.id.progressBar);

        String filePath = getIntent().getStringExtra("path");

        if (getIntent().getIntExtra("type", 1) == Utils.CAPTURE_ID) {
            File file = new File(filePath);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgView.setImageBitmap(bitmap);

        file = Utils.createImage(bitmap, 1280, 720);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.enhance_item:
                changeImage("enhance");
                return true;
            case R.id.deblur_item:
                changeImage("deblur");
                return true;
            case R.id.dehaze_item:
                changeImage("dehaze");
                return true;
            case R.id.fix_light_item:
                changeImage("lowlight");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeImage(String mode) {

        progressBar.setVisibility(View.VISIBLE);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), reqFile);

        retrofit2.Call<ResponseModel> req = CameraApplication.getApi().changeImage(mode, body);
        req.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response.body() != null) {
                    byte[] img = response.body().getImg();
                    Bitmap newBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

                    if (imgView != null) {
                        imgView.setImageBitmap(newBitmap);
                    }
                }

                if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
