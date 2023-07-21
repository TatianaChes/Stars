package com.example.starview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.starview.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {
    private ActivityMenuBinding binding;
    private Animation mEnlargeAnimation;
    int DELAY = 1000;
    Handler handler;
    ImageView imgRecearch, imgScan, imgPutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        handler = new Handler();
        getPermission();
        imgRecearch = findViewById(R.id.recearch);
        imgScan = findViewById(R.id.scan);
        imgPutImage = findViewById(R.id.putImage);
        mEnlargeAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge);

    }

    public void Open(View view) throws InterruptedException { // тут все норм

        imgRecearch.startAnimation(mEnlargeAnimation);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MenuActivity.this, ListStar.class);
                startActivity(intent);
                finish();
            }
        }, DELAY);

    }


    public void ImagesOpen(View view) throws InterruptedException {

        imgPutImage.startAnimation(mEnlargeAnimation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MenuActivity.this, PhotoActivity.class);
                intent.putExtra("requestCode", 1);
                startActivity(intent);
            }
        }, DELAY);

    }

    public void CameraOpen(View view) throws InterruptedException {

        imgScan.startAnimation(mEnlargeAnimation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MenuActivity.this, PhotoActivity.class);
                intent.putExtra("requestCode", 12);
                startActivity(intent);
            }
        }, DELAY);


    }


    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.CAMERA}, 11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}