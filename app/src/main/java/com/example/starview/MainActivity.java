package com.example.starview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Animation anim;
        anim = AnimationUtils.loadAnimation(this, R.anim.myrotate);
        imageView.startAnimation(anim);
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent mainIntent = new Intent(MainActivity.this, MenuActivity.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }
    }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}