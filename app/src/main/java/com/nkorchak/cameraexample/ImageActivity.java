package com.nkorchak.cameraexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Bitmap bitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imgView = findViewById(R.id.image);
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
                progressBar.setVisibility(View.VISIBLE);
                //your code
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 1000);
                return true;
            case R.id.deblur_item:
                //your code
                return true;
            case R.id.dehaze_item:
                //your code
                return true;
            case R.id.fix_light_item:
                //your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
